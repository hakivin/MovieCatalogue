package com.hakivin.submission3.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.hakivin.submission3.BuildConfig;
import com.hakivin.submission3.R;
import com.hakivin.submission3.ui.main.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "movie catalogue";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CODE = "code";
    public static final int ID_DAILY = 100;
    public static final int ID_RELEASE_TODAY = 101;

    private final static String TIME_FORMAT = "HH:mm";

    public AlarmReceiver(){

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        int code = intent.getIntExtra(EXTRA_CODE, 0);
        if (code == ID_RELEASE_TODAY){
            Date date = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
            System.out.println("Date = " + fDate);
            AsyncHttpClient client = new AsyncHttpClient();
            final String url = BuildConfig.DISCOVER_MOVIE_URL+BuildConfig.TMDB_API_KEY+"&primary_release_date.gte="+fDate
                    +"&primary_release_date.lte="+fDate+"&with_original_language=en";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String title = null;
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray array = responseObject.getJSONArray("results");
                        if (!array.isNull(0)){
                            JSONObject movie = array.getJSONObject(0);
                            title = movie.getString("title");
                            sendNotification(context, title, title+context.getString(R.string.release_today_reminder), AlarmReceiver.ID_RELEASE_TODAY);
                        }
                        System.out.println("title = "+title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();
                }
            });
        }
        else {
            sendNotification(context, context.getString(R.string.app_name), context.getString(R.string.daily_reminder), code);
        }
        System.out.println("onReceive called");
    }

    private void sendNotification(Context context, String title, String message, int requestCode){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
        Notification notification = builder.build();
        if (notificationManager != null)
            notificationManager.notify(requestCode, notification);
        System.out.println("sendNotification called");
    }

    public void setRepeatingAlarm(Context context, String time, int requestCode){
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_CODE, requestCode);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        // Check if the Calendar time is in the past
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // it will tell to run to next day
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            System.out.println("setRepeatingAlarm called, getTimeMillis = " + calendar.getTimeInMillis() + "current time = " + new Date().getTime());
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5000L, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    public boolean isAlarmNotSet(Context context, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) == null;
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
