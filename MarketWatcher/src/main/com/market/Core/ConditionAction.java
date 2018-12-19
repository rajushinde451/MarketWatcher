package com.market.Core;

import com.market.Core.LocalConstants.ConditionTypes;
import com.market.Core.LocalConstants.Priority;
import com.market.Core.LocalConstants.TradeAction;

public class ConditionAction
{
	public ConditionAction(ConditionTypes condType, boolean ispass, TradeAction action, Priority prio) {
		// TODO Auto-generated constructor stub
		conditionType = condType;
		isPassed=ispass;
		tradeAction = action;
		priority =prio;
	}
	
	public ConditionTypes conditionType;
	public boolean isPassed;
	public TradeAction tradeAction;
	public Priority priority;
}