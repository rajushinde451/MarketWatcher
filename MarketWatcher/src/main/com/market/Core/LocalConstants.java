package com.market.Core;

import java.util.ArrayList;

public class LocalConstants
{
	public static ArrayList<ConditionAction> listOfActions = getListOfActions();
	
	public static enum TradeAction
	{
		BUY,
		SELL,
		NONE
	}
	
	public static enum Priority
	{
		HIGH,
		MEDIUM,
		LOW
	}
	
	public static enum ConditionTypes {

		// Uptrend
		BUY_QTY_MORE_THAN_SELL,
		LAST_FIVE_TICKS_UP,
		DIRECTION_CHANGED_UP,
		LAST_30_SEC_PRICE_VOL_UP,
		LAST_1_MIN_PRICE_VOL_UP,
		LAST_2_MIN_PRICE_VOL_UP,
		LAST_5_MIN_PRICE_VOL_UP,
		
		// DownTrend
		SELL_QTY_MORE_THAN_BUY,
		LAST_FIVE_TICKS_DOWN,
		DIRECTION_CHANGED_DOWN,
		LAST_30_SEC_PRICE_VOL_DOWN,
		LAST_1_MIN_PRICE_VOL_DOWN,
		LAST_2_MIN_PRICE_VOL_DOWN,		
		LAST_5_MIN_PRICE_VOL_DOWN	
		
	}

	private static ArrayList<ConditionAction> getListOfActions() {
		ArrayList<ConditionAction> listOfActionsLocal = new ArrayList<ConditionAction> ();
		
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.BUY_QTY_MORE_THAN_SELL,false,TradeAction.BUY,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.SELL_QTY_MORE_THAN_BUY,false,TradeAction.SELL,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_FIVE_TICKS_UP,false,TradeAction.BUY,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_FIVE_TICKS_DOWN,false,TradeAction.SELL,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_30_SEC_PRICE_VOL_UP,false,TradeAction.BUY,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_30_SEC_PRICE_VOL_DOWN,false,TradeAction.SELL,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_1_MIN_PRICE_VOL_UP,false,TradeAction.BUY,Priority.MEDIUM));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_1_MIN_PRICE_VOL_DOWN,false,TradeAction.SELL,Priority.MEDIUM));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_2_MIN_PRICE_VOL_UP,false,TradeAction.BUY,Priority.LOW));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_2_MIN_PRICE_VOL_DOWN,false,TradeAction.SELL,Priority.LOW));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_5_MIN_PRICE_VOL_UP,false,TradeAction.BUY,Priority.LOW));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.LAST_5_MIN_PRICE_VOL_DOWN,false,TradeAction.SELL,Priority.LOW));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.DIRECTION_CHANGED_UP,false,TradeAction.BUY,Priority.HIGH));
		listOfActionsLocal.add(new ConditionAction(ConditionTypes.DIRECTION_CHANGED_DOWN,false,TradeAction.SELL,Priority.HIGH));
		
		return listOfActionsLocal;
	}

}



