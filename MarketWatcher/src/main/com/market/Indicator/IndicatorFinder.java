package com.market.Indicator; 

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.market.Core.ConditionAction;
import com.market.Core.CustomTick;
import com.market.Core.LocalConstants;
import com.market.Core.LocalConstants.ConditionTypes;
import com.market.Core.LocalConstants.TradeAction;
import com.market.Core.PrioritiesCount;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;

public class IndicatorFinder {

	ConcurrentHashMap<String, TreeMap<Date, CustomTick>> MasterlastFiveTicks = new ConcurrentHashMap<String, TreeMap<Date, CustomTick>> ();

	public OrderParams findTheTrendAndExecute(CustomTick customTick) {
		// TODO Auto-generated method stub
		
		TreeMap<Date, CustomTick> lastFiveTicks =  getLastFiveTicks(customTick);
		
		createMasteListOfLastFiveTicks(customTick);
		
		if(lastFiveTicks.size() <= 1)
			return null;
		
		Condition c = getTheCondition();
		
		TradeAction action = conditionPassed(c, customTick, lastFiveTicks); 
		
		if(action != TradeAction.NONE)
		{
			return PlaceBracketOrder(customTick, action);
		}
		return null;
	}

	private OrderParams PlaceBracketOrder(CustomTick customTick, TradeAction action) {
		// TODO Auto-generated method stub
		
		OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        //orderParams.price = 30.5;
        orderParams.transactionType = (action==TradeAction.BUY)?Constants.TRANSACTION_TYPE_BUY:Constants.TRANSACTION_TYPE_SELL;
        orderParams.tradingsymbol = customTick.symbol;
        orderParams.trailingStoploss = 1.0;
        orderParams.stoploss = 1.0;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.squareoff = 3.0;
        orderParams.product = Constants.PRODUCT_MIS;
        
        return orderParams;
        
	}

	public TreeMap<Date, CustomTick> getLastFiveTicks(CustomTick customTick) 
	{
		if (MasterlastFiveTicks.containsKey(customTick.symbol)) 
        {
			return MasterlastFiveTicks.get(customTick.symbol);
        }
		else 
			return new TreeMap<Date, CustomTick>();
	}
	
	public void createMasteListOfLastFiveTicks(CustomTick customTick) 
	{
		TreeMap<Date, CustomTick> sortedCustomTickList;
		if (MasterlastFiveTicks.containsKey(customTick.symbol)) 
        {
            sortedCustomTickList = MasterlastFiveTicks.get(customTick.symbol);
            sortedCustomTickList.put(customTick.time, customTick);
            
            if (sortedCustomTickList.size() > 5)
            	sortedCustomTickList.remove(sortedCustomTickList.firstKey());
        }
        else 
        {
            sortedCustomTickList = new TreeMap<Date, CustomTick>(Collections.reverseOrder());
            sortedCustomTickList.put(customTick.time, customTick);
            MasterlastFiveTicks.put(customTick.symbol, sortedCustomTickList) ;
        }
	}
	
	private TradeAction conditionPassed(Condition c, CustomTick customTick, TreeMap<Date, CustomTick> lastFiveTicks) {
		
		TradeAction action = TradeAction.NONE;
		PrioritiesCount priorityCountBuy = CheckConidtionsBuy(c, customTick, lastFiveTicks);
		
		if (priorityCountBuy.getTotalCount() == 7 ||
				priorityCountBuy.highPriorityCount == 4 ||
				(priorityCountBuy.highPriorityCount == 3 && priorityCountBuy.mediumPriorityCount==1))
			return TradeAction.BUY;
		
		return action;
	}

	private PrioritiesCount CheckConidtionsBuy(Condition c, CustomTick customTick, TreeMap<Date, CustomTick> lastFiveTicks) {
		PrioritiesCount priority = new PrioritiesCount();
		
		for(ConditionAction conditionAction : LocalConstants.listOfActions) 
		{
			if(conditionAction.conditionType == ConditionTypes.BUY_QTY_MORE_THAN_SELL) 
			{
				if (customTick.getTick().getTotalBuyQuantity() > customTick.getTick().getTotalSellQuantity() ) 
				{
					double changeQty = customTick.getTick().getTotalBuyQuantity() - customTick.getTick().getTotalSellQuantity();
					
					Map.Entry<Date, CustomTick> preTickEntry = lastFiveTicks.firstEntry();
					double preTickChangeQty = preTickEntry.getValue().getTick().getTotalBuyQuantity() - preTickEntry.getValue().getTick().getTotalSellQuantity();
					
					int prcnt = Math.abs((int) (changeQty *100 /preTickChangeQty));
					
					if(prcnt > 10) 
					{
						conditionAction.isPassed =true;
						priority.highPriorityCount++;
					}
				}
				
			}
			if(conditionAction.conditionType == ConditionTypes.LAST_FIVE_TICKS_UP) 
			{
				if(ConsicutiveIncreaseInLastFiveTicks(customTick, lastFiveTicks)) 
				{
					conditionAction.isPassed =true;
					priority.highPriorityCount++;
				}
			}
			if(conditionAction.conditionType == ConditionTypes.DIRECTION_CHANGED_UP) 
			{
				if(customTick.isSwitched && customTick.currentState.equals("Buy")) 
				{
					conditionAction.isPassed =true;
					priority.highPriorityCount++;
				}
			}
			if(conditionAction.conditionType == ConditionTypes.LAST_30_SEC_PRICE_VOL_UP) 
			{
				if(customTick.last30SecPriceChange >0 && customTick.last30SecPriceChange > c.last30secChangePrice &&
						customTick.last30SecVolumeChange > 0 && customTick.last30SecVolumeChange > c.last30secChangeVol) 
				{
					conditionAction.isPassed =true;
					priority.highPriorityCount++;
				}
			}
			if(conditionAction.conditionType == ConditionTypes.LAST_1_MIN_PRICE_VOL_UP) 
			{
				if(customTick.lastOneMinPriceChange >0 && customTick.lastOneMinPriceChange > c.last1minChangePrice &&
						customTick.lastOneMinVolumeChange > 0 && customTick.lastOneMinVolumeChange > c.last1minChangeVol) 
				{
					conditionAction.isPassed =true;
					priority.mediumPriorityCount++;
				}
			}
			if(conditionAction.conditionType == ConditionTypes.LAST_2_MIN_PRICE_VOL_UP) 
			{
				if(customTick.lastTwoMinPriceChange >0 && customTick.lastTwoMinPriceChange > c.last2minChangePrice &&
						customTick.lastTwoMinVolumeChange > 0 && customTick.lastTwoMinVolumeChange > c.last2minChangeVol) 
				{
					conditionAction.isPassed =true;
					priority.lowPriorityCount++;
				}
			}
			if(conditionAction.conditionType == ConditionTypes.LAST_5_MIN_PRICE_VOL_UP) 
			{
				if(customTick.lastFiveMinPriceChange >0 && customTick.lastFiveMinPriceChange > c.last5minChangePrice &&
						customTick.lastFiveMinVolumeChange > 0 && customTick.lastFiveMinVolumeChange > c.last5minChangeVol) 
				{
					conditionAction.isPassed =true;
					priority.lowPriorityCount++;
				}
			}
			
		}
		
		return priority;
	}

	private boolean ConsicutiveIncreaseInLastFiveTicks(CustomTick customTick, TreeMap<Date, CustomTick> lastFiveTicks) {
		boolean isIncreasing=false;
		if (customTick.getTick().getTotalBuyQuantity() > customTick.getTick().getTotalSellQuantity() ) 
		{
			double previosDiff=0;
			int conseqCount=0;
			for (Map.Entry<Date, CustomTick> preTick : lastFiveTicks.entrySet()) 
			{
				Date key = preTick.getKey();
				CustomTick value = preTick.getValue();
				
				if (value.getTick().getTotalBuyQuantity() > value.getTick().getTotalSellQuantity() ) 
				{
					double difference = value.getTick().getTotalBuyQuantity() - value.getTick().getTotalSellQuantity() ;
					
					if(previosDiff!=0) 
					{
						if(difference > previosDiff)
							break;
						else 
						{
							conseqCount++;
						}
					}
					previosDiff = difference;
				}
			}
			
			if(conseqCount==5) 
			{
				isIncreasing =true;
			}
		}
		return isIncreasing;
	}

	private Condition getTheCondition() {
		Condition condition = Condition.getInstance();
		
		return condition;
	}

}
