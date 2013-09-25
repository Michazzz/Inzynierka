package pl.edu.zut.mwojtalewicz.friendlocalizer;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Constans;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.os.Bundle;
import android.util.Log;
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
	private EditText email;
	private EditText name;
	private EditText lastname;
	private TextView tvEmail;
	private TextView tvName;
	private TextView tvLastname;
	private Button searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		
		searchBtn = (Button) findViewById(R.id.searchBtn);
		
		name = (EditText)findViewById(R.id.searchName);
		lastname = (EditText)findViewById(R.id.searchLastname);
		email = (EditText)findViewById(R.id.searchEmail);
		tvEmail = (TextView)findViewById(R.id.tvSearchEmail);
		tvName = (TextView)findViewById(R.id.tvSearchName);
		tvLastname = (TextView)findViewById(R.id.tvSearchLastname);
		
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
					String addEmail = email.getText().toString();
					JSONObject json = userFunction.searchFriends(addEmail);
					
					try {
						if (json.getString(Constans.KEY_SUCCESS) != null) {
							String res = json.getString(Constans.KEY_SUCCESS); 
							if(Integer.parseInt(res) == 1){
								int userNum = Integer.parseInt(json.getString("usersNumber"));
								JSONObject jsonUser;
								for(int i = 0; i < userNum; i++){
									jsonUser = json.getJSONObject(""+i);
									Log.d("LOL", jsonUser.toString());
								}
							}else{
								
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				else
				{
					String usrName = name.getText().toString();
					String usrLastname = lastname.getText().toString();
					
					JSONObject json = userFunction.searchFriends(usrName, usrLastname);
					try {
						if (json.getString(Constans.KEY_SUCCESS) != null) {
							String res = json.getString(Constans.KEY_SUCCESS); 
							if(Integer.parseInt(res) == 1){
								int userNum = Integer.parseInt(json.getString("usersNumber"));
								JSONObject jsonUser;
								for(int i = 0; i < userNum; i++){
									jsonUser = json.getJSONObject(""+i);
									Log.d("LOL", jsonUser.toString());
								}					
							}else{
								
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
			tvEmail.setEnabled(false);
			email.setEnabled(false);
			tvEmail.setTextColor(Color.rgb(127,127,127));
			email.setVisibility(View.GONE);
			tvEmail.setVisibility(View.GONE);
			
			tvName.setTextColor(Color.WHITE);
			tvLastname.setTextColor(Color.WHITE);
			name.setEnabled(true);
			lastname.setEnabled(true);
		}
		else
		{
			tvEmail.setEnabled(true);
			email.setEnabled(true);
			tvEmail.setTextColor(Color.WHITE);
			email.setVisibility(View.VISIBLE);
			tvEmail.setVisibility(View.VISIBLE);
			
			tvName.setTextColor(Color.rgb(127,127,127));
			tvLastname.setTextColor(Color.rgb(127,127,127));
			name.setEnabled(false);
			lastname.setEnabled(false);
		}
	}
}
