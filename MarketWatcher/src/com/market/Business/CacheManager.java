package com.market.Business;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {

	List<Instrument> instruments = new ArrayList<Instrument>();
	
	String subscribeInstruments ="";

    private static CacheManager instance;

    private CacheManager() 
    {
        
    }

    public static CacheManager GetInstance()
    {
    	if (instance == null)
        {
            instance = new CacheManager();
        }
        return instance;
    }
    
    public void LoadInstruments(KiteConnect kiteConnect) throws KiteException, IOException
    {
    	//List<Instrument> instruments = kiteConnect.getInstruments();
        instruments = kiteConnect.getInstruments();
    }
    
    public String GetSecurityName(long instrumentToken) 
    {
    	String securityName = "";

        for (Instrument instrument : instruments) 
        {
            if (instrument.getInstrument_token() == instrumentToken) 
            {
                securityName= instrument.getTradingsymbol();
                break;
            }
        }

        return securityName;
    }
    
    public long GetInstrumentToken(String symbol) 
    {
    	long instrumentToken =0;
        
        for (Instrument instrument : instruments)
        {
            if (instrument.getTradingsymbol().equals(symbol)) 
            {
                instrumentToken = instrument.getInstrument_token();
                break;
            }
        }

        return instrumentToken;
    }
}
