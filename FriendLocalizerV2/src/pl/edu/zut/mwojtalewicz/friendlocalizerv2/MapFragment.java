package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.HashMap;

import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapFragment extends android.support.v4.app.Fragment {
	private GoogleMap googleMap;
	private SupportMapFragment mMapFragment;
	
    protected LocationManager locationManager;
    
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    
    Polygon polyline;
    
	private SharedPreferences mPref;
	
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
        	googleMap.setMyLocationEnabled(true);
        	googleMap.getUiSettings().setZoomControlsEnabled(true);
        	googleMap.getUiSettings().setZoomGesturesEnabled(false);
        	googleMap.getUiSettings().setCompassEnabled(true);
        	googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        	
        	LatLng lt = loadData();
        	if(lt != null){
        		CameraPosition cameraPosition = new CameraPosition.Builder().target(lt).zoom(14).build();
        		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        	}
	    } else
	    if (googleMap != null) {
	    		mMapFragment = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map));
	    		googleMap = mMapFragment.getMap();
	        	googleMap.setMyLocationEnabled(true);
	        	googleMap.getUiSettings().setZoomControlsEnabled(true);
	        	googleMap.getUiSettings().setZoomGesturesEnabled(false);
	        	googleMap.getUiSettings().setCompassEnabled(true);
	        	googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	        	LatLng lt = loadData();
	        	if(lt != null){
	        		CameraPosition cameraPosition = new CameraPosition.Builder().target(lt).zoom(14).build();
	        		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
			
			String name, lastname, longitude, latitude, updated;
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
					updated = jsonUser.getString("updated_at");
					
					MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title(name + " " +lastname).snippet(updated);
					marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
					
					googleMap.addMarker(marker).showInfoWindow();					
				}
			} catch (Exception e) {e.printStackTrace();}
		}
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker position) {
				try{
					
					if(polyline != null)
						polyline.remove();
					Location loc = googleMap.getMyLocation();
					LatLng lt;
					if(loc != null){
						lt = new LatLng(loc.getLatitude(), loc.getLongitude());
					} 
					else
						lt = loadData();

					polyline = googleMap.addPolygon(new PolygonOptions()
				    .add(lt, position.getPosition())
				    .geodesic(true));
					
				} catch (Exception e){
					e.printStackTrace();
				}
				return false;
			}
		});
	}
	
	@Override
	public void onDestroyView (){
	    super.onDestroyView(); 
	    try{
		    SupportMapFragment fragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
		    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		    ft.remove(fragment);
		    ft.commit();
	    } catch (Exception e){
	    	e.getMessage();
	    	getActivity().finish();
	    }
	}

	public LatLng loadData(){
		mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		double lang = Double.parseDouble(mPref.getString("longitude", null));
		double lat = Double.parseDouble(mPref.getString("latitude", null));
	
		return new LatLng(lat, lang); 
	}
}
