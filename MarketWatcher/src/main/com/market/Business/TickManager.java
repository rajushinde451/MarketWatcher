package com.market.Business;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.market.Core.CustomTick;
import com.market.Core.DisplayObject;
import com.market.Indicator.IndicatorFinder;
import com.market.IndicatorCalc.QtyIndicatorCalc;
import com.market.Logging.Logging;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import com.zerodhatech.ticker.*;
import java.util.concurrent.*;

import org.json.JSONException;



public class TickManager {

	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	QtyIndicatorCalc qtyIndicatorCalc = new QtyIndicatorCalc();
	IndicatorFinder indicatorFinder = new IndicatorFinder();
	Logging logging = new Logging();
	
	public ConcurrentHashMap<String, TreeMap<Date, CustomTick>> symbolTickMasterList = new ConcurrentHashMap<String, TreeMap<Date, CustomTick>>();
	
    /** Demonstrates com.zerodhatech.ticker connection, subcribing for instruments, unsubscribing for instruments, set mode of tick data, com.zerodhatech.ticker disconnection*/
    public void tickerUsage(KiteConnect kiteConnect, ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        /** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
        final KiteTicker tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());

        tickerProvider.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                /** Subscribe ticks for token.
                 * By default, all tokens are subscribed for modeQuote.
                 * */
                tickerProvider.subscribe(tokens);
                tickerProvider.setMode(tokens, KiteTicker.modeFull);
                
                logging.createLogFiles();
            }
        });

        tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                // your code goes here
            }
        });

        /** Set listener to get order updates.*/
        tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                System.out.println("order update "+order.orderId);
            }
        });

        /** Set error listener to listen to errors.*/
        tickerProvider.setOnErrorListener(new OnError() {
            @Override
            public void onError(Exception exception) {
                //handle here.
            }

            @Override
            public void onError(KiteException kiteException) {
                //handle here.
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                NumberFormat formatter = new DecimalFormat();
                System.out.println("ticks size "+ticks.size());
                if(ticks.size() > 0) {
                	OrderParams orderParams  = AddTick(ticks.get(0));

                	if(orderParams!=null)
						try {
							Order order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
						} catch (JSONException | IOException | KiteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                		//Order s = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
                }
            }
        });
        // Make sure this is called before calling connect.
        tickerProvider.setTryReconnection(true);
        //maximum retries and should be greater than 0
        tickerProvider.setMaximumRetries(10);
        //set maximum retry interval in seconds
        tickerProvider.setMaximumRetryInterval(30);

        /** connects to com.zerodhatech.com.zerodhatech.ticker server for getting live quotes*/
        tickerProvider.connect();

        /** You can check, if websocket connection is open or not using the following method.*/
        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println(isConnected);

        /** set mode is used to set mode in which you need tick for list of tokens.
         * Ticker allows three modes, modeFull, modeQuote, modeLTP.
         * For getting only last traded price, use modeLTP
         * For getting last traded price, last traded quantity, average price, volume traded today, total sell quantity and total buy quantity, open, high, low, close, change, use modeQuote
         * For getting all data with depth, use modeFull*/
        tickerProvider.setMode(tokens, KiteTicker.modeLTP);

        // Unsubscribe for a token.
        //tickerProvider.unsubscribe(tokens);

        // After using com.zerodhatech.com.zerodhatech.ticker, close websocket connection.
        //tickerProvider.disconnect();
    }
    
    public OrderParams AddTick(Tick tick)
    {
    	//System.out.println("Point1");
    	
    	logging.writeIntoTickerLog(tick);
    	
    	CustomTick customTick = new CustomTick();
        customTick.symbol = GetSymbol(tick.getInstrumentToken());
        customTick.tick = tick;
        
        customTick.time = tick.getTickTimestamp();
        long t= tick.getTickTimestamp().getTime();
        
        customTick.last30secTime = new Date(t - ONE_MINUTE_IN_MILLIS/2);//customTick.time.AddSeconds(-30);
        customTick.last1MinTime = new Date(t - ONE_MINUTE_IN_MILLIS);//customTick.time.AddMinutes(-1);
        customTick.last2MinTime = new Date(t - (2*ONE_MINUTE_IN_MILLIS));//customTick.time.AddMinutes(-2);
        customTick.last5MinTime = new Date(t - (5*ONE_MINUTE_IN_MILLIS));//customTick.time.AddMinutes(-5);
        
        TreeMap<Date, CustomTick> sortedCustomTickList;
        
        //System.out.println("Point4");
        
        if (symbolTickMasterList.containsKey(customTick.symbol)) 
        {
            sortedCustomTickList = symbolTickMasterList.get(customTick.symbol);
            sortedCustomTickList.put(customTick.time, customTick);
        }
        else 
        {
            sortedCustomTickList = new TreeMap<Date, CustomTick>();
            sortedCustomTickList.put(customTick.time, customTick);
            symbolTickMasterList.put(customTick.symbol, sortedCustomTickList) ;
        }
        
        ArrayList<Date> listOfTimes = new ArrayList<Date>();

        UpdateLast30SecPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast30SecVolChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast1MinPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast1MinVolChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast2MinPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast2MinVolChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast5MinPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast5MinVolChange(customTick, sortedCustomTickList, listOfTimes);
        
        qtyIndicatorCalc.calculate(customTick);
        
        Boolean isPresent = false;
        List<DisplayObject> list = CacheManager.GetInstance().getListOfDisplayItems();
        for (DisplayObject item : list) 
        {
            if (item.symbol.equals(customTick.symbol)) 
            {
            	System.out.println("Thread name is:"+Thread.currentThread().getName()+" List count is :"+list.size());
            	System.out.println("Inside Loop -- Contains symbol");
            	System.out.println("HashCode :"+this.hashCode()+" String Name"+this.toString());
                item.last30SecPriceChange = customTick.last30SecPriceChange;
                item.last30SecVolumeChange = customTick.last30SecVolumeChange;
                item.lastOneMinPriceChange = customTick.lastOneMinPriceChange;
                item.lastOneMinVolumeChange = customTick.lastOneMinVolumeChange;
                item.lastTwoMinPriceChange = customTick.lastTwoMinPriceChange;
                item.lastTwoMinVolumeChange = customTick.lastTwoMinVolumeChange;
                item.lastFiveMinPriceChange = customTick.lastFiveMinPriceChange;
                item.lastFiveMinVolumeChange = customTick.lastFiveMinVolumeChange;

                isPresent = true;
                
                logging.writeIntoTrendLog(customTick);
                break;
            }
        }

        //setListOfDisplayItems(listOfDisplayItems);
        
        if (!isPresent) 
        {
        	System.out.println("Thread Name :"+Thread.currentThread().getName());
        	DisplayObject item = new DisplayObject(customTick);
        	list.add(item);
        	logging.writeIntoTrendLog(customTick);
        }
        
        // This logic is to catch the trigger point
        return indicatorFinder.findTheTrendAndExecute(customTick);

    }

	public List<DisplayObject> getListOfDisplayItems() {
		List<DisplayObject> list = CacheManager.GetInstance().getListOfDisplayItems();
		System.out.println("Thread name is:"+Thread.currentThread().getName()+" List count is :"+list.size());
		System.out.println("HashCode :"+this.hashCode()+" String Name"+this.toString());
		return list;
	}

	public void setListOfDisplayItems(List<DisplayObject> listOfDisplayItems) {
		//this.listOfDisplayItems = listOfDisplayItems;
	}

	private void UpdateLast5MinVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastFiveMinVolumeChange = GetTheVolumePercentage(customTick, customTick.last5MinTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" lastFiveMinVolumeChange"+ customTick.lastFiveMinVolumeChange);
		
	}

	private void UpdateLast5MinPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastFiveMinPriceChange = GetThePricePercentage(customTick, customTick.last5MinTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" lastFiveMinPriceChange"+ customTick.lastFiveMinPriceChange);
		
	}

	private void UpdateLast2MinVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastTwoMinVolumeChange = GetTheVolumePercentage(customTick, customTick.last2MinTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" lastTwoMinVolumeChange"+ customTick.lastTwoMinVolumeChange);
		
	}

	private void UpdateLast2MinPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastTwoMinPriceChange = GetThePricePercentage(customTick, customTick.last2MinTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" lastTwoMinPriceChange"+ customTick.lastTwoMinPriceChange);
		
	}

	private void UpdateLast1MinVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastOneMinVolumeChange = GetTheVolumePercentage(customTick, customTick.last1MinTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" lastOneMinVolumeChange"+ customTick.lastOneMinVolumeChange);
		
	}

	private void UpdateLast1MinPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastOneMinPriceChange = GetThePricePercentage(customTick, customTick.last1MinTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" lastOneMinPriceChange"+ customTick.lastOneMinPriceChange);
		
	}

	private void UpdateLast30SecVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.last30SecVolumeChange = GetTheVolumePercentage(customTick, customTick.last30secTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" last30SecVolumeChange"+ customTick.last30SecVolumeChange);
		
	}

	private void UpdateLast30SecPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.last30SecPriceChange = GetThePricePercentage(customTick, customTick.last30secTime, sortedCustomTickList, listOfTimes);
		System.out.println("Symbol - "+customTick.symbol +" last30SecPriceChange"+ customTick.last30SecPriceChange);
		
	}

	private float GetTheVolumePercentage(CustomTick customTick, Date prevTime,
			TreeMap<Date, CustomTick> sortedCustomTickList, ArrayList<Date> listOfTimes) {
		double prcntChange = 0;
		CustomTick lastTick = new CustomTick();
		CustomTick backTick = new CustomTick();
		Boolean found=false;
		for(Map.Entry<Date,CustomTick> entry : sortedCustomTickList.entrySet()) {
			//System.out.println("X.1.1.3");
			  Date dateKey = entry.getKey();
			  CustomTick value = entry.getValue();

			 // System.out.println("X.1.1.4");
			  if(dateKey.compareTo(prevTime) > 0 )
			  {
				  //System.out.println("X.1.1.5   Greater");
				  lastTick=backTick;
				  found=true;
				  break;
			  }
			  else if (dateKey.compareTo(prevTime) == 0 )
			  {
				//  System.out.println("X.1.1.5   Same");
				  lastTick=value;
				  found=true;
				  break;
			  }
			  else
			  {
				  //System.out.println("X.1.1.5   Larger");
				  backTick = value;
			  }
			}

		//System.out.println("X.1.1.5 OUTSIDE");
		if(found) 
		{
			if(lastTick.tick!=null) 
			{
			prcntChange = ((customTick.tick.getVolumeTradedToday() * 100) / lastTick.tick.getVolumeTradedToday())-100;
			}
		}
		//System.out.println("Returning value");
		return Math.round((float)prcntChange*1000);
	}
	
	private float GetThePricePercentage(CustomTick customTick, Date prevTime,
			TreeMap<Date, CustomTick> sortedCustomTickList, ArrayList<Date> listOfTimes) {
		//System.out.println("5.1.1.1");
		float prcntChange = 0;
		CustomTick lastTick = new CustomTick();
		//System.out.println("5.1.1.2");
		//System.out.println("Size is :"+sortedCustomTickList.entrySet().size());
		CustomTick backTick=null;
		Boolean found=false;
		for(Map.Entry<Date,CustomTick> entry : sortedCustomTickList.entrySet()) {
			//System.out.println("=====================================================");
			//System.out.println("5.1.1.3");
			  Date dateKey = entry.getKey();
			  CustomTick value = entry.getValue();

			  
			  //System.out.println("dateKey is"+dateKey);
			  //System.out.println("prevTime is"+prevTime);
			  //System.out.println(value.getSymbol()+" "+value.getLast30secTime()+" "+ value.getLast1MinTime()+" "+value.getLast2MinTime()+" "+value.getLast5MinTime());
			  
			  if(dateKey.compareTo(prevTime) > 0 )
			  {
				 // System.out.println("5.1.1.4.FirstIsGreater");
				  if(backTick!=null) 
				  {
					  lastTick=backTick;
					//  System.out.println("Custom Tick trade Price "+ customTick.tick.getLastTradedPrice());
					 // System.out.println("Last Tick trade Price "+ lastTick.tick.getLastTradedPrice());
					  found=true;
					  break;
				  }
				  
			  }
			  else if (dateKey.compareTo(prevTime) == 0 )
			  {
				//  System.out.println("5.1.1.4.BothAreSame");
				  lastTick=value;
				  found=true;
				  break;
			  }
			  else
			  {
				  //System.out.println("5.1.1.4.SecondIsGreater");
				  backTick = value;
			  }
			  //System.out.println("5.1.1.4");
			}
		//System.out.println("5.1.1.4.Afterchange");
		//System.out.println("Custom Tick Last Traded Price "+customTick.tick.getLastTradedPrice() +" Last Tick  Last Traded Price "+lastTick.tick.getLastTradedPrice());

		if(found) 
		{
	        prcntChange = (float)((customTick.tick.getLastTradedPrice() * 100) / lastTick.tick.getLastTradedPrice())-100;
	
	        System.out.println("Symbol "+customTick.getSymbol()+" Current: "+customTick.tick.getLastTradedPrice()+" Last:"+lastTick.tick.getLastTradedPrice());
	        
	        return Math.round(prcntChange*1000);
	        /*if (customTick.tick.getLastTradedPrice() >= lastTick.tick.getLastTradedPrice())
	        {
	            return (float)prcntChange;
	        }
	        else
	        {
	        	System.out.println("NEGETIVE - "+(-1 * prcntChange));
	            return (-1 * prcntChange);
	        }*/
		}
		else 
		{
			return 0;
		}
	}

	private String GetSymbol(long instrumentToken) {
		return CacheManager.GetInstance().GetSecurityName(instrumentToken);
	}
}
