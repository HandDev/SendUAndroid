package sendyou.biz.senduandroid.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.fragment.ContactsFragment;
import sendyou.biz.senduandroid.fragment.MainFragment;
import sendyou.biz.senduandroid.fragment.SelectTemplateFragment;
import sendyou.biz.senduandroid.fragment.TrackFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    private static final long RIPPLE_DURATION = 250;
    private View guillotineMenu;
    private GuillotineAnimation guillotineAnimation;
    private GoogleApiClient mGoogleApiClient;
    private Data mData = (Data) getApplication();

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

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();

        Log.w(TAG, mData.getUserInfo().toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
