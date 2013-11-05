package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.Notification;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class SearchFriendsActivity extends Activity implements OnClickListener{
	
	private CheckBox isEmailActivated;
	private EditText name;
	private Button searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		searchBtn = (Button) findViewById(R.id.searchBtn);
		name = (EditText)findViewById(R.id.searchName);
		imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
		
		isEmailActivated = (CheckBox)findViewById(R.id.isEmailSearchActivated);
		isEmailActivated.setOnClickListener(this);
		
		searchBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.searchBtn:
				UserFunctions userFunction = new UserFunctions();
				if(isEmailActivated.isChecked())
				{
						String addEmail = name.getText().toString();
						JSONObject json = userFunction.searchFriends(addEmail);
						if(json == null)
						{
							Notification not = new Notification(this);
							not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", this);
						}
						else
						{
						try {
							if (json.getString(Constans.KEY_SUCCESS) != null) {
								String res = json.getString(Constans.KEY_SUCCESS); 
								if(Integer.parseInt(res) == 1)
								{
									Intent i = new Intent(getApplicationContext(), SearchFriendsList.class);
									i.putExtra("jsonobject", json.toString());
									startActivity(i);
									finish();
								} 
								else
								{
									
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				else
				{
						String usrName = name.getText().toString();
						
						JSONObject json = userFunction.searchFriends(usrName, "");
						if(json == null)
						{
							Notification not = new Notification(this);
							not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", this);
						}
						else
						{
						try {
							if (json.getString(Constans.KEY_SUCCESS) != null) {
								String res = json.getString(Constans.KEY_SUCCESS); 
								if(Integer.parseInt(res) == 1)
								{
									Intent i = new Intent(getApplicationContext(), SearchFriendsList.class);
									i.putExtra("jsonobject", json.toString());
									startActivity(i);
									finish();
								} 
								else
								{
									
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				break;
		}
	}
}
