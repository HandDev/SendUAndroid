package biz.sendyou.senduandroid.thread;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;

import biz.sendyou.senduandroid.Util.HttpUtil;

/**
 * Created by parkjaesung on 2016. 8. 23..
 */
public class TemplateDownloadThread extends AsyncTask<Void, Void, Void> {

    private final String LOGTAG  = "TemplateDownloadThread";
    private ArrayList<String> urls = new ArrayList<>();

    @Override
    protected void onPreExecute() {
        Log.i(LOGTAG, "onPreExecute");
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.i(LOGTAG, "doInBackGorund started");
        String templatesJSON = "";
        try {
            templatesJSON = HttpUtil.get("http://sendyou.biz/resources/templates");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(templatesJSON);
            JSONArray templateURLArray = jsonObject.getJSONArray("templates");

            for(int i=0; i<templateURLArray.length(); i++){
                JSONObject url = (JSONObject) templateURLArray.get(i);
                urls.add(url.getString(String.valueOf(i+1)));
            }
        }catch (Exception e ){
            e.printStackTrace();
        }

        Log.i(LOGTAG,jsonObject.toString());
        Log.i(LOGTAG, "urls length :" + urls.size());

        return null;
    }
}
