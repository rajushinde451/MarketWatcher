package com.market.Core;

public class DisplayObject {

	 public String symbol ;
     public float last30SecPriceChange ;
     public float last30SecVolumeChange ;
     public float lastOneMinPriceChange ;
     public float lastOneMinVolumeChange ;
     public float lastTwoMinPriceChange ;
     public float lastTwoMinVolumeChange ;
     public float lastFiveMinPriceChange ;
     public float lastFiveMinVolumeChange ;

     public DisplayObject(CustomTick customTick)
     {
         symbol = customTick.symbol;
         last30SecPriceChange  =customTick.last30SecPriceChange;
         last30SecVolumeChange=customTick.last30SecVolumeChange;
         lastOneMinPriceChange =customTick.lastOneMinPriceChange;
         lastOneMinVolumeChange =customTick.lastOneMinVolumeChange;
         lastTwoMinPriceChange = customTick.lastTwoMinPriceChange;
         lastTwoMinVolumeChange =customTick.lastTwoMinVolumeChange;
         lastFiveMinPriceChange =customTick.lastFiveMinPriceChange;
         lastFiveMinVolumeChange=customTick.lastFiveMinVolumeChange;
     }

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public float getLast30SecPriceChange() {
		return last30SecPriceChange;
	}

	public void setLast30SecPriceChange(float last30SecPriceChange) {
		this.last30SecPriceChange = last30SecPriceChange;
	}

	public float getLast30SecVolumeChange() {
		return last30SecVolumeChange;
	}

	public void setLast30SecVolumeChange(float last30SecVolumeChange) {
		this.last30SecVolumeChange = last30SecVolumeChange;
	}

	public float getLastOneMinPriceChange() {
		return lastOneMinPriceChange;
	}

	public void setLastOneMinPriceChange(float lastOneMinPriceChange) {
		this.lastOneMinPriceChange = lastOneMinPriceChange;
	}

	public float getLastOneMinVolumeChange() {
		return lastOneMinVolumeChange;
	}

	public void setLastOneMinVolumeChange(float lastOneMinVolumeChange) {
		this.lastOneMinVolumeChange = lastOneMinVolumeChange;
	}

	public float getLastTwoMinPriceChange() {
		return lastTwoMinPriceChange;
	}

	public void setLastTwoMinPriceChange(float lastTwoMinPriceChange) {
		this.lastTwoMinPriceChange = lastTwoMinPriceChange;
	}

	public float getLastTwoMinVolumeChange() {
		return lastTwoMinVolumeChange;
	}

	public void setLastTwoMinVolumeChange(float lastTwoMinVolumeChange) {
		this.lastTwoMinVolumeChange = lastTwoMinVolumeChange;
	}

	public float getLastFiveMinPriceChange() {
		return lastFiveMinPriceChange;
	}

	public void setLastFiveMinPriceChange(float lastFiveMinPriceChange) {
		this.lastFiveMinPriceChange = lastFiveMinPriceChange;
	}

	public float getLastFiveMinVolumeChange() {
		return lastFiveMinVolumeChange;
	}

	public void setLastFiveMinVolumeChange(float lastFiveMinVolumeChange) {
		this.lastFiveMinVolumeChange = lastFiveMinVolumeChange;
	}
     
}
