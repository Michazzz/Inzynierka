package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.Timer;

import pl.edu.zut.mwojtalewicz.GPS.AlarmServices;
import pl.edu.zut.mwojtalewicz.GPS.GPSStatus;
import pl.edu.zut.mwojtalewicz.Library.TabsPagerAdapter;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class LoggedMainScreen extends android.support.v4.app.FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	private AlarmServices updateProfile;
	private Timer timer;
	GPSStatus gps;
	
	Double longitude, latitude;
	
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

		gps = new GPSStatus(this);
		try
		{
			gps.getLocation();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(gps.canGetLocation())
		{
	        timer  = new Timer();
	        updateProfile = new AlarmServices(this);
	        timer.scheduleAtFixedRate(updateProfile, 0, 180000); 
		}
		else
			gps.showSettingsAlert();
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
				gps.stopUsingGPS();
				timer.cancel();
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
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
