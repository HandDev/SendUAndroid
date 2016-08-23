package biz.sendyou.senduandroid.Util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by pyh42 on 2016-08-12.
 */
public class Http extends Thread {

    private static String TAG = "Http";

    /**********  File Path *************/
    final String uploadFile = "/template/file.jpg";//경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보

    @Override
    public void run() {
        try {
            Log.w(TAG, "start");
            uploadPictureToServer(uploadFile);
            Log.w(TAG, "complete");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void uploadPictureToServer(String i_file) throws ClientProtocolException, IOException, NoSuchAlgorithmException {
        // TODO Auto-generated method stub
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        Log.w(TAG, "1");
        UUID uid = UUID.randomUUID();

        File file = new File(i_file);

        //Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        //Get the checksum
        String checksum = FileCheckSum.getFileChecksum(md5Digest, file);

        HttpPost httppost = new HttpPost("http://sendukor7833.cloudapp.net:8080/PostCardManageSystem_war/file/upload?orderuuid=" + uid.toString() + "&checksum=" + checksum);

        Log.w(TAG, "2");
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file, "image/jpeg");
        mpEntity.addPart("userfile", cbFile);

        Log.w(TAG, "3");
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        Log.w(TAG, "4");
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        httpclient.getConnectionManager().shutdown();

    }
}
