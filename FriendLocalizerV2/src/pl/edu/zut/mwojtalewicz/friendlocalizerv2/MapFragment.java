package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

/*
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
*/
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	/*
	private GoogleMap googleMap;
	private SupportMapFragment mMapFragment;
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    View v = inflater.inflate(R.layout.map_fragment, container, false);

	    setUpMapIfNeeded(v);
	    return v;
		
	}
 
	private void setUpMapIfNeeded(View v) {
	    /*
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
	    */
	}
	/*
	public void onDestroyView ()
	{
	      try{
	    SupportMapFragment fragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	        ft.remove(fragment);
	        ft.commit();
	      }catch(Exception e){
	      }
	    super.onDestroyView();  
	}
	*/
}
