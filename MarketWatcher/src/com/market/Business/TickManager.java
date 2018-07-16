package com.market.Business;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.market.Core.CustomTick;
import com.market.Core.DisplayObject;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnError;
import com.zerodhatech.ticker.OnOrderUpdate;
import com.zerodhatech.ticker.OnTicks;
import java.util.concurrent.*;

public class TickManager {

	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
	public ConcurrentHashMap<String, TreeMap<Date, CustomTick>> symbolTickMasterList = new ConcurrentHashMap<String, TreeMap<Date, CustomTick>>();
	public ArrayList<DisplayObject> listOfDisplayItems = new ArrayList<DisplayObject>();
	
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
                	AddTick(ticks.get(0));
                    /*System.out.println("last price "+ticks.get(0).getLastTradedPrice());
                    System.out.println("open interest "+formatter.format(ticks.get(0).getOi()));
                    System.out.println("day high OI "+formatter.format(ticks.get(0).getOpenInterestDayHigh()));
                    System.out.println("day low OI "+formatter.format(ticks.get(0).getOpenInterestDayLow()));
                    System.out.println("change "+formatter.format(ticks.get(0).getChange()));
                    System.out.println("tick timestamp "+ticks.get(0).getTickTimestamp());
                    System.out.println("tick timestamp date "+ticks.get(0).getTickTimestamp());
                    System.out.println("last traded time "+ticks.get(0).getLastTradedTime());
                    System.out.println(ticks.get(0).getMarketDepth().get("buy").size());*/
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
        tickerProvider.unsubscribe(tokens);

        // After using com.zerodhatech.com.zerodhatech.ticker, close websocket connection.
        tickerProvider.disconnect();
    }
    
    public void AddTick(Tick tick)
    {
    	CustomTick customTick = new CustomTick();
        customTick.symbol = GetSymbol(tick.getInstrumentToken());
        customTick.tick = tick;

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();

        customTick.time = new Date(t);
        customTick.last30secTime = new Date(t - ONE_MINUTE_IN_MILLIS/2);//customTick.time.AddSeconds(-30);
        customTick.last1MinTime = new Date(t - ONE_MINUTE_IN_MILLIS);//customTick.time.AddMinutes(-1);
        customTick.last2MinTime = new Date(t - (2*ONE_MINUTE_IN_MILLIS));//customTick.time.AddMinutes(-2);
        customTick.last5MinTime = new Date(t - (5*ONE_MINUTE_IN_MILLIS));//customTick.time.AddMinutes(-5);
        
        TreeMap<Date, CustomTick> sortedCustomTickList;
        
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
        
        ArrayList<Date> listOfTimes = new ArrayList<Date>(sortedCustomTickList.keySet());
        
        UpdateLast30SecPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast30SecVolChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast1MinPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast1MinVolChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast2MinPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast2MinVolChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast5MinPriceChange(customTick, sortedCustomTickList, listOfTimes);
        UpdateLast5MinVolChange(customTick, sortedCustomTickList, listOfTimes);
        
        Boolean isPresent = false;
        for (DisplayObject item : listOfDisplayItems) 
        {
            if (item.symbol.equals(customTick.symbol)) 
            {
                item.last30SecPriceChange = customTick.last30SecPriceChange;
                item.last30SecVolumeChange = customTick.last30SecVolumeChange;
                item.lastOneMinPriceChange = customTick.lastOneMinPriceChange;
                item.lastOneMinVolumeChange = customTick.lastOneMinVolumeChange;
                item.lastTwoMinPriceChange = customTick.lastTwoMinPriceChange;
                item.lastTwoMinVolumeChange = customTick.lastTwoMinVolumeChange;
                item.lastFiveMinPriceChange = customTick.lastFiveMinPriceChange;
                item.lastFiveMinVolumeChange = customTick.lastFiveMinVolumeChange;

                isPresent = true;
                break;
            }
        }

        if (!isPresent) 
        {
        	DisplayObject item = new DisplayObject(customTick);
            listOfDisplayItems.add(item);
        }
    }

	private void UpdateLast5MinVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastFiveMinVolumeChange = GetTheVolumePercentage(customTick, customTick.last5MinTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast5MinPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastFiveMinPriceChange = GetThePricePercentage(customTick, customTick.last5MinTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast2MinVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastTwoMinVolumeChange = GetTheVolumePercentage(customTick, customTick.last2MinTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast2MinPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastTwoMinPriceChange = GetThePricePercentage(customTick, customTick.last2MinTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast1MinVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastOneMinVolumeChange = GetTheVolumePercentage(customTick, customTick.last1MinTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast1MinPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.lastOneMinPriceChange = GetThePricePercentage(customTick, customTick.last1MinTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast30SecVolChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.last30SecVolumeChange = GetTheVolumePercentage(customTick, customTick.last30secTime, sortedCustomTickList, listOfTimes);
		
	}

	private void UpdateLast30SecPriceChange(CustomTick customTick, TreeMap<Date, CustomTick> sortedCustomTickList,
			ArrayList<Date> listOfTimes) {
		customTick.last30SecPriceChange = GetThePricePercentage(customTick, customTick.last30secTime, sortedCustomTickList, listOfTimes);
		
	}

	private float GetTheVolumePercentage(CustomTick customTick, Date prevTime,
			TreeMap<Date, CustomTick> sortedCustomTickList, ArrayList<Date> listOfTimes) {
		double prcntChange = 0;
		CustomTick lastTick = new CustomTick();
		for(Map.Entry<Date,CustomTick> entry : sortedCustomTickList.entrySet()) {
			  Date dateKey = entry.getKey();
			  CustomTick value = entry.getValue();

			  CustomTick backTick = new CustomTick();
			  if(dateKey.compareTo(prevTime) > 0 )
			  {
				  lastTick=backTick;
				  break;
			  }
			  else if (dateKey.compareTo(prevTime) == 0 )
			  {
				  lastTick=value;
				  break;
			  }
			  else
			  {
				  backTick = value;
			  }
			}

		prcntChange = (customTick.tick.getVolumeTradedToday() * 100) / lastTick.tick.getVolumeTradedToday();
		return (float)prcntChange;
	}
	
	private float GetThePricePercentage(CustomTick customTick, Date prevTime,
			TreeMap<Date, CustomTick> sortedCustomTickList, ArrayList<Date> listOfTimes) {
		double prcntChange = 0;
		CustomTick lastTick = new CustomTick();
		for(Map.Entry<Date,CustomTick> entry : sortedCustomTickList.entrySet()) {
			  Date dateKey = entry.getKey();
			  CustomTick value = entry.getValue();

			  CustomTick backTick = new CustomTick();
			  if(dateKey.compareTo(prevTime) > 0 )
			  {
				  lastTick=backTick;
				  break;
			  }
			  else if (dateKey.compareTo(prevTime) == 0 )
			  {
				  lastTick=value;
				  break;
			  }
			  else
			  {
				  backTick = value;
			  }
			}

        prcntChange = (customTick.tick.getLastTradedPrice() * 100) / lastTick.tick.getLastTradedPrice();

        if (customTick.tick.getLastTradedPrice() >= lastTick.tick.getLastTradedPrice())
        {
            return (float)prcntChange;
        }
        else
        {
            return (float)(-1 * prcntChange);
        }
	}

	private String GetSymbol(long instrumentToken) {
		return CacheManager.GetInstance().GetSecurityName(instrumentToken);
	}
}
