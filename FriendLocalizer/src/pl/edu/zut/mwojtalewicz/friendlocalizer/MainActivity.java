package pl.edu.zut.mwojtalewicz.friendlocalizer;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class MainActivity extends Activity {
	
	private UserFunctions userFunctions;	
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
        	setContentView(R.layout.activity_main);
        	
        	progress = ProgressDialog.show(this, "£adowanie ustawieñ.",
        			  "Proszê czekaæ...", true);
        	
        	new Thread() {
        		public void run() {
	        		try{
	            	Intent logged = new Intent(getApplicationContext(), LoggedMainScreen.class);
	            	logged.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	startActivity(logged);
	            	finish();
	        		} catch (Exception e) {
	        		}
	        		progress.dismiss();
        		}
        	}.start();
        }else{
        	Intent login = new Intent(getApplicationContext(), LogInActivity.class);
        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(login);
        	finish();
        }
	}
}
