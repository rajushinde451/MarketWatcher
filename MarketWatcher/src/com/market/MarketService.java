package com.market;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 
import javax.ws.rs.GET; 
import javax.ws.rs.POST;
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.market.Business.CacheManager;
import com.market.Business.TickManager;
import com.market.Core.DisplayObject;
import com.market.Core.LoginRequest;
import com.market.Core.LoginResponse;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;


@Path("/MarketService") 
public class MarketService {
	public static KiteConnect kiteConnect    ;
	public static String apiSecret    ;
	TickManager tickManager = new TickManager();
	
	@GET 
	   @Path("/login") 
	   @Produces(MediaType.APPLICATION_JSON) 
	   public String getUsers(){ 
	      return "Raj"; 
	   } 
	
	@GET 
	   @Path("/LoginRedirect") 
	   @Produces(MediaType.APPLICATION_JSON) 
	   public String loginRedirect(@Context UriInfo uriInfo){ 
		try{
			// First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteConnect api call.
      // Initialize KiteSdk with your apiKey.
		      String requestToken = uriInfo.getQueryParameters().get("request_token").toArray()[0].toString();
			User user =  kiteConnect.generateSession(requestToken, apiSecret);
         kiteConnect.setAccessToken(user.accessToken);
         kiteConnect.setPublicToken(user.publicToken);
         CacheManager.GetInstance().LoadInstruments(kiteConnect);
         return "Success";
		}
		catch (KiteException e) {
         System.out.println(e.message+" "+e.code+" "+e.getClass().getName());
         return "Success";
     } catch (JSONException e) {
         e.printStackTrace();
         return "Success";
     }catch (IOException e) {
         e.printStackTrace();
         return "Success";
     }
	   } 
	
	@POST
	   @Path("/GetKiteLoginLink")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public LoginResponse GetLoginLink(LoginRequest request){		
		try{
			// First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteConnect api call.
         // Initialize KiteSdk with your apiKey.
         kiteConnect = new KiteConnect(request.apiKey);

         // Set userId
         kiteConnect.setUserId(request.userId);
         
         apiSecret=request.apiSecret;

         //Enable logs for debugging purpose. This will log request and response.
         kiteConnect.setEnableLogging(true);

         // Get login url
         //String url = "{ url:'"+kiteConnect.getLoginURL()+"'}";
         LoginResponse response = new LoginResponse();
         response.url = kiteConnect.getLoginURL();

         // Set session expiry callback.
         kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
             @Override
             public void sessionExpired() {
                 System.out.println("session expired");
             }
         });

         return response;
         //return Response.ok(url, MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin","*")
           //      .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
		}
	 catch (JSONException e) {
     e.printStackTrace();
     //return "Login failed";
     //return Response.ok("faled", MediaType.APPLICATION_JSON).build();
     return new LoginResponse();
 }
		}
	
	@POST
	   @Path("/Subscribe")
	   @Consumes(MediaType.APPLICATION_JSON)
	   public void SubscribeSecurities(){		
		
		ArrayList<Long> tokens = new ArrayList<>();
        tokens.add(Long.parseLong("265"));
        try {
			tickManager.tickerUsage(kiteConnect, tokens);
		} catch (IOException | WebSocketException | KiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		}
	
	@GET
	   @Path("/GetPotentialStocks")
	   @Produces(MediaType.APPLICATION_JSON)
	   public ArrayList<DisplayObject>  GetPotentialStocks(){		
		
		return tickManager.listOfDisplayItems;
     
		}
}
