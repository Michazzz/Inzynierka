package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.Notification;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import pl.edu.zut.mwojtalewicz.friendlocalizerv2.LoggedMainScreen.MyLocationIntrface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements MyLocationIntrface{

	private TextView tvHelloPerson;
	private TextView tvGpsStatus;
	
	private SharedPreferences mPref;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ScrollView mScrollView = (ScrollView)inflater.inflate(R.layout.profile_fragment, container, false);
		
		tvHelloPerson = (TextView)mScrollView.findViewById(R.id.tvHelloPerson);
		tvGpsStatus = (TextView)mScrollView.findViewById(R.id.tvGPSStatus);
		
		DataBaseHandler db = new DataBaseHandler(getActivity());
    	HashMap<String, String> userDetails = db.getUserDetails();
    	tvHelloPerson.setText(userDetails.get("name") + " " + userDetails.get("lastname"));
    	
    	try{
	    	LatLng lt = loadData();
			tvGpsStatus.setText("Długość: " + lt.longitude + "\n" + 
					"Szerokość: " + lt.latitude + "\n"
					);
    	} catch (Exception e){
    		tvGpsStatus.setText("Oczekuję na dane...");
    	}
    	return mScrollView;
	}
	
	@Override public void setUserVisibleHint(boolean isVisibleToUser) { 
		super.setUserVisibleHint(isVisibleToUser); 
			if (isVisibleToUser) { 
				
			} 
		}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		ConnectionDetector cd = new ConnectionDetector(getActivity()); 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		if(isInternetPresent == true)
		{
			DataBaseHandler db = new DataBaseHandler(getActivity());
			HashMap<String, String> userDetails = db.getUserDetails();
			UserFunctions usr = new UserFunctions();
			String uniqueID = userDetails.get("uid");
			JSONObject json = usr.refreshFriendsList(uniqueID);
			
			if(json == null)
			{
				Notification not = new Notification(getActivity());
				not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", getActivity());
			}
			else
			{	
				try {
					if(json.get(Constans.KEY_SUCCESS) != null)
					{
						String res = json.getString(Constans.KEY_SUCCESS);
						String errRes = json.getString(Constans.KEY_ERROR);
						
						if(Integer.parseInt(res) == 1)
						{
							Intent tokenIntent = new Intent(getActivity(), NewInviteToFriends.class);
							Notification not = new Notification(tokenIntent);
							not.displayOwnNotification(getActivity(), "Nowe zaproszenie!", "Zostałeś zaproszony do znajomych...", json.toString());
						}else
						{
							switch(Integer.parseInt(errRes))
							{
								case 7:
									break;
									
								case 8:
									break;
							}
						}
					}
					else
					{
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			setInternetConnectionAlertDialog("FriendLocalizer", "Brak aktywnego połączenia z internetem. Włączyć?", getActivity());
		}		  
	}	
	
	private void setInternetConnectionAlertDialog(String title, String msg, Context context)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	final  Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                	intent.addCategory(Intent.CATEGORY_LAUNCHER);
                	final ComponentName cn = new ComponentName("com.android.phone","com.android.phone.Settings");
                	intent.setComponent(cn);
                	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                	startActivity(intent);
                }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nie", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish();
			}
		});
        alertDialog.show();
	}

	@Override
	public void onGPSChange(Location location) {
		// TODO Auto-generated method stub
		tvGpsStatus.setText("Długość: " + location.getLongitude() + "\n" + 
							"Szerokość: " + location.getLatitude() + "\n"
							);
		saveData(location);
		try{
			sendGpsPosition(location.getLongitude(), location.getLatitude());
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onNetworkChange(Location location) {
		// TODO Auto-generated method stub
		tvGpsStatus.setText("Długość: " + location.getLongitude() + "\n" + 
				"Szerokość: " + location.getLatitude() + "\n"
				);
		saveData(location);
		try{
			sendGpsPosition(location.getLongitude(), location.getLatitude());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
    public void sendGpsPosition(Double longitude, Double latitude)
    {
		ConnectionDetector cd = new ConnectionDetector(getActivity()); 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		if(isInternetPresent)
		{
			DataBaseHandler db = new DataBaseHandler(getActivity());
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
    
	private void saveData(Location loc)
	{
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

			editor.putString("longitude", ""+loc.getLongitude());
			editor.putString("latitude", ""+loc.getLatitude());
			editor.commit();
	}
	
	public LatLng loadData(){
		mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		double lang = Double.parseDouble(mPref.getString("longitude", null));
		double lat = Double.parseDouble(mPref.getString("latitude", null));
	
		return new LatLng(lat, lang); 
	}
}
