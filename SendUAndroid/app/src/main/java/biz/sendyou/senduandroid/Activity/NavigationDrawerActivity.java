package biz.sendyou.senduandroid.Activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.Fragment.AddressBookFragment;
import biz.sendyou.senduandroid.Fragment.CreateCardFragment;
import biz.sendyou.senduandroid.Fragment.FrontFragment;
import biz.sendyou.senduandroid.Fragment.OrderCardFragment;
import biz.sendyou.senduandroid.Fragment.SelectTemplateFragment;
import biz.sendyou.senduandroid.Fragment.SendCheckFragment;
import biz.sendyou.senduandroid.Fragment.SettingFragment;
import biz.sendyou.senduandroid.Fragment.SignInFragment;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.Address;
import biz.sendyou.senduandroid.datatype.CardTemplate;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SignInFragment.OnFragmentInteractionListener,FrontFragment.OnFragmentInteractionListener, AddressBookFragment.OnListFragmentInteractionListener, CreateCardFragment.OnFragmentInteractionListener,SelectTemplateFragment.OnListFragmentInteractionListener, OrderCardFragment.OnFragmentInteractionListener{

    final int DEFAULT_LODING_COUNT = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("                     SendU");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            changeFragmentToFront();
        }
    }

    public void setToolBarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {
            // Handle the camera action
            changeFragmentToSelectTemplate();
        } else if (id == R.id.nav_address) {
            changeFragmentToAddressBook();
        } else if (id == R.id.nav_storage) {

        } else if (id == R.id.nav_sendcheck) {
            changeFragmentToSendCheck();
        } else if (id == R.id.nav_settings) {
            changeFragmentToSetting();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //TODO Refactor methods
    private void changeFragmentToAddressBook(){
        AddressBookFragment addressBookFragment = AddressBookFragment.newInstance(1);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,addressBookFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToFront(){
        FrontFragment mFrontFragment = FrontFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,mFrontFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToSendCheck(){
        SendCheckFragment mSendCheckFragment = SendCheckFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,mSendCheckFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToSetting() {
        SettingFragment mSettingFragment = SettingFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,mSettingFragment);
        fragmentTransaction.commit();

    }

    private void changeFragmentToSelectTemplate() {
        //TODO Remove Creating DummyData code
        List<CardTemplate> templates = new ArrayList<>();

        String result = SendByHttp(); // 메시지를 서버에 보냄
        String[][] parsedData = jsonParserList(result);

        for(int i = 1;i <= DEFAULT_LODING_COUNT ;i++) {
            templates.add(new CardTemplate());
        }

        SelectTemplateFragment mSelectTemplateFragment = SelectTemplateFragment.newInstance(templates ,2);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, mSelectTemplateFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToCreateCrad() {
        CreateCardFragment mCreateCardFragment = CreateCardFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, mCreateCardFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Address item) {

    }

    @Override
    public void onListFragmentInteraction(CardTemplate item) {

    }

    private String SendByHttp() {

        String URL = "https://api.imgur.com/3/album/DevHand";

        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(URL);

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(),
                            "utf-8"));

            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            client.getConnectionManager().shutdown();
            return "";
        }

    }

    public String[][] jsonParserList(String pRecvServerPage) {

        Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json != null) {
                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }

            for(int i=0; i<parseredData.length; i++){
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);
            }

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
