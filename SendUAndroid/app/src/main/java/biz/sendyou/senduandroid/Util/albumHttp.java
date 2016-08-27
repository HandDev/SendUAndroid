package biz.sendyou.senduandroid.Util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pyh42 on 2016-08-27.
 */
public class albumHttp extends Thread {
    private static final String TAG = "albumHttp";

    @Override
    public void run() {
        Log.w(TAG, "start");
        SendByHttp();
        Log.w(TAG, "complete");
        super.run();
    }
    private String SendByHttp() {

        String URL = "https://api.imgur.com/3/album/iF1M8";

        DefaultHttpClient client = new DefaultHttpClient();
        try {
            Map<String, String> hearders = new HashMap<>();
            hearders.put("Authorization", "ad80551cc425295239239d3d659ce4f3d26ca01d");
            HttpPost post = new HttpPost(HttpUtil.get(URL, hearders));

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(),
                            "utf-8"));

            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            client.getConnectionManager().shutdown();
            return "";
        }

    }

    public String[][] jsonParserList(String pRecvServerPage) {

        Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json != null) {
                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }

            for(int i=0; i<parseredData.length; i++){
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);
            }

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
