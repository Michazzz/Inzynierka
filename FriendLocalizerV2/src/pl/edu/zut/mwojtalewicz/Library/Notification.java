package pl.edu.zut.mwojtalewicz.Library;

import pl.edu.zut.mwojtalewicz.friendlocalizerv2.R;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Notification {
	
	private Intent newActivity;
	public Notification(Intent targetIntent)
	{
		this.newActivity = targetIntent;
	}
	
	public Notification(Context ctx)
	{
	}
	
	public void displayOwnNotification(Context context, String title, String message, String json)
	{
		this.newActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.newActivity.putExtra("friendlistjson", json);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, this.newActivity, PendingIntent.FLAG_UPDATE_CURRENT);
		
		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
		contentView.setImageViewResource(R.id.notify_image, R.drawable.ic_launcher);
		contentView.setTextViewText(R.id.title_text, title);
		contentView.setTextViewText(R.id.message_text, message);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
			            							.setContentIntent(contentIntent)
			            							.setAutoCancel(true)
										    		.setContentTitle(title)
										    		.setContentText(message)
										    		.setContent(contentView)
										    		.setSmallIcon(R.drawable.ic_launcher);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}
	
	public void setNewAlertDialog(String title, String msg, Context context)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
        });
        alertDialog.show();
	}
}
