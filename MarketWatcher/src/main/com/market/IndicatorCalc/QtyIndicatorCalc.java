package com.market.IndicatorCalc;

import java.util.HashMap;

import com.market.Core.CustomTick;
import com.market.Core.QuantityIndicator;

public class QtyIndicatorCalc 
{
	HashMap<String, QuantityIndicator> symbolIndicatorMap = new HashMap<String, QuantityIndicator>();
	
	public QtyIndicatorCalc() 
	{}
	
	public void calculate(CustomTick customTick) 
	{
		if(symbolIndicatorMap.containsKey(customTick.symbol)) 
		{
			QuantityIndicator qtyInd = symbolIndicatorMap.get(customTick.symbol);
			String newState = getCurrentState(customTick);
			
			if (!newState.equals(qtyInd.getCurrentState())) 
			{
				qtyInd.setIsSwitched(true);
				qtyInd.setQtySinceChanged(0);
				
				customTick.isSwitched =true;
				customTick.qtySinceChanged =0;
			}
			else 
			{
				customTick.isSwitched = false;
			}
			double difference = customTick.getTick().getTotalBuyQuantity() - customTick.getTick().getTotalSellQuantity();
			//double currentQty = qtyInd.getQtySinceChanged();
			qtyInd.setQtySinceChanged(difference);
			customTick.qtySinceChanged =difference;
			customTick.currentState = newState;
		}
		else 
		{
			QuantityIndicator qtyInd = new QuantityIndicator();
			qtyInd.setIsSwitched(false);
			qtyInd.setQtySinceChanged(0);
			qtyInd.setSymbol(customTick.symbol);
			qtyInd.setCurrentState(getCurrentState(customTick));
			
			customTick.isSwitched =false;
			customTick.qtySinceChanged =0;
			customTick.currentState =getCurrentState(customTick);
			
			symbolIndicatorMap.put(customTick.symbol, qtyInd);
			customTick.setQtyIndicator(qtyInd);
		}
	}

	private String getCurrentState(CustomTick customTick) {
		// TODO Auto-generated method stub
		
		if(customTick.getTick().getTotalBuyQuantity() >= customTick.getTick().getTotalSellQuantity()) 
		{
			return "Buy";
		}
		else 
		{
			return "Sell";
		}
		
	}

}
