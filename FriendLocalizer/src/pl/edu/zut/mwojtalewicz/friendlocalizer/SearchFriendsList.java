package pl.edu.zut.mwojtalewicz.friendlocalizer;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.JSONParser;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.SearchFriendsItem;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.SearchFriendsListViewAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class SearchFriendsList extends Activity {
	
	private String object;
	private ArrayList<SearchFriendsItem> list;
	private JSONParser jParser;
	private SearchFriendsListViewAdapter searchAdapter;
	
	public ListView searchFriendsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends_list);
		
		searchFriendsListView = (ListView)findViewById(R.id.searchFriendsListView);
		
		
		Intent intent = getIntent();
		object = intent.getStringExtra("jsonobject");
		jParser = new JSONParser();
		
		try {
			JSONObject json = new JSONObject(object);
			list = new ArrayList<SearchFriendsItem>();
			list = jParser.searchFriendsJSONParser(json);
			Log.d("Michazzz", json.toString());
			searchAdapter = new SearchFriendsListViewAdapter(this, list);
			searchFriendsListView.setAdapter(searchAdapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
