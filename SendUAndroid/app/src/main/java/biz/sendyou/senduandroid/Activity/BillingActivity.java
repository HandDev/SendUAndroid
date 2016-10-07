package biz.sendyou.senduandroid.Activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Util.billing.IabHelper;
import biz.sendyou.senduandroid.Util.billing.IabResult;
import biz.sendyou.senduandroid.Util.billing.Purchase;
import biz.sendyou.senduandroid.Util.billing.cInAppBillingHelper;

import com.android.vending.billing.*;

import org.json.JSONObject;

import java.util.ArrayList;

public class BillingActivity extends AppCompatActivity {

    private final String LOGTAG = "BillingActivity";
    private String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiLTzA7QObMr8Ifg63ijffxIi9FAgNVs116f0mCUO/myBvi0hKNXqZK5Gh1+yysSDCwGSCF3uHVz6IQQRXxVf/zBfVKu/8dl7KDIHRy93Tc1ooUN0UpCljpcu9eLEoFhOlQ4z4BWEGzUXogd8i0CkHC93Tjk2yXENRBgT8mMh0Q6ufxC28LEs3PFO3rBU9WD7EcGPN1Y9dQ0tZA8QRhtUXz5zULsC60vaSqElsXZKnKitK5sc1lH/NFe12McD+i+AuKI1YONQj1xpsBZ6xT1cp0cv34HXMzfj3cE7Feb6mIKnHmPtFXKtYutx2JC0IS66RrVpJHFhVJ3aScEXUij/WQIDAQAB";
    cInAppBillingHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        mHelper = new cInAppBillingHelper(this,key) {
            @Override
            public void addInventory() {
            }
        };

        Button button = (Button)findViewById(R.id.billing_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.buy("test1");
            }
        });

        Log.i(LOGTAG,"Buy Product");
        //mHelper.buy("test1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHelper.activityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
