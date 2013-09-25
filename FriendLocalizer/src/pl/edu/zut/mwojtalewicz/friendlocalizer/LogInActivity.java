package pl.edu.zut.mwojtalewicz.friendlocalizer;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Constans;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends Activity implements OnClickListener {
	
	private Button btnLogin;
	private Button btnRegister;
	
	private EditText inputEmail;
	private EditText inputPassword;
	private TextView loginErrorMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnRegister = (Button)findViewById(R.id.btnLinkToRegisterScreen);
		
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
		
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.btnLogin:
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				Log.d("Button", "Login");
				JSONObject json = userFunction.loginUser(email, password);

				try {
					if (json.getString(Constans.KEY_SUCCESS) != null) {
						loginErrorMsg.setText("");
						String res = json.getString(Constans.KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){

							DataBaseHandler db = new DataBaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");
							
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(Constans.KEY_NAME), json_user.getString(Constans.KEY_LASTNAME), json_user.getString(Constans.KEY_EMAIL), json.getString(Constans.KEY_UID), json_user.getString(Constans.KEY_CREATED_AT));						

							Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
							
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							
							finish();
						}else{
							loginErrorMsg.setText("Niepoprawny login i/lub has³o");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			case R.id.btnLinkToRegisterScreen:
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				finish();
				break;
		}
	}
}
