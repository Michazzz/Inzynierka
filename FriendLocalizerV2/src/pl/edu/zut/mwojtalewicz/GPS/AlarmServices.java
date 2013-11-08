package pl.edu.zut.mwojtalewicz.GPS;

import java.util.HashMap;
import java.util.TimerTask;

import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class AlarmServices extends TimerTask {

    private Context context;
    private Handler mHandler = new Handler();
    GPSStatus gps;

    public AlarmServices(Context con) {
        this.context = con;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    	gps = new GPSStatus(context);
    	new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    	try{
                    	gps.getLocation();
                    	
                    	Log.d(Constans.Tag, "Longitude: " + gps.getLongitude() + "\nLatitude: " + gps.getLatitude());
                    	
                    	if(gps != null)
                    		sendGpsPosition(gps.getLongitude(), gps.getLatitude());
                    	
                    	Intent intent = new Intent();
                    	intent.putExtra("latitude",""+gps.getLatitude());
                    	intent.putExtra("longitude",""+gps.getLongitude());
                    	intent.setAction("pl.edu.zut.mwojtalewicz.LonLat");
                    	context.sendBroadcast(intent); 
                    	} catch (Exception e) {
                    		e.printStackTrace();
                    	}
                    }
                });
            }
        }).start();
    }
    
    public void sendGpsPosition(Double longitude, Double latitude)
    {
		ConnectionDetector cd = new ConnectionDetector(context); 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		if(isInternetPresent)
		{
			DataBaseHandler db = new DataBaseHandler(context);
			HashMap<String, String> userDetails = db.getUserDetails();
			UserFunctions usr = new UserFunctions();
			String uniqueID = userDetails.get("uid");
			if(uniqueID != null)
			{
				JSONObject json = usr.sendUserGpsLocation(uniqueID, ""+longitude, ""+latitude);
			
				if(json == null)
				{
				
				}
			}
		}
    }
}
