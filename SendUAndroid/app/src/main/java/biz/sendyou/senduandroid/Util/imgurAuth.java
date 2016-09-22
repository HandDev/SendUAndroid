package biz.sendyou.senduandroid.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pyh42 on 2016-08-28.
 */
public class imgurAuth extends Thread{

    private static final String TAG = "imgurAuth";
    private String clientID = "84a5444517aec75";

    @Override
    public void run() {
        String response = null;
        Log.w(TAG, "start");
        try {
            response = sendHttpRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.w(TAG, "complete");
        //Log.w(TAG, response);
        super.run();
    }

    private String sendHttpRequest() throws IOException {
        URL url = new URL("https://api.imgur.com/3/account/");

        String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode("devhand", "UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String buf;

        while((buf = br.readLine()) != null) {
            stb.append(buf).append("\n");
        }
        br.close();
        wr.close();

        return stb.toString();
    }
}
