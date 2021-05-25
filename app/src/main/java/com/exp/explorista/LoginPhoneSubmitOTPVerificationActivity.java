package com.exp.explorista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.exp.explorista.api.ApiService;
import com.exp.explorista.helper.RetrofitClient;
import com.exp.explorista.model.LoginPhoneSubmitOTPResendResponse;
import com.exp.explorista.model.LoginPhoneSubmitOTPVerificationResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPhoneSubmitOTPVerificationActivity extends AppCompatActivity {

    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    AppCompatButton verify_otp,resend_otp;
    Boolean isOTPValid;
    RelativeLayout rl_no_internet , rl_try_again ;
    Button btn_no_internet , btn_try_again;
    LinearLayout llShimmer , ll_otp;
    ImageView imageView;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView tv_phone_log_ver;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    Button verifyOTP;
    TextView textViewMessage;
   // EditText otpText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_submit_o_t_p_verification);
        startSmsUserConsent();
        tv_phone_log_ver = findViewById(R.id.tv_phone_login_verification);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
        String cust_phone = sharedPreferences.getString("key_phone_one","");
        tv_phone_log_ver.setText("Enter the verifcation code we just sent you on your Mobile-number "+cust_phone+".");
        imageView = findViewById(R.id.image_back_login_exist);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rl_no_internet = findViewById(R.id.rl_no_internet);
        rl_try_again = findViewById(R.id.rl_try_again);
        btn_no_internet = findViewById(R.id.btn_no_internet);
        btn_try_again = findViewById(R.id.btn_try_again);
        ll_otp = findViewById(R.id.ll_layout_customer_login_phone_otp_verification_exist_user);
        llShimmer = findViewById(R.id.ll_shimmer_customer_login_phone_otp_verification_exist_user);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_login_phone_otp_verification_exist_user);
        otp_textbox_one = findViewById(R.id.ed_otp1_exist);
        otp_textbox_two = findViewById(R.id.ed_otp2_exist);
        otp_textbox_three = findViewById(R.id.ed_otp3_exist);
        otp_textbox_four = findViewById(R.id.ed_otp4_exist);
        verify_otp = findViewById(R.id.btnOTPVerify_exist);
        resend_otp = findViewById(R.id.btnOTPResend_exist);
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerFrameLayout.startShimmer();
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        llShimmer.setVisibility(View.VISIBLE);
                        ll_otp.setVisibility(View.GONE);
                        try {
                            startSmsUserConsent();
                            otp_resend();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },0);
            }
        });


        otp_textbox_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
                if(editable.length()>0){
                    otp_textbox_one.clearFocus();
                    otp_textbox_two.requestFocus();
                    otp_textbox_three.setCursorVisible(true);
                }
            }
        });

        otp_textbox_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
                if(editable.length() > 0) {
                    otp_textbox_two.clearFocus();
                    otp_textbox_three.requestFocus();
                    otp_textbox_three.setCursorVisible(true);
                }
            }
        });

        otp_textbox_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    otp_textbox_three.clearFocus();
                    otp_textbox_four.requestFocus();
                    otp_textbox_four.setCursorVisible(true);
                }
            }
        });


        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp_textbox_one.getText().toString().isEmpty() || otp_textbox_two.getText().toString().isEmpty() || otp_textbox_three.getText().toString().isEmpty() || otp_textbox_four.getText().toString().isEmpty() ) {
                    Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    isOTPValid = false;
                } else {
                    isOTPValid = true;
                }

                if (isOTPValid) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            shimmerFrameLayout.startShimmer();
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                            llShimmer.setVisibility(View.VISIBLE);
                            ll_otp.setVisibility(View.GONE);
                            try {
                                otp_verification();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0);
                }
            }
        });
    }


    public void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                Pattern pattern = Pattern.compile("(|^)\\d{4}");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find())
                {
                    char otp_one = Objects.requireNonNull(matcher.group(0)).charAt(0);
                    char otp_two = Objects.requireNonNull(matcher.group(0)).charAt(1);
                    char otp_three = Objects.requireNonNull(matcher.group(0)).charAt(2);
                    char otp_four = Objects.requireNonNull(matcher.group(0)).charAt(3);
                    String one_fn = String.valueOf(otp_one);
                    String two_fn = String.valueOf(otp_two);
                    String three_fn = String.valueOf(otp_three);
                    String four_fn = String.valueOf(otp_four);
                    otp_textbox_one.setText(one_fn);
                    otp_textbox_two.setText(two_fn);
                    otp_textbox_three.setText(three_fn);
                    otp_textbox_four.setText(four_fn);
                    otp_verification();
//                    SharedPreferences sharedPreferences = getSharedPreferences("fn_data",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("key_otp_data",ttttt);
//                    editor.apply();
                    Toast.makeText(getApplicationContext(), otp_one+""+otp_two+""+otp_three+""+otp_four, Toast.LENGTH_LONG).show();
                }


            //    textViewMessage.setText(
                ///        String.format("%s - %s", getString(R.string.received_message), message));
      //          getOtpFromMessage(message);
            }
        }
    }
//    public void getOtpFromMessage(String message) {
//        // This will match any 6 digit number in the message
//        Pattern pattern = Pattern.compile("(|^)\\d{4}");
//        Matcher matcher = pattern.matcher(message);
//        if (matcher.find()) {
//
//            String cust_otp = otp_textbox_one.getText().toString().trim()+otp_textbox_two.getText().toString().trim()+otp_textbox_three.getText().toString().trim()+otp_textbox_four.getText().toString().trim();
//
//            String s_fn =  matcher.group(0).toString();
//            char dddddd = Objects.requireNonNull(matcher.group(0)).charAt(0);
//            String ttttt = String.valueOf(dddddd);
//
//
//       //  assert s_fn != null;
//           // char first = Objects.requireNonNull(s_fn).charAt(0);
//           // char second = s_fn.charAt(1);
//           // char third = s_fn.charAt(2);
//           // char fourth = s_fn.charAt(3);
//            ;
         //   otp_textbox_two.setText(Objects.requireNonNull(matcher.group(0)).charAt(1));
        //    otp_textbox_three.setText(Objects.requireNonNull(matcher.group(0)).charAt(2));
        //    otp_textbox_four.setText(Objects.requireNonNull(matcher.group(0)).charAt(3));
//         //   Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//         //   Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "hii2"+s, Toast.LENGTH_SHORT).show();
//        //    otpText.setText(matcher.group(0));
//        }
//    }
    public void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }



    private void otp_resend()
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
        {
            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
            String cust_phone = sharedPreferences.getString("key_phone_one","");
            SharedPreferences sharedPreferences11 = getSharedPreferences("fn_data",MODE_PRIVATE);
            String cust_otp_fn = sharedPreferences11.getString("key_otp_data","");
            Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, cust_otp_fn, Toast.LENGTH_SHORT).show();

            ApiService api = RetrofitClient.getApiService();
            Call<LoginPhoneSubmitOTPResendResponse> call = api.getLoginPhoneSubmitOTPResend(cust_phone);
            call.enqueue(new Callback<LoginPhoneSubmitOTPResendResponse>() {
                @Override
                public void onResponse(Call<LoginPhoneSubmitOTPResendResponse> call, Response<LoginPhoneSubmitOTPResendResponse> response) {



                     if (response.body().getMessageResponse().equals("otp updated"))
                    {

                       // Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "OTP Updated", Toast.LENGTH_SHORT).show();
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }
                    else if(response.body().getMessageResponse().equals("otp not updated"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "OTP Not Updated", Toast.LENGTH_SHORT).show();
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if(response.body().getMessageResponse().equals("invalid method"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "Invalid Method", Toast.LENGTH_SHORT).show();
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<LoginPhoneSubmitOTPResendResponse> call, Throwable t) {
                    shimmerFrameLayout.stopShimmer();
                    llShimmer.setVisibility(View.GONE);
                    ll_otp.setVisibility(View.GONE);
                    rl_try_again.setVisibility(View.VISIBLE);
                    btn_try_again.setOnClickListener(v -> {
                        shimmerFrameLayout.startShimmer();
                        llShimmer.setVisibility(View.VISIBLE);
                        rl_try_again.setVisibility(View.GONE);
                        Handler handler1 = new Handler();
                        handler1.postDelayed(() -> otp_resend(), 0);
                    });
                }
            });
        }
        else
        {

            shimmerFrameLayout.stopShimmer();
            llShimmer.setVisibility(View.GONE);
            ll_otp.setVisibility(View.GONE);
            rl_no_internet.setVisibility(View.VISIBLE);
            btn_no_internet.setVisibility(View.VISIBLE);
            btn_no_internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shimmerFrameLayout.startShimmer();
                    llShimmer.setVisibility(View.VISIBLE);
                    btn_no_internet.setVisibility(View.GONE);
                    rl_no_internet.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            otp_resend();
                        }
                    },500);
                }
            });
        }


    }

    private void otp_verification() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
        {
            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
            String cust_phone = sharedPreferences.getString("key_phone_one","");
            SharedPreferences sharedPreferences11 = getSharedPreferences("fn_data",MODE_PRIVATE);
            String otp_datasss = sharedPreferences11.getString("key_otp_data","");
            Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, otp_datasss, Toast.LENGTH_SHORT).show();
            String cust_otp = otp_textbox_one.getText().toString().trim()+otp_textbox_two.getText().toString().trim()+otp_textbox_three.getText().toString().trim()+otp_textbox_four.getText().toString().trim();

            ApiService api = RetrofitClient.getApiService();
            Call<LoginPhoneSubmitOTPVerificationResponse> call = api.getLoginPhoneSubmitOTPVerification(cust_phone,cust_otp);
            call.enqueue(new Callback<LoginPhoneSubmitOTPVerificationResponse>() {
                @Override
                public void onResponse(Call<LoginPhoneSubmitOTPVerificationResponse> call, Response<LoginPhoneSubmitOTPVerificationResponse> response) {



                    if (response.body().getMessageResponse().equals("otp expired"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "OTP Expired", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                   else  if (response.body().getMessageResponse().equals("otp valid exist data regd"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "OTP Matched", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_phone_one_otp_verify_data_exist",cust_phone);
                        editor.apply();
//                        try {
//                            if ((sharedPreferences.getString("key_phone_one_otp_verify_data_exist", null) != null)) {

                                Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this, DashboardActivity.class);
                                startActivity(intent);

//                            }
                  //      }
                     //   catch (Exception e){e.printStackTrace();}
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if (response.body().getMessageResponse().equals("otp valid not exist data regd"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "OTP Matched 2211", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_phone_one_otp_verify_data_not_exist",cust_phone);
                        editor.apply();
  //                      try {
    //                        if ((sharedPreferences.getString("key_phone_one", null) != null)) {

                                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                                Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this, LoginPhoneNameFinalSubmitActivity.class);
                                startActivity(intent);

      //                      }
                    //    }
                    //    catch (Exception e){e.printStackTrace();}
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if(response.body().getMessageResponse().equals("otp invalid"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "OTP Invalid", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if(response.body().getMessageResponse().equals("invalid method"))
                    {

                        Toast.makeText(LoginPhoneSubmitOTPVerificationActivity.this, "Invalid Method", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<LoginPhoneSubmitOTPVerificationResponse> call, Throwable t) {
                    shimmerFrameLayout.stopShimmer();
                    llShimmer.setVisibility(View.GONE);
                    ll_otp.setVisibility(View.GONE);
                    rl_try_again.setVisibility(View.VISIBLE);
                    btn_try_again.setOnClickListener(v -> {
                        shimmerFrameLayout.startShimmer();
                        llShimmer.setVisibility(View.VISIBLE);
                        rl_try_again.setVisibility(View.GONE);
                        Handler handler1 = new Handler();
                        handler1.postDelayed(() -> otp_verification(), 0);
                    });
                }
            });
        }
        else
        {

            shimmerFrameLayout.stopShimmer();
            llShimmer.setVisibility(View.GONE);
            ll_otp.setVisibility(View.GONE);
            rl_no_internet.setVisibility(View.VISIBLE);
            btn_no_internet.setVisibility(View.VISIBLE);
            btn_no_internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shimmerFrameLayout.startShimmer();
                    llShimmer.setVisibility(View.VISIBLE);
                    btn_no_internet.setVisibility(View.GONE);
                    rl_no_internet.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            otp_verification();
                        }
                    },500);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
        try
        {
            if((sharedPreferences.getString("key_phone_one_otp_verify_data_exist", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this, DashboardActivity.class);
                startActivity(intent);

            }
            else if((sharedPreferences.getString("key_phone_one_otp_verify_data_not_exist", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this, LoginPhoneNameFinalSubmitActivity.class);
                startActivity(intent);

            }
        }
        catch (Exception e)
        { e.printStackTrace(); }

    }

    @Override
    public void onBackPressed()
    {
            super.onBackPressed();
            Intent intent = new Intent(LoginPhoneSubmitOTPVerificationActivity.this,LoginOrSignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}