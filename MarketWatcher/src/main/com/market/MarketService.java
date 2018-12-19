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
import com.market.Core.ConditionInput;
import com.market.Core.DisplayObject;
import com.market.Core.LoginRequest;
import com.market.Core.LoginResponse;
import com.market.Indicator.Condition;
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
	   @Path("/health") 
	   @Produces(MediaType.APPLICATION_JSON) 
	   public String getUsers(){ 
		 //tickManager.createLogFiles();
	      return "I am running fine!!! yupieeee"; 
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
	   @Path("/UpdateCondition")
	   @Consumes(MediaType.APPLICATION_JSON)
	   public void UpdateCondition(ConditionInput conditionInput){		
			try{
				Condition condition = Condition.getInstance();
				condition.UpdateCondition(conditionInput);
			}
			catch (JSONException e) {
			     e.printStackTrace();
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
	
	@GET
	   @Path("/Subscribe")
	   public void SubscribeSecurities(){		
		
		ArrayList<Long> tokens = new ArrayList<>();
		tokens.add(Long.parseLong("492033"));
		tokens.add(Long.parseLong("424961"));
		tokens.add(Long.parseLong("1207553"));
		tokens.add(Long.parseLong("60417"));
		tokens.add(Long.parseLong("70401"));
		tokens.add(Long.parseLong("81153"));
		tokens.add(Long.parseLong("2714625"));
		tokens.add(Long.parseLong("340481"));
		tokens.add(Long.parseLong("348929"));
		tokens.add(Long.parseLong("2672641"));
		tokens.add(Long.parseLong("969473"));
		tokens.add(Long.parseLong("3050241"));
		tokens.add(Long.parseLong("2952193"));
		tokens.add(Long.parseLong("779521"));
		tokens.add(Long.parseLong("2977281"));
		tokens.add(Long.parseLong("519937"));
		tokens.add(Long.parseLong("2939649"));
		tokens.add(Long.parseLong("7712001"));
		tokens.add(Long.parseLong("345089"));
		tokens.add(Long.parseLong("738561"));
		tokens.add(Long.parseLong("895745"));
		tokens.add(Long.parseLong("408065"));
		tokens.add(Long.parseLong("356865"));
		tokens.add(Long.parseLong("1850625"));
		tokens.add(Long.parseLong("3834113"));
		tokens.add(Long.parseLong("177665"));
		tokens.add(Long.parseLong("359937"));
		tokens.add(Long.parseLong("1346049"));
		tokens.add(Long.parseLong("3861249"));
		tokens.add(Long.parseLong("5215745"));
		tokens.add(Long.parseLong("857857"));
		tokens.add(Long.parseLong("2953217"));
		tokens.add(Long.parseLong("2889473"));
		tokens.add(Long.parseLong("415745"));
		tokens.add(Long.parseLong("341249"));
		tokens.add(Long.parseLong("1510401"));
		tokens.add(Long.parseLong("232961"));
		tokens.add(Long.parseLong("784129"));
		tokens.add(Long.parseLong("1270529"));
		tokens.add(Long.parseLong("225537"));
		tokens.add(Long.parseLong("4267265"));
		tokens.add(Long.parseLong("2815745"));
		tokens.add(Long.parseLong("884737"));
		tokens.add(Long.parseLong("975873"));
		tokens.add(Long.parseLong("3465729"));
		tokens.add(Long.parseLong("325121"));
		tokens.add(Long.parseLong("134657"));
		tokens.add(Long.parseLong("633601"));
		tokens.add(Long.parseLong("558337"));
		tokens.add(Long.parseLong("7458561"));

        
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
	   public List<DisplayObject>  GetPotentialStocks(){		
		
		System.out.println("Getting potential stocks ========= Thread Name :"+Thread.currentThread().getName());
		System.out.println("Getting potential stocks");
		List<DisplayObject> listOfDisplayObjects =  tickManager.getListOfDisplayItems();
     
		return listOfDisplayObjects;
		}
}
