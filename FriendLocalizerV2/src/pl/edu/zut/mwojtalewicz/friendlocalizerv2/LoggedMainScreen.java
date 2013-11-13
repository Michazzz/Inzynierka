package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.TabsPagerAdapter;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class LoggedMainScreen extends android.support.v4.app.FragmentActivity implements ActionBar.TabListener, LocationListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	private MyLocationIntrface mCallback;
	
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 200;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 3;
    
    protected LocationManager locationManager;
    
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
	
	private String[] tabs = { "Profil", "Znajomi", "Mapa" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_main_screen);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        if (!isGPSEnabled && !isNetworkEnabled) {
        	showSettingsAlert();
        }else{
        	if(isNetworkEnabled && !isGPSEnabled){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d(Constans.Tag, "Network");
                if (locationManager != null) {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
        	}
        	else {	
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d(Constans.Tag, "GPS");
                if (locationManager != null) {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
        	}
        }
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	            
	        case R.id.action_search_friends:
				Intent search = new Intent(getApplicationContext(), SearchFriendsActivity.class);
				search.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(search);
	        	return true;
	            
	        case R.id.action_logout:
				UserFunctions usr = new UserFunctions();
				usr.logoutUser(getApplicationContext());
				Intent mainintent = new Intent(getApplicationContext(), MainActivity.class);
				mainintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mainintent);
	        	finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		try{
			mCallback = (MyLocationIntrface) fragment;
		} catch (Exception e){
			e.getMessage();
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	    if (!isGPSEnabled && !isNetworkEnabled) {
	        	showSettingsAlert();
	     }else{
	        	if(isNetworkEnabled && !isGPSEnabled){
	                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                Log.d(Constans.Tag, "Network");
	                if (locationManager != null) {
	                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                }
	        	}
	        	else {	
	                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                Log.d(Constans.Tag, "GPS");
	                if (locationManager != null) {
	                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                }
	        	}
	        }
	}
	
	public interface MyLocationIntrface {
		public void onGPSChange(Location location);
		public void onNetworkChange(Location location);
	}

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}

	public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Ustawienia GPS");
        alertDialog.setMessage("GPS wyłączony, przejść do ustawień i włączyć?");
        alertDialog.setPositiveButton("Ustawienia", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });
  
        alertDialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    
    @Override
    public void onLocationChanged(Location location) {
    	Log.d(Constans.Tag, location.toString());
    	if(location != null){
	    	if(isGPSEnabled){
	    		mCallback.onGPSChange(location);
	    	} else {
	    		mCallback.onNetworkChange(location);
	    	}
    	}
    }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}
