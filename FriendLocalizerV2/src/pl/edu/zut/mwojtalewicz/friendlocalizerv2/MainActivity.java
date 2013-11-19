package pl.edu.zut.mwojtalewicz.friendlocalizerv2;
import pl.edu.zut.mwojtalewicz.Library.ConnectionDetector;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {

	private UserFunctions userFunctions;	
	private ProgressDialog progress;
	
	private Boolean isInternetPresent;
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        
        if(!isInternetPresent)
        {
                setNewAlertDialog("Połączenie Internetowe", "Czy chcesz włączyć przesył danych?", this);
        }
        else
        {
                userFunctions = new UserFunctions();
                if(userFunctions.isUserLoggedIn(this)){
                	setContentView(R.layout.activity_main);
         
                	progress = ProgressDialog.show(this, "Ładowanie ustawień.",
                          "Proszę czekać...", true);
         
                	Intent logged = new Intent(getApplicationContext(), LoggedMainScreen.class);
                	logged.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	startActivity(logged);
                	progress.dismiss();
                	finish();
                }else{
                	Intent login = new Intent(getApplicationContext(), LogInActivity.class);
                	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	startActivity(login);
                	finish();
                }
        }
	}

	


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(isInternetPresent)
		{
            userFunctions = new UserFunctions();
            if(userFunctions.isUserLoggedIn(this)){
            	setContentView(R.layout.activity_main);
     
            	progress = ProgressDialog.show(this, "Ładowanie ustawień.",
                      "Proszę czekać...", true);
     
            	Intent logged = new Intent(getApplicationContext(), LoggedMainScreen.class);
            	logged.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(logged);
            	progress.dismiss();
            	finish();
            }else{
            	Intent login = new Intent(getApplicationContext(), LogInActivity.class);
            	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(login);
            	finish();
            }
		} else {
            setNewAlertDialog("Połączenie Internetowe", "Czy chcesz włączyć przesył danych?", this);
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
		}
	}

	private void setNewAlertDialog(String title, String msg, Context context)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	startActivity(intent);
				}
			});
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nie", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
        alertDialog.show();
	}
}
