package pl.edu.zut.mwojtalewicz.friendlocalizer;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Constans;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements OnClickListener {
	
	private Button btnRegister;
	private Button btnLinkToLogin;
	private EditText inputName;
	private EditText inputLastname;
	private EditText inputEmail;
	private EditText inputPassword;
	private TextView registerErrorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		inputName = (EditText) findViewById(R.id.registerName);
		inputLastname = (EditText)findViewById(R.id.registerLastname);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		
		btnRegister.setOnClickListener(this);
		btnLinkToLogin.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.btnRegister:
				String name = inputName.getText().toString();
				String lastname = inputLastname.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.registerUser(name, lastname, email, password);
				
				try {
					if (json.getString(Constans.KEY_SUCCESS) != null) {
						registerErrorMsg.setText("");
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
							registerErrorMsg.setText("Wyst¹pi³ problem z rejestracj¹.");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			case R.id.btnLinkToLoginScreen:
				Intent i = new Intent(getApplicationContext(), LogInActivity.class);
				startActivity(i);
				finish();
				break;
		}
	}

}
