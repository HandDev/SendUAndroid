package biz.sendyou.senduandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.Calendar;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.EmailCheck;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.SignUpService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chan_Woo_Kim on 2016-08-24.
 */
public class SignupInputActivity extends AppCompatActivity implements View.OnClickListener,CalendarDatePickerDialogFragment.OnDateSetListener {

    private EditText mFirstNameEditText;
    private EditText mNameEditText;
    private EditText mBirthEditText;
    private EditText mEmailEditText;
    private EditText mPasswordCheckEditText;
    private EditText mPasswordEditText;
    private static final String URL = "http://52.78.159.163:3000/user/";

    private static final String FRAG_TAG_DATE_PICKER = "Select Date";

    private SignupInputActivity signupInputActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_input);

        findViewById(R.id.signup_next_imageview).setOnClickListener(this);

        signupInputActivity = this;

        mFirstNameEditText = (EditText)findViewById(R.id.signup_first_name_edittext);
        mNameEditText = (EditText)findViewById(R.id.signup_name_edittext);
        mBirthEditText = (EditText)findViewById(R.id.signup_birth);
        mEmailEditText = (EditText)findViewById(R.id.signup_email_edittext);
        mPasswordEditText = (EditText)findViewById(R.id.signup_password_edittext);
        mPasswordCheckEditText = (EditText)findViewById(R.id.signup_password_check_edittext);

        mBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(SignupInputActivity.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(mFirstNameEditText.getText().toString().matches("") || mNameEditText.getText().toString().matches("") || mEmailEditText.getText().toString().matches("") || mPasswordCheckEditText.getText().toString().matches("") || mPasswordEditText.getText().toString().matches("")) {
            Toast.makeText(getBaseContext(),"빈칸이 있는지 확인해주세요.",Toast.LENGTH_LONG).show();
        }
        else if(mPasswordEditText.getText().toString().matches(mPasswordCheckEditText.getText().toString())) {
            emailck();
            Log.e("password",mPasswordEditText.getText().toString());
            Log.e("check",mPasswordCheckEditText.getText().toString());
        }
        else {
            Toast.makeText(getBaseContext(),"비밀번호를 확인해주세요.",Toast.LENGTH_LONG).show();
        }
    }

    private void emailck() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmailCheck emailCheck = retrofit.create(EmailCheck.class);

        Call<Repo> call = emailCheck.emailck(mEmailEditText.getText().toString());

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Repo repo = response.body();

                Log.e("Resp",String.valueOf(repo.isSuccess()));
                Log.e("Response",response.raw().toString());
                Log.e("Response",response.message());

                if(repo.isSuccess()){
                    Intent mIntent = new Intent(signupInputActivity, SignupAddressActivity.class);
                    mIntent.putExtra("userName", mFirstNameEditText.getText().toString() + mNameEditText.getText().toString());
                    mIntent.putExtra("email", mEmailEditText.getText().toString());
                    mIntent.putExtra("password", mPasswordCheckEditText.getText().toString());
                    mIntent.putExtra("birth",mBirthEditText.getText().toString());
                    startActivity(mIntent);
                    finish();
                }

                else {
                    Toast.makeText(signupInputActivity, "중복된 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        int months = monthOfYear+1;
        String month,day,years;
        years = String.valueOf(year);
        if(monthOfYear<10) {
            month = "0"+months;
        }
        else {
            month = String.valueOf(monthOfYear);
        }
        if(dayOfMonth<10) {
            day = "0"+dayOfMonth;
        }
        else {
            day = String.valueOf(dayOfMonth);
        }
        mBirthEditText.setText(dialog.getResources().getString(R.string.calendar_date_picker_result_values, years, month, day));
    }
}
