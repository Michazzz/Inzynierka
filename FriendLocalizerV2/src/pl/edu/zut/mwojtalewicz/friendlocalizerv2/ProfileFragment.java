package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.Notification;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import pl.edu.zut.mwojtalewicz.friendlocalizerv2.LoggedMainScreen.MyLocationInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements MyLocationInterface {
	
	private TextView tvHelloPerson;
	private TextView tvGpsStatus;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ScrollView mScrollView = (ScrollView)inflater.inflate(R.layout.profile_fragment, container, false);
		
		tvHelloPerson = (TextView)mScrollView.findViewById(R.id.tvHelloPerson);
		tvGpsStatus = (TextView)mScrollView.findViewById(R.id.tvGPSStatus);
		
		DataBaseHandler db = new DataBaseHandler(getActivity());
    	HashMap<String, String> userDetails = db.getUserDetails();
    	tvHelloPerson.setText(userDetails.get("name") + " " + userDetails.get("lastname"));
    	tvGpsStatus.setText("Oczekuję na dane...");
    	
    	return mScrollView;
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
	public void onGpsChange(double longitude, double latitude) {
		tvGpsStatus.setText("Długość: "+longitude + "\nSzerokość: " + latitude + "\n");
	}
}
