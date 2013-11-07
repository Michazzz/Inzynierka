package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.HashMap;

import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsItem;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends android.support.v4.app.Fragment {
	private GoogleMap googleMap;
	private SupportMapFragment mMapFragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    View v = inflater.inflate(R.layout.map_fragment, container, false);

	    setUpMapIfNeeded();
	    return v;
		
	}
 
	private void setUpMapIfNeeded() 
	{
		
		if (googleMap == null) {
	    	mMapFragment = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map));
	    	googleMap = mMapFragment.getMap();
	        if (googleMap != null) {
	        	googleMap.setMyLocationEnabled(true);
	        	googleMap.getUiSettings().setZoomControlsEnabled(true);
	        	googleMap.getUiSettings().setZoomGesturesEnabled(false);
	        	googleMap.getUiSettings().setCompassEnabled(true);
	        	googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	        }
	    }
		
		ConnectionDetector cd = new ConnectionDetector(getActivity()); 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		if(isInternetPresent == true)
		{
			DataBaseHandler db = new DataBaseHandler(getActivity());
			HashMap<String, String> userDetails = db.getUserDetails();
			UserFunctions usr = new UserFunctions();
			String uniqueID = userDetails.get("uid");
			JSONObject json = usr.showUserFriendsLocatons(uniqueID);
			
			String name, lastname, longitude, latitude;
			try {
				int userNum = Integer.parseInt(json.getString("usersNumber"));
				JSONObject jsonUser;
				for(int i = 0; i < userNum; i++)
				{
					jsonUser = json.getJSONObject(""+i);
					uniqueID = jsonUser.getString("unique_id");
					name = jsonUser.getString("name");
					lastname = jsonUser.getString("lastname");
					longitude = jsonUser.getString("longitude");
					latitude = jsonUser.getString("latitude");
					
					MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title(name + " " +lastname);
					marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
					
					googleMap.addMarker(marker).showInfoWindow();
				}
			} catch (Exception e) {e.printStackTrace();}
			
			
		}	    
	}
	
	@Override
	public void onDestroyView ()
	{
	    super.onDestroyView();  
	      try{
	    	  SupportMapFragment fragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
	    	  FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    	  ft.remove(fragment);
	    	  ft.commit();
	      }catch(Exception e){
	      }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpMapIfNeeded();
	}
}
