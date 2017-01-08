package sendyou.biz.senduandroid.util;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;

/**
 * Created by parkjaesung on 2016. 9. 22..
 */
//Usage -> AWSManager.getInstance(getApplicationContext)
public class AWSManager {
    private static AWSManager instance = null;
    private AmazonS3 s3 = null;
    private CognitoCachingCredentialsProvider credentialsProvider = null;

    private AWSManager(Context context){
        this.credentialsProvider = new CognitoCachingCredentialsProvider(
                context,    /* get the context for the application */
                "ap-northeast-1:9c6cb252-ef36-46a1-9222-0dc9ecfa5920",    /* Identity Pool ID */
                Regions.AP_NORTHEAST_1           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        s3 = new AmazonS3Client(credentialsProvider);
        s3.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
    }

    public static AWSManager getInstance(Context context){
        if(instance == null){
            instance = new AWSManager(context);
        }

        return instance;
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public AmazonS3 getS3() {
        return s3;
    }
}
