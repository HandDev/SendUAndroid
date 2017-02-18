package sendyou.biz.senduandroid.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.data.UserInfo;
import sendyou.biz.senduandroid.fragment.ContactsFragment;
import sendyou.biz.senduandroid.fragment.EditFragment;
import sendyou.biz.senduandroid.fragment.MainFragment;
import sendyou.biz.senduandroid.fragment.SelectTemplateFragment;
import sendyou.biz.senduandroid.fragment.TrackFragment;
import sendyou.biz.senduandroid.service.RegisterToken;
import sendyou.biz.senduandroid.service.cInAppBillingHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    private final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlY5e0w2OBBriVEDQHc3T95yQAN2BQSXAqwURFyoGweCsf5oR08Sr9FNDxoNkEa6bzDdCujyYOnaxz1to4cDIEjGNMHdhMGSD/f6Z2YRbPbBirSMyIZjLhn6WytJkGz/zEbpYLf7GeMDcYgIX/711WontmP3h/KA3PDQXEHCfq0PoxFrHkOK1CEW6buNVDe+xT8OlaKdWPW6ELJGFXNyPpCSJs7R/CWeQgr0Pa/GhhOwaAXtXkmCeDZzw6iBtFCyek6iYBiClcRDxynuFL4N7fjBKtJI3mStPl3wBcvNGyW/cy/BRUCwOuJ2QtP6PIixsd5kDOoBPJ1FULuKaNism6wIDAQAB";
    private static final long RIPPLE_DURATION = 250;
    private View guillotineMenu;
    private GuillotineAnimation guillotineAnimation;
    private GoogleApiClient mGoogleApiClient;
    private Data mData = (Data) getApplication();
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private cInAppBillingHelper mHelper;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.root_main) FrameLayout root;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.content) FrameLayout content;
    @BindView(R.id.actionbar_title) TextView title;

    @BindColor(R.color.light_magenta) int light_magenta;
    @BindString(R.string.exit_q) String exit_q;
    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.cancel) String cancel;
    @BindString(R.string.logout_q) String logout_q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Runtime.getRuntime().gc();

        mHelper = new cInAppBillingHelper(this, PUBLIC_KEY)
        {
            @Override
            public void addInventory()
            {
            }
        };

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(light_magenta);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        guillotineMenu.findViewById(R.id.home_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.order_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.contacts_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.track_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.settings_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.logout_group).setOnClickListener(this);

        sendToken();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();

        Log.w(TAG, mData.getUserInfo().toString());
    }

    public void sendToken() {
        final String token = FirebaseInstanceId.getInstance().getToken();
        Log.w(TAG, token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterToken registerToken = retrofit.create(RegisterToken.class);

        Call<UserInfo> call = registerToken.register(mData.getUserInfo().getUid(), token);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                Log.w(TAG, "Success");
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.w(TAG, "Fail " + t.getStackTrace());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_group :
                title.setText(getString(R.string.home));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.order_group :
                title.setText(getString(R.string.order));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new SelectTemplateFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.contacts_group :
                title.setText(getString(R.string.contacts));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new ContactsFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.track_group :
                title.setText(getString(R.string.track));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new TrackFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.settings_group :
                guillotineAnimation.close();
                break;
            case R.id.logout_group :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(logout_q)
                        .setCancelable(false)
                        .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    public cInAppBillingHelper getHelper() {
        return mHelper;
    }

    @Override
    protected void onStart() {
        switch (mData.getUserInfo().getStatus()) {
            case 1:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getResources().getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                mGoogleApiClient.connect();
                break;
        }
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        String message = exit_q;
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);

        switch (fragment.getClass().getName()) {
            case "sendyou.biz.senduandroid.fragment.SelectTemplateFragment":
            case "sendyou.biz.senduandroid.fragment.EditFragment":
                message = "편지 작성을 취소하겠습니까?";
                break;
            case "sendyou.biz.senduandroid.fragment.AddressFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new EditFragment()).commit();
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (fragment.getClass().getName()) {
                            case "sendyou.biz.senduandroid.fragment.SelectTemplateFragment":
                            case "sendyou.biz.senduandroid.fragment.EditFragment":
                            case "sendyou.biz.senduandroid.fragment.AddressFragment":
                                title.setText(getString(R.string.home));
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
                                break;
                            default:
                                System.exit(0);
                                break;
                        }
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public IInAppBillingService getService() {
        return mService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
        mHelper.destroy();
        Runtime.getRuntime().gc();
    }
}
