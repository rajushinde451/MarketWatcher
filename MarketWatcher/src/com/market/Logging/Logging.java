package com.market.Logging;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.market.Business.CacheManager;
import com.market.Core.CustomTick;
import com.zerodhatech.models.Tick;

public class Logging {

	FileWriter tickerWriter = null;
	FileWriter trendWriter = null;
	
	public Logging() 
	{
		//createLogFiles();
	}

	public  void writeIntoTrendLog(CustomTick customTick) {
		try {
			synchronized(this) {
				System.out.println("Logging Thread name is:"+Thread.currentThread().getName());
				 //Date date = new Date();  
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
				 String strDate= sdf.format(customTick.getTime()); 
			 
				trendWriter.append(customTick.symbol);
		        trendWriter.append(',');
		        trendWriter.append(strDate);
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.last30SecPriceChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.last30SecVolumeChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.lastOneMinPriceChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.lastOneMinVolumeChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.lastTwoMinPriceChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.lastTwoMinVolumeChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.lastFiveMinPriceChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.lastFiveMinVolumeChange));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getLastTradedPrice()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getChange()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getOpenPrice()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getClosePrice()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getHighPrice()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getLowPrice()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getTotalBuyQuantity()));
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.tick.getTotalSellQuantity()));
		        trendWriter.append(',');
		       
		        if (customTick.currentState == null) customTick.currentState="";
		        
		        trendWriter.append(String.valueOf(customTick.currentState));
		        trendWriter.append(',');
		        trendWriter.append(customTick.isSwitched?"TRUE":"FALSE");
		        trendWriter.append(',');
		        trendWriter.append(String.valueOf(customTick.qtySinceChanged));
		        
		        trendWriter.append('\n');
		        
		        trendWriter.flush();
		        }
		}
		catch (IOException e) {
            e.printStackTrace();
         }
		
	}
	
	public void writeIntoTickerLog(Tick tick) {
		try {
			tickerWriter.append(GetSymbol(tick.getInstrumentToken()));
	        tickerWriter.append(',');
	        tickerWriter.append(tick.getTickTimestamp().toString());
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getLastTradedPrice()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getChange()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getOpenPrice()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getClosePrice()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getHighPrice()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getLowPrice()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getTotalBuyQuantity()));
	        tickerWriter.append(',');
	        tickerWriter.append(String.valueOf(tick.getTotalSellQuantity()));
	        tickerWriter.append('\n');
	        
	        tickerWriter.flush();
		}
		catch (IOException e) {
            e.printStackTrace();
         }
	}
	
	
	public void createLogFiles() 
	{
		String tickerFilePath = getFilePath("ticker");
        String trendFilePath = getFilePath("trend");
        
        try 
        {
	        tickerWriter = new FileWriter(tickerFilePath);
	        trendWriter = new FileWriter(trendFilePath);
	        
	        tickerWriter.append("Security");
	        tickerWriter.append(',');
	        tickerWriter.append("TimeStamp");
	        tickerWriter.append(',');
	        tickerWriter.append("LastTradePrice");
	        tickerWriter.append(',');
	        tickerWriter.append("Change%");
	        tickerWriter.append(',');
	        tickerWriter.append("Open Price");
	        tickerWriter.append(',');
	        tickerWriter.append("Close Price");
	        tickerWriter.append(',');
	        tickerWriter.append("High Price");
	        tickerWriter.append(',');
	        tickerWriter.append("Low Price");
	        tickerWriter.append(',');
	        tickerWriter.append("Total Buy Qty");
	        tickerWriter.append(',');
	        tickerWriter.append("Total Sell Qty");
	        tickerWriter.append('\n');
	        
	        tickerWriter.flush();
	        
	        trendWriter.append("Security");
	        trendWriter.append(',');
	        trendWriter.append("TimeStamp");
	        trendWriter.append(',');
	        trendWriter.append("last30SecPriceChange");
	        trendWriter.append(',');
	        trendWriter.append("last30SecVolumeChange");
	        trendWriter.append(',');
	        trendWriter.append("lastOneMinPriceChange");
	        trendWriter.append(',');
	        trendWriter.append("lastOneMinVolumeChange");
	        trendWriter.append(',');
	        trendWriter.append("lastTwoMinPriceChange");
	        trendWriter.append(',');
	        trendWriter.append("lastTwoMinVolumeChange");
	        trendWriter.append(',');
	        trendWriter.append("lastFiveMinPriceChange");
	        trendWriter.append(',');
	        trendWriter.append("lastFiveMinVolumeChange");
	        trendWriter.append(',');
	        trendWriter.append("LastTradePrice");
	        trendWriter.append(',');
	        trendWriter.append("Change%");
	        trendWriter.append(',');
	        trendWriter.append("Open Price");
	        trendWriter.append(',');
	        trendWriter.append("Close Price");
	        trendWriter.append(',');
	        trendWriter.append("High Price");
	        trendWriter.append(',');
	        trendWriter.append("Low Price");
	        trendWriter.append(',');
	        trendWriter.append("Total Buy Qty");
	        trendWriter.append(',');
	        trendWriter.append("Total Sell Qty");
	        trendWriter.append(',');
	        trendWriter.append("Current State");
	        trendWriter.append(',');
	        trendWriter.append("Is Switched");
	        trendWriter.append(',');
	        trendWriter.append("Qty Since Changed");
	        trendWriter.append('\n');
	        
	        trendWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
         } /*finally {
               try {
            	   tickerWriter.flush();
            	   tickerWriter.close();
            	   trendWriter.flush();
            	   trendWriter.close();
               } catch (IOException e) {
             e.printStackTrace();
       }*/
              // }
	}
	
	public String getFilePath(String filetype) 
	{
		Date date = new Date();  
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
		 String strDate= sdf.format(date); 
		 return "D:\\"+filetype+"_"+strDate+".csv";
	}
	
	private String GetSymbol(long instrumentToken) {
		return CacheManager.GetInstance().GetSecurityName(instrumentToken);
	}
	
	
}
