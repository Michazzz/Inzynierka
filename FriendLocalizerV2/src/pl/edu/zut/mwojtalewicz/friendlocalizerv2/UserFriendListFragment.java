package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.JSONParser;
import pl.edu.zut.mwojtalewicz.Library.Notification;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsItem;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsListViewAdapter;
import pl.edu.zut.mwojtalewicz.Library.Constans;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserFriendListFragment extends Fragment {

	private ArrayList<SearchFriendsItem> list;
	private JSONParser jParser;
	private SearchFriendsListViewAdapter searchAdapter;
	
	
	public ListView userFriendsListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RelativeLayout mRelativeLayout = (RelativeLayout)inflater.inflate(R.layout.user_friends_list_fragment, container, false);
		
		userFriendsListView = (ListView)mRelativeLayout.findViewById(R.id.userFriendsListView);
		userFriendsListView.setClickable(true);
		userFriendsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				setNewAlertDialog("FriendLocalizer", "Czy chcesz usunąć " + Html.fromHtml(("<b>"+list.get(arg2).getName() + " " + list.get(arg2).getLastname()+"</b>").toString()
						) + " ze znajomych?", getActivity(), arg2);
			}
		});

		return mRelativeLayout;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ConnectionDetector cd = new ConnectionDetector(getActivity()); 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		if(isInternetPresent)
		{
			DataBaseHandler db = new DataBaseHandler(getActivity());
			HashMap<String, String> userDetails = db.getUserDetails();
			UserFunctions usr = new UserFunctions();
			String uniqueID = userDetails.get("uid");
			JSONObject json = usr.getUserFriendList(uniqueID);
			
			if(json == null)
			{
				Notification not = new Notification(getActivity());
				not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", getActivity());
			}
			else
			{
				jParser = new JSONParser();
				
				list = new ArrayList<SearchFriendsItem>();
				list = jParser.newInviteJSONParser(json, getActivity());
				searchAdapter = new SearchFriendsListViewAdapter(getActivity(), list);
				userFriendsListView.setAdapter(searchAdapter);
			}
		}
		else
		{
			Notification not = new Notification(getActivity());
			not.setNewAlertDialog("Błąd", "Wystąpił problem z siecią. Sprawdź połączenie internetowe.", getActivity());
		}
	}	
	
	private void setNewAlertDialog(String title, String msg, Context context, final int pos)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
            		DataBaseHandler db = new DataBaseHandler(getActivity());
            		HashMap<String, String> userDetails = db.getUserDetails();
            		UserFunctions usr = new UserFunctions();
            		String uniqueID = userDetails.get("uid");
            		JSONObject json = usr.removeUserFromFriends(uniqueID, list.get(pos).getUniqueID());
            		try {
    					if(json.getString(Constans.KEY_SUCCESS) != null) {
    						list.remove(pos);
    						searchAdapter = new SearchFriendsListViewAdapter(getActivity(), list);
    						userFriendsListView.setAdapter(searchAdapter);
    						Toast.makeText(getActivity(), "Usunięto pomyślnie!", Toast.LENGTH_SHORT).show();
    						dialog.dismiss();
    					}
                	} catch (JSONException e) {
    						e.printStackTrace();
    				}
                }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nie", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            }
    });
        alertDialog.show();
	}
}
