package com.market.Core;

import java.util.Date;

import com.zerodhatech.models.Tick;

public class CustomTick {
	
	 public Tick tick ;
     public String symbol ;
     public float last30SecPriceChange ;
     public float last30SecVolumeChange ;
     public float lastOneMinPriceChange ;
     public float lastOneMinVolumeChange ;
     public float lastTwoMinPriceChange ;
     public float lastTwoMinVolumeChange ;
     public float lastFiveMinPriceChange ;
     public float lastFiveMinVolumeChange ;
     public Date time ;
     public Date last30secTime;
     public Date last1MinTime ;
     public Date last2MinTime ;
     public Date last5MinTime ;
	public Tick getTick() {
		return tick;
	}
	public void setTick(Tick tick) {
		this.tick = tick;
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getLast30secTime() {
		return last30secTime;
	}
	public void setLast30secTime(Date last30secTime) {
		this.last30secTime = last30secTime;
	}
	public Date getLast1MinTime() {
		return last1MinTime;
	}
	public void setLast1MinTime(Date last1MinTime) {
		this.last1MinTime = last1MinTime;
	}
	public Date getLast2MinTime() {
		return last2MinTime;
	}
	public void setLast2MinTime(Date last2MinTime) {
		this.last2MinTime = last2MinTime;
	}
	public Date getLast5MinTime() {
		return last5MinTime;
	}
	public void setLast5MinTime(Date last5MinTime) {
		this.last5MinTime = last5MinTime;
	}
     
     
}
