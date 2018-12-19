package com.market.Core;

public class QuantityIndicator {
	String symbol;
	String currentState;
	Boolean isSwitched;
	double qtySinceChanged;
	
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public Boolean getIsSwitched() {
		return isSwitched;
	}
	public void setIsSwitched(Boolean isSwitched) {
		this.isSwitched = isSwitched;
	}
	public double getQtySinceChanged() {
		return qtySinceChanged;
	}
	public void setQtySinceChanged(double qtySinceChanged) {
		this.qtySinceChanged = qtySinceChanged;
	}
	
	

}
