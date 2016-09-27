package biz.sendyou.senduandroid;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

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
                "us-west-2:3e511588-52d8-4322-a497-0739c9a26773",    /* Identity Pool ID */
                Regions.US_WEST_2           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        s3 = new AmazonS3Client(credentialsProvider);
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
