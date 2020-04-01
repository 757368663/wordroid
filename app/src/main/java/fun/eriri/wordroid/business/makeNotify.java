package fun.eriri.wordroid.business;

import fun.eriri.wordroid.activitys.Main;
import fun.eriri.wordroid.database.DataAccess;
import fun.eriri.wordroid.model.WordList;
import wordroid.model.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;

public class makeNotify extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			Log.e("1", "onReceive: 1" );
			OperationOfBooks OOB = new OperationOfBooks();
			SharedPreferences settings = context.getSharedPreferences("wordroid.model_preferences", 0);
			OOB.setNotify(settings.getString("time", "18:00 下午"),context);
		}
		else if(intent.getAction().equals("shownotify")){
			Log.e("2", "onReceive: 2" );
			SharedPreferences settings = context.getSharedPreferences("wordroid.model_preferences", 0);
			if(settings.getBoolean("notify", false)){
				DataAccess data = new DataAccess(context);
				ArrayList<WordList> list=data.QueryList(null, null);
				boolean notify=false;
				for(int i=0;i<list.size();i++){
					if (list.get(i).getShouldReview().equals("1")){
						notify=true;
						break;
					}
				}
				if(notify){
					NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification.Builder builder = null ;
					if (Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O){
						builder = new Notification.Builder(context);
						builder.setContentTitle("有单词需要复习~")
								.setContentText("快来复习吧")
								.setSmallIcon(R.drawable.icon)
								.setWhen(System.currentTimeMillis())
								.setAutoCancel(true);
					}
					else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
						//channelld 必须和channelid一致！！！
						builder = new Notification.Builder(context,"1");
						builder.setContentTitle("有单词需要复习~")
								.setContentText("快来复习吧")
								.setSmallIcon(R.drawable.icon)
								.setWhen(System.currentTimeMillis())
								.setAutoCancel(true);
						Log.e("os", "onReceive: 10" );
						NotificationChannel channel = new NotificationChannel("1","单词",NotificationManager.IMPORTANCE_HIGH);
						notiManager.createNotificationChannel(channel);
						Log.i("receive", "receive" + channel.toString());
					}

//					Notification notification = new Notification(R.drawable.icon, "有单词需要复习~", System.currentTimeMillis());
					Intent intent1 = new Intent(context, Main.class);
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
							intent1, PendingIntent.FLAG_UPDATE_CURRENT);


//					notification.setLatestEventInfo(context, "复习提醒", "有单词需要复习~",
//							contentIntent);
					builder.setContentIntent(contentIntent);
					notiManager.notify(1, builder.build());
					Log.i("receive", "receive");
				}

			}
		}


	}
}
