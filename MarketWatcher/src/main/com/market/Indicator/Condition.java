package com.market.Indicator;

import com.market.Core.ConditionInput;

public class Condition {

	float target;
	float stoploss;
	boolean qtyCheck;
	int last30secChangePrice;
	int last1minChangePrice;
	int last2minChangePrice;
	int last5minChangePrice;
	int last30secChangeVol;
	int last1minChangeVol;
	int last2minChangeVol;
	int last5minChangeVol;
	
	// static variable single_instance of type Singleton 
    private static Condition _condition = null; 
  
    // private constructor restricted to this class itself 
    private Condition() 
    { 
		target=(float) 0.30;
		stoploss=(float) 0.25;
		qtyCheck = true;
		last30secChangeVol =200;
		last1minChangeVol=200;
		last2minChangeVol=200;
		last5minChangeVol=200;
		last30secChangePrice=220;
		last1minChangePrice=200;
		last2minChangePrice=200;
		last5minChangePrice=200;
    } 
  
    // static method to create instance of Singleton class 
    public static Condition getInstance() 
    { 
        if (_condition == null) 
        	_condition = new Condition(); 
  
        return _condition; 
    } 
    
    public void UpdateCondition(ConditionInput conditionInput) 
    {
    	_condition.target = conditionInput.target;
    	_condition.stoploss = conditionInput.stoploss;
    	_condition.qtyCheck = conditionInput.qtyCheck;
    	_condition.last30secChangePrice = conditionInput.last30secChangePrice;
    	_condition.last30secChangeVol = conditionInput.last30secChangeVol;
    	_condition.last1minChangePrice = conditionInput.last1minChangePrice;
    	_condition.last1minChangeVol = conditionInput.last1minChangeVol;
    	_condition.last2minChangePrice = conditionInput.last2minChangePrice;
    	_condition.last2minChangeVol = conditionInput.last2minChangeVol;
    	_condition.last5minChangePrice = conditionInput.last5minChangePrice;
    	_condition.last5minChangeVol = conditionInput.last5minChangeVol;
    }
}
