package pl.edu.zut.mwojtalewicz.friendLocalizerLibrary;

import pl.edu.zut.mwojtalewicz.friendlocalizer.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Notification {
	
	private Intent newActivity;
	
	public Notification(Intent targetIntent)
	{
		this.newActivity = targetIntent;
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

}
