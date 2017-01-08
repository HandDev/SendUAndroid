package sendyou.biz.senduandroid.util;

import android.content.Context;
import android.util.Log;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

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
    private Context context;
    private List<String> raw_keys = new ArrayList<>();
    private List<String> thumb_keys = new ArrayList<>();
    ThreadReceive mThreadReceive;

    public TemplateDownloadThread(Context context, ThreadReceive threadReceive) {
        this.context = context;
        this.mThreadReceive = threadReceive;
    }

    public interface ThreadReceive {
        public void onReceiveRun();
    }

    @Override
    public void run() {
        System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");

        this.s3 = AWSManager.getInstance(context).getS3();

        AmazonS3Client s3 = new AmazonS3Client(AWSManager.getInstance(context).getCredentialsProvider());

        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        for ( S3ObjectSummary summary : S3Objects.withPrefix(s3, "cardbackground","raw") ) {
            Log.i(LOGTAG, "Object with key : " + summary.getKey());

            raw_keys.add(summary.getKey());
        }

        for ( S3ObjectSummary summary : S3Objects.withPrefix(s3, "cardbackground","thumb") ) {
            Log.i(LOGTAG, "Object with key : " + summary.getKey());

            thumb_keys.add(summary.getKey());
        }
        mThreadReceive.onReceiveRun();
    }

    public List<String> getRaw_keys() {
        return raw_keys;
    }

    public List<String> getThumb_keys() {
        return thumb_keys;
    }
}
