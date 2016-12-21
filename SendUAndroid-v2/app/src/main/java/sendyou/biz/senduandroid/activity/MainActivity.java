package sendyou.biz.senduandroid.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.fragment.ContactsFragment;
import sendyou.biz.senduandroid.fragment.MainFragment;
import sendyou.biz.senduandroid.fragment.OrderFragment;
import sendyou.biz.senduandroid.fragment.TrackFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    private static final long RIPPLE_DURATION = 250;
    private View guillotineMenu;
    private GuillotineAnimation guillotineAnimation;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.root_main) FrameLayout root;
    @BindView(R.id.content_hamburger) View contentHamburger;
    @BindView(R.id.content) FrameLayout content;

    @BindColor(R.color.light_magenta) int light_magenta;
    @BindString(R.string.exit_q) String exit_q;
    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.cancel) String cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_group :
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.order_group :
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new OrderFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.contacts_group :
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new ContactsFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.track_group :
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new TrackFragment()).commit();
                guillotineAnimation.close();
                break;
            case R.id.settings_group :
                break;
            case R.id.logout_group :
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(exit_q)
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
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
}
