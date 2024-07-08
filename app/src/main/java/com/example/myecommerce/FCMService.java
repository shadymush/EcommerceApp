package com.example.myecommerce;

import android.os.AsyncTask;
import android.util.Log;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class FCMService {

    private static final String SERVER_KEY = "d52MYHBj93XLHzdK6MyFqZp5HAu-TSSEAifCZ4hhMoQ"; // Replace with a secure mechanism
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "FCMService";

    public static void sendNotification(String userToken, String title, String message) {
        new SendNotificationTask(userToken, title, message).execute();
    }

    private static class SendNotificationTask extends AsyncTask<Void, Void, Void> {

        private String userToken;
        private String title;
        private String message;

        public SendNotificationTask(String userToken, String title, String message) {
            this.userToken = userToken;
            this.title = title;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            JSONObject json = new JSONObject();
            JSONObject dataJson = new JSONObject();
            try {
                dataJson.put("title", title);
                dataJson.put("message", message);

                json.put("to", userToken);
                json.put("data", dataJson);

                RequestBody body = RequestBody.create(json.toString(), JSON);
                Request request = new Request.Builder()
                        .header("Authorization", "key=" + SERVER_KEY)
                        .url(FCM_URL)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Notification sending failed: " + response.body().string());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error sending notification", e);
            }

            return null;
        }
    }
}
