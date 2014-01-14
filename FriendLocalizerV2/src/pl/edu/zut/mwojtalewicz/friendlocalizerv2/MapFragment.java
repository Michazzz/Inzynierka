package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.JSONParser;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapFragment extends android.support.v4.app.Fragment {
	private GoogleMap googleMap;
	private SupportMapFragment mMapFragment;
	
    protected LocationManager locationManager;
    
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    
    Polyline polyline;
    
    TextView markerTitle;
    TextView markerDescription;
    
	private SharedPreferences mPref;
	
	private float pinTable[] = {
								BitmapDescriptorFactory.HUE_ROSE,
								BitmapDescriptorFactory.HUE_AZURE,
								BitmapDescriptorFactory.HUE_CYAN,
								BitmapDescriptorFactory.HUE_BLUE,
								BitmapDescriptorFactory.HUE_GREEN,
								BitmapDescriptorFactory.HUE_ORANGE,
								BitmapDescriptorFactory.HUE_MAGENTA,
								BitmapDescriptorFactory.HUE_VIOLET,
								BitmapDescriptorFactory.HUE_RED,
								BitmapDescriptorFactory.HUE_YELLOW
								};
	
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
        	googleMap.getUiSettings().setZoomControlsEnabled(false);
        	googleMap.getUiSettings().setZoomGesturesEnabled(true);
        	googleMap.getUiSettings().setCompassEnabled(true);
        	googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        	
        	try{
        		LatLng lt = loadData();
            	if(lt != null){
            		CameraPosition cameraPosition = new CameraPosition.Builder().target(lt).zoom(14).build();
            		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            	}
        	} catch (Exception e){
        		e.printStackTrace();
        	}
	    } else
	    if (googleMap != null) {
	    		mMapFragment = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map));
	    		googleMap = mMapFragment.getMap();
	        	googleMap.setMyLocationEnabled(true);
	        	googleMap.getUiSettings().setZoomControlsEnabled(false);
	        	googleMap.getUiSettings().setZoomGesturesEnabled(true);
	        	googleMap.getUiSettings().setCompassEnabled(true);
	        	googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	        	try{
	        		LatLng lt = loadData();
	            	if(lt != null){
	            		CameraPosition cameraPosition = new CameraPosition.Builder().target(lt).zoom(14).build();
	            		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	            	}
	        	} catch (Exception e){
	        		e.printStackTrace();
	        	}
	     }
		
		ConnectionDetector cd = new ConnectionDetector(getActivity()); 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		View markerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
		markerTitle = (TextView) markerView.findViewById(R.id.markerTitle);
		markerDescription = (TextView) markerView.findViewById(R.id.markerText);
		
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
				int pin = 0;
				for(int i = 0; i < userNum; i++)
				{
					jsonUser = json.getJSONObject(""+i);
					uniqueID = jsonUser.getString("unique_id");
					name = jsonUser.getString("name");
					lastname = jsonUser.getString("lastname");
					longitude = jsonUser.getString("longitude");
					latitude = jsonUser.getString("latitude");
					updated = jsonUser.getString("updated_at");
					
					markerTitle.setText(name + " " + lastname);
					markerDescription.setText(updated);
					
					MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
															  .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), markerView)));
					//marker.icon(BitmapDescriptorFactory.defaultMarker(pinTable[i]));
					
					googleMap.addMarker(marker).showInfoWindow();
					
					pin++;
					if(pin == 10)
						pin = 0;
				}
			} catch (Exception e) {e.printStackTrace();}
		}
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			
			@Override
			public boolean onMarkerClick(Marker position) {
				String url;
				if(polyline != null)
					polyline.remove();
				try{
					Location loc = googleMap.getMyLocation();
					LatLng lt;
					if(loc != null){
						lt = new LatLng(loc.getLatitude(), loc.getLongitude());
					} 
					else
						lt = loadData();
					
					url = makeURL(lt.latitude, lt.longitude, position.getPosition().latitude, position.getPosition().longitude);
					
					JSONParser jParser = new JSONParser();
					String json = jParser.getJSONFromUrl(url);
					
					drawPath(json, googleMap);
					
				} catch (Exception e){
					e.printStackTrace();
				}
				return false;
			}
		});
		
		googleMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				googleMap.clear();
				setUpMapIfNeeded();
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
		double lang = Double.parseDouble(mPref.getString("longitude", "0"));
		double lat = Double.parseDouble(mPref.getString("latitude", "0"));
	
		return new LatLng(lat, lang); 
	}
	
	 public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
	        StringBuilder urlString = new StringBuilder();
	        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	        urlString.append("?origin=");// from
	        urlString.append(Double.toString(sourcelat));
	        urlString.append(",");
	        urlString
	                .append(Double.toString( sourcelog));
	        urlString.append("&destination=");// to
	        urlString
	                .append(Double.toString( destlat));
	        urlString.append(",");
	        urlString.append(Double.toString( destlog));
	        urlString.append("&sensor=false&mode=driving&alternatives=true");
	        return urlString.toString();
	 }
	 
	 public void drawPath(String  result, GoogleMap mMap) {
		 mMap.clear();
		 setUpMapIfNeeded();
		    try {
		           final JSONObject json = new JSONObject(result);
		           JSONArray routeArray = json.getJSONArray("routes");
		           JSONObject routes = routeArray.getJSONObject(0);
		           JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
		           String encodedString = overviewPolylines.getString("points");
		           List<LatLng> list = decodePoly(encodedString);	           


		           for(int z = 0; z<list.size()-1;z++) {
		                LatLng src= list.get(z);
		                LatLng dest= list.get(z+1);
		                polyline = mMap.addPolyline(new PolylineOptions()
		                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
		                .width(4)
		                .color(Color.RED).geodesic(true));
		            }
		    } 
		    catch (JSONException e) {
		    	
		    }
		}
	 
	 private List<LatLng> decodePoly(String encoded) {

		    List<LatLng> poly = new ArrayList<LatLng>();
		    int index = 0, len = encoded.length();
		    int lat = 0, lng = 0;

		    while (index < len) {
		        int b, shift = 0, result = 0;
		        do {
		            b = encoded.charAt(index++) - 63;
		            result |= (b & 0x1f) << shift;
		            shift += 5;
		        } while (b >= 0x20);
		        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		        lat += dlat;

		        shift = 0;
		        result = 0;
		        do {
		            b = encoded.charAt(index++) - 63;
		            result |= (b & 0x1f) << shift;
		            shift += 5;
		        } while (b >= 0x20);
		        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		        lng += dlng;

		        LatLng p = new LatLng( (((double) lat / 1E5)),
		                 (((double) lng / 1E5) ));
		        poly.add(p);
		    }

		    return poly;
		}
	 
		public static Bitmap createDrawableFromView(Context context, View view) {
			DisplayMetrics displayMetrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
			view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
			view.buildDrawingCache();
			Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
	 
			Canvas canvas = new Canvas(bitmap);
			view.draw(canvas);
	 
			return bitmap;
		}
}
