package sendyou.biz.senduandroid.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONObject;

import java.util.ArrayList;

import sendyou.biz.senduandroid.util.IabHelper;
import sendyou.biz.senduandroid.util.IabResult;
import sendyou.biz.senduandroid.util.Purchase;

/**
 * Created by pyh42 on 2017-02-18.
 */

public abstract class cInAppBillingHelper
{
    private String          PUBLIC_KEY;

    private final int       REQUEST_CODE = 1001;

    private Activity mActivity;
    private IInAppBillingService mService;
    private IabHelper mHelper;

    public cInAppBillingHelper(Activity _act, String _public_key)
    {
        mActivity           = _act;
        PUBLIC_KEY          = _public_key;
        init();
    }

    ServiceConnection mServiceConn = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mService        = IInAppBillingService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mService        = null;
        }
    };

    public void init()
    {
        // 패키지를 명시적으로 설정. ( => 모든 버전의 안드로이드에서 작동시키기 위함.)
        Intent              intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        mActivity.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

        mHelper             = new IabHelper(mActivity, PUBLIC_KEY);
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            @Override
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess())
                {
                    // TODO:: 구매오류처리 (토스트 하나 띄우고 결제팝업 종료시키면 됨)
                    Toast.makeText(mActivity, "구매 실패", Toast.LENGTH_SHORT).show();
                }
                // 구매목록을 초기화하는 메서드.
                // v3으로 넘어오면서 구매기록이 모두 남게 되는데, 재구매 가능한 상품( 게임에서는 코인같은 아잍메은 ) 구매후 삭제해줘야 함.
                // 이 메서드는 상품 구매전 혹은 후에 반드시 호출해야한다. ( 재구매가 불가능한 1회성 아이템의경우 호출하면 안됨. )
                AlreadyPurchaseItems();
            }
        });
    }

    public void destroy()
    {
        if (mServiceConn != null)
            mActivity.unbindService(mServiceConn);
    }

    public void AlreadyPurchaseItems()
    {
        try
        {
            Bundle ownedItems = mService.getPurchases(3, mActivity.getPackageName(), "inapp", null);
            int             response = ownedItems.getInt("RESPONSE_CODE");

            if (response == 0)
            {
                ArrayList purchaseDataList  = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                String[] tokens = new String[purchaseDataList.size()];

                for (int i = 0; i < purchaseDataList.size(); ++i)
                {
                    String purchaseData = (String)purchaseDataList.get(i);
                    JSONObject jo   = new JSONObject(purchaseData);
                    tokens[i]   = jo.getString("purchaseToken");
                    // 여기서 tokens를 모두 컨슘 해주기
                    mService.consumePurchase(3, mActivity.getPackageName(), tokens[i]);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void buy(String _item)
    {
        try
        {
            Bundle buyIntentBundle  = mService.getBuyIntent(3, mActivity.getPackageName(), _item, "inapp", "test");     // 부정 방지를 위해 임의로 토큰 생성.
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

            if(mHelper != null) {
                mHelper.flagEndAsync();
            }
            if (pendingIntent != null)
            {
                mHelper.launchPurchaseFlow(mActivity, mActivity.getPackageName(), REQUEST_CODE, mPurchaseFinishedListener, "test");
            }
            else
            {
                // 결제가 막혔다면
                Toast.makeText(mActivity, "구매 실패", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(mActivity, "구매 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener   = new IabHelper.OnIabPurchaseFinishedListener()
    {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info)
        {
            if (result.isFailure())
            {
                AlreadyPurchaseItems();
            }
            else
            {
                Toast.makeText(mActivity, "구매!", Toast.LENGTH_SHORT).show();
                // 여기서 아이템 추가 해주면 됨.
                addInventory();
                AlreadyPurchaseItems();
            }
        }
    };

    public abstract void addInventory();

    public void activityResult(int _requestCode, int _resultCode, Intent _data)
    {
        // IabHelper가 null값인 경우 리턴.
        if (mHelper == null)
            return;

        // IabHelper가 데이터를 핸들링하도록 데이터 전달.
        if (!mHelper.handleActivityResult(_requestCode, _resultCode, _data))
            return;
    }
}