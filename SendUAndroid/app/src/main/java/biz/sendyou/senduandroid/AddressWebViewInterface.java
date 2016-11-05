package biz.sendyou.senduandroid;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import biz.sendyou.senduandroid.Fragment.OrderCardFragment;

/**
 * Created by parkjaesung on 2016. 11. 6..
 */

public class AddressWebViewInterface {
    private WebView mWebView;
    private Activity mActivity;
    private OrderCardFragment mOrderCardFragment;

    public AddressWebViewInterface(Activity mActivity, WebView mWebView, OrderCardFragment mOrderCardFragment){
        this.mActivity = mActivity;
        this.mWebView = mWebView;
        this.mOrderCardFragment = mOrderCardFragment;
    }

    @JavascriptInterface
    public void setAddress(String address){
        Log.i("setAddress", "address : " + address);
        this.mOrderCardFragment.setNumAddress(address);
        this.mOrderCardFragment.closeAddressWebViewDialog();
    }

}
