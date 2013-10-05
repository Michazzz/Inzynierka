package pl.edu.zut.mwojtalewicz.friendlocalizer;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Constans;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;


public class SearchFriendsActivity extends Activity implements OnClickListener{
	
	private CheckBox isEmailActivated;
	private EditText name;
	private TextView tvName;
	private Button searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		
		searchBtn = (Button) findViewById(R.id.searchBtn);
		name = (EditText)findViewById(R.id.searchName);
		tvName = (TextView)findViewById(R.id.tvSearchName);
		
		isEmailActivated = (CheckBox)findViewById(R.id.isEmailSearchActivated);
		isEmailActivated.setOnClickListener(this);
		
		searchBtn.setOnClickListener(this);
		
		setViewInSettings(isEmailActivated.isChecked());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.isEmailSearchActivated:
				setViewInSettings(isEmailActivated.isChecked());
				break;
				
			case R.id.searchBtn:
				UserFunctions userFunction = new UserFunctions();
				if(isEmailActivated.isChecked())
				{
					String addEmail = name.getText().toString();
					JSONObject json = userFunction.searchFriends(addEmail);
					
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
				else
				{
					String usrName = name.getText().toString();
					
					JSONObject json = userFunction.searchFriends(usrName, "");
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
				break;
		}
	}
	
	public void setViewInSettings(Boolean state)
	{
		if(!state)
		{		
			tvName.setTextColor(Color.WHITE);
			name.setEnabled(true);
		}
		else
		{		
			tvName.setTextColor(Color.rgb(127,127,127));
			name.setEnabled(false);
		}
	}
}
