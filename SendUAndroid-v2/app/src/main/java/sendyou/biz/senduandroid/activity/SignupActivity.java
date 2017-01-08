package sendyou.biz.senduandroid.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.data.Response;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.service.Login;
import sendyou.biz.senduandroid.service.SignUp;
import sendyou.biz.senduandroid.widget.TermDialog;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignupActivity";
    private Data mData = (Data)getApplication();

    @BindView(R.id.birthday_edit) EditText birthday_edit;
    @BindView(R.id.actionbar_title) TextView title;
    @BindView(R.id.first_address) EditText first_address;
    @BindView(R.id.second_address) EditText second_address;
    @BindView(R.id.address_num) EditText address_num;
    @BindView(R.id.name_edit) EditText name_edit;
    @BindView(R.id.phone_num) EditText phone_num;
    @BindView(R.id.signup_btn) Button signup_btn;
    @BindView(R.id.check_all_terms) CheckBox check_all;
    @BindView(R.id.check_terms1) CheckBox check_terms1;
    @BindView(R.id.check_terms2) CheckBox check_terms2;
    @BindView(R.id.check_terms3) CheckBox check_terms3;
    @BindView(R.id.terms_detail1) TextView terms_detail1;
    @BindView(R.id.terms_detail2) TextView terms_detail2;
    @BindView(R.id.terms_detail3) TextView terms_detail3;

    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.cancel) String cancel;
    @BindString(R.string.term1) String term1;
    @BindString(R.string.term2) String term2;
    @BindString(R.string.term3) String term3;

    int b_year, b_month, b_day;
    String birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        FirebaseAuth.getInstance().signOut();
        title.setText("가입");

        name_edit.setText(mData.getUserInfo().getUserName());

        GregorianCalendar calendar = new GregorianCalendar();
        b_year = calendar.get(Calendar.YEAR);
        b_month = calendar.get(Calendar.MONTH) + 1;
        b_day = calendar.get(Calendar.DAY_OF_MONTH);

        birthday_edit.setOnClickListener(this);
        first_address.setOnClickListener(this);
        address_num.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        terms_detail1.setOnClickListener(this);
        terms_detail2.setOnClickListener(this);
        terms_detail3.setOnClickListener(this);

        check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    switch (compoundButton.getId()) {
                        case R.id.check_all_terms :
                            check_terms1.setChecked(true);
                            check_terms2.setChecked(true);
                            check_terms3.setChecked(true);
                            break;
                        case R.id.check_terms1 : break;
                        case R.id.check_terms2 : break;
                        case R.id.check_terms3 : break;
                    }
                } else {
                    switch (compoundButton.getId()) {
                        case R.id.check_all_terms :
                            check_terms1.setChecked(false);
                            check_terms2.setChecked(false);
                            check_terms3.setChecked(false);
                            break;
                        case R.id.check_terms1 : break;
                        case R.id.check_terms2 : break;
                        case R.id.check_terms3 : break;
                    }
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            birthday = String.format("%d / %2d / %2d", year, monthOfYear+1, dayOfMonth);
            Log.w(TAG, birthday);

            b_year = year;
            b_month = monthOfYear + 1;
            b_day = dayOfMonth;

            birthday_edit.setText(birthday);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birthday_edit:
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, dateSetListener, b_year, b_month-1, b_day);
                datePickerDialog.show();
                break;
            case R.id.first_address:
            case R.id.address_num:
                Intent intent = new Intent(SignupActivity.this, AddressActivity.class);
                startActivityForResult(intent, 9001);
                break;
            case R.id.signup_btn:
                signup();
                break;
            case R.id.terms_detail1: showTerm(1); break;
            case R.id.terms_detail2: showTerm(2); break;
            case R.id.terms_detail3: showTerm(3); break;
        }
    }

    private void showTerm(int key) {
        String term = "";
        String title = "";
        switch (key) {
            case 1: term = term1; title = "전자금융거래 이용약관"; break;
            case 2: term = term2; title = "개인정보 수집/이용 동의 약관"; break;
            case 3: term = term3; title = "SENDU 서비스 약관"; break;
        }
        TermDialog termDialog = new TermDialog(this, term, title);

        Display d = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = d.getWidth();
        int height = d.getHeight();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(termDialog.getWindow().getAttributes());
        lp.width = (int)(width * 0.9);
        lp.height = (int)(height * 0.6);
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.6f;

        termDialog.show();
        Window window = termDialog.getWindow();
        window.setAttributes(lp);
    }

    private void signup() {
        if(birthday_edit.getText().toString().equals("")) {
            Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(first_address.getText().toString().equals("")) {
            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(second_address.getText().toString().equals("")) {
            Toast.makeText(this, "추가 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(phone_num.getText().toString().equals("")) {
            Toast.makeText(this, "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!check_terms1.isChecked() || !check_terms2.isChecked() || !check_terms3.isChecked()) {
            Toast.makeText(this, "약관을 동의해주십시요.", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignUp signUp = retrofit.create(SignUp.class);

        Call<Response> call = signUp.doSignup(mData.getUserInfo().getUserName(), "sendu1234", mData.getUserInfo().getUid(), address_num.getText().toString(), first_address.getText().toString() + " " + second_address.getText().toString(), birthday);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                finish();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.w(TAG, "Signup failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 9001 :
                first_address.setText(data.getStringExtra("address"));
                address_num.setText(data.getStringExtra("address_num"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("회원가입을 취소하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
    }
}
