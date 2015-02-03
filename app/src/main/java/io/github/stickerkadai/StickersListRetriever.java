package io.github.stickerkadai;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class StickersListRetriever extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        Log.d("StickersListRetriever", "Processing Started");
        String[] IMAGES= null;
        try{
            Context context =(Context) params[0];
            Boolean isConnectedToInternet = false;
            JSONArray StickersArray;

            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                            isConnectedToInternet = true;
            }

            if(isConnectedToInternet) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://stickerkadai.github.io/stickers.json");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String response = EntityUtils.toString(httpEntity);
                    StickersArray = new JSONArray(response);
                    context.getSharedPreferences("io.github.stickerkadai", 0).edit().putString("Stickers", StickersArray.toString()).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                SharedPreferences sp = context.getSharedPreferences("io.github.stickerkadai", Context.MODE_PRIVATE);
                String Stickers=sp.getString("Stickers", "");
                StickersArray = new JSONArray(Stickers);
                int noOfStickers = StickersArray.length();
                IMAGES = new String[noOfStickers];
                for (int i = 0; i < noOfStickers; i++)
                    IMAGES[i] = "http://stickerkadai.github.io/Stickers/" + StickersArray.getJSONObject(i).getString("path");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return IMAGES;
    }
}