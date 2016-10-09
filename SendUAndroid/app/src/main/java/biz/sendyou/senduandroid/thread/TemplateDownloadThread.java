package biz.sendyou.senduandroid.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.AWSManager;
import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.Util.HttpUtil;

/**
 * Created by parkjaesung on 2016. 8. 23..
 */
/*
Usage
TemplateDownloadThread thread = new TemplateDownloadThread();

thread.join();
thread.getRaw_keys();
thread.getThumb_keys();

 */
public class TemplateDownloadThread extends Thread {

    private final String LOGTAG  = "TemplateDownloadThread";
    private AmazonS3 s3;

    private List<String> raw_keys = new ArrayList<>();
    private List<String> thumb_keys = new ArrayList<>();

    public TemplateDownloadThread() {

    }

    @Override
    public void run() {
        System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");

        this.s3 = AWSManager.getInstance(Usr.getContext()).getS3();

        AmazonS3Client s3 = new AmazonS3Client(AWSManager.getInstance(Usr.getContext()).getCredentialsProvider());

        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        for ( S3ObjectSummary summary : S3Objects.withPrefix(s3, "cardbackground","raw") ) {
            Log.i(LOGTAG, "Object with key : " + summary.getKey());

            raw_keys.add(summary.getKey());
        }

        for ( S3ObjectSummary summary : S3Objects.withPrefix(s3, "cardbackground","thumb") ) {
            Log.i(LOGTAG, "Object with key : " + summary.getKey());

            thumb_keys.add(summary.getKey());
        }
    }

    public List<String> getRaw_keys() {
        return raw_keys;
    }

    public List<String> getThumb_keys() {
        return thumb_keys;
    }
}
