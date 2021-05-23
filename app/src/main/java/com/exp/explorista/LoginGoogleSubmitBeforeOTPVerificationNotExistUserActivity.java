package com.exp.explorista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.exp.explorista.api.ApiService;
import com.exp.explorista.helper.RetrofitClient;
import com.exp.explorista.model.LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse;
import com.exp.explorista.model.LoginPhoneSubmitOTPResendResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity extends AppCompatActivity {

   // GoogleSignInClient mGoogleSignInClient;
    private final static int RESOLVE_HINT = 1011;
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    AppCompatButton verify_otp,resend_otp;
    Boolean isOTPValid;
    RelativeLayout rl_no_internet , rl_try_again ;
    Button btn_no_internet , btn_try_again;
    LinearLayout llShimmer , ll_otp;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView imageView;
    GoogleSignInClient mGoogleSignInClient;
    TextView tv_phone_log_google_ver;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google_submit_before_o_t_p_verification_not_exist_user);
        startSmsUserConsent();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //   SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
        //  SharedPreferences.Editor editor = sharedPreferences.edit();

        // editor.apply();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        //    tv_name_login_google.setText(personName);
          //  tv_gmail_login_google.setText(personEmail);
            //  editor.putString("key_name_google",personName);
            //  editor.putString("key_email_google",personEmail);
            //    editor.putString("key_phone_one",et_phone_login_google.getText().toString());
            //  editor.apply();
        }
        tv_phone_log_google_ver = findViewById(R.id.tv_phone_login_google_verification);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
        String cust_phone = sharedPreferences.getString("key_phone_three","");
        tv_phone_log_google_ver.setText("Enter the verifcation code we just sent you on your Mobile-number "+cust_phone+".");
        imageView = findViewById(R.id.image_back_login_google_not_exist);
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
        ll_otp = findViewById(R.id.ll_layout_customer_login_google_submit_before_otp_verification_not_exist_user);
        llShimmer = findViewById(R.id.ll_shimmer_customer_login_google_submit_before_otp_verification_not_exist_user);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_login_google_submit_before_otp_verification_not_exist_user);
        otp_textbox_one = findViewById(R.id.ed_otp1_login_google_not_exist);
        otp_textbox_two = findViewById(R.id.ed_otp2_login_google_not_exist);
        otp_textbox_three = findViewById(R.id.ed_otp3_login_google_not_exist);
        otp_textbox_four = findViewById(R.id.ed_otp4_login_google_not_exist);
        verify_otp = findViewById(R.id.btnLoginGoogleSubmitOTPVerify);
        resend_otp = findViewById(R.id.btnLoginGoogleSubmitOTPResend);
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
                            otp_resend();
                            startSmsUserConsent();
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
                    Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
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
            String cust_phone = sharedPreferences.getString("key_phone_three","");

            ApiService api = RetrofitClient.getApiService();
            Call<LoginPhoneSubmitOTPResendResponse> call = api.getLoginPhoneSubmitOTPResend(cust_phone);
            call.enqueue(new Callback<LoginPhoneSubmitOTPResendResponse>() {
                @Override
                public void onResponse(Call<LoginPhoneSubmitOTPResendResponse> call, Response<LoginPhoneSubmitOTPResendResponse> response) {

                    assert response.body() != null;
                    if (response.body().getMessageResponse().equals("otp updated"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "OTP Updated", Toast.LENGTH_SHORT).show();
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }
                    else if(response.body().getMessageResponse().equals("otp not updated"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "OTP Not Updated", Toast.LENGTH_SHORT).show();
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if(response.body().getMessageResponse().equals("invalid method"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Invalid Method", Toast.LENGTH_SHORT).show();
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
            String cust_phone = sharedPreferences.getString("key_login_google_phone_fn","");
            String cust_name = sharedPreferences.getString("key_login_google_name_user_not_exist","");
            String cust_email = sharedPreferences.getString("key_email_google","");
            String unique_two = sharedPreferences.getString("key_value_three","");
            String cust_otp = otp_textbox_one.getText().toString().trim()+otp_textbox_two.getText().toString().trim()+otp_textbox_three.getText().toString().trim()+otp_textbox_four.getText().toString().trim();

            ApiService api = RetrofitClient.getApiService();
            Call<LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse> call = api.getLoginGoogleSubmitBeforeOTPVerificationNotExistUser(cust_name,cust_email,cust_phone,cust_otp);
            call.enqueue(new Callback<LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse>() {
                @Override
                public void onResponse(Call<LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse> call, Response<LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse> response) {

                    if (response.body().getMessageResponse().equals("user already exist"))
                    {
                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "111111111", Toast.LENGTH_SHORT).show();

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Data success 123", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this,DashboardActivity.class);
                        startActivity(intent);
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_fn_dashboard_email",cust_email);
                        editor.putString("key_login_google_phone_fn_dashboard",cust_phone);
                        editor.putString("key_value_three_fn_dashboard",unique_two);
                        editor.apply();
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }


                    else if (response.body().getMessageResponse().equals("not mn existing customer regd google data updated"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Data success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_fn_google_location_email",cust_email);
                        editor.putString("key_login_google_phone_fn_location",cust_phone);
                        editor.putString("key_value_three_fn_location",unique_two);
                        editor.apply();
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this,LoginGoogleLocationActivity.class);
                        startActivity(intent);
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if (response.body().getMessageResponse().equals("not mn existing customer regd google data not updated"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Data not success", Toast.LENGTH_SHORT).show();
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }


                   else if (response.body().getMessageResponse().equals("not mn existing customer regd google data inserted"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Data success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_fn_google_location_email",cust_email);
                        editor.putString("key_login_google_phone_fn_location",cust_phone);
                        editor.putString("key_value_three_fn_location",unique_two);
                        editor.apply();
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this,LoginGoogleLocationActivity.class);
                        startActivity(intent);
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText("");
                        otp_textbox_four.setText("");
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if (response.body().getMessageResponse().equals("not mn existing customer regd google data not inserted"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Data not success", Toast.LENGTH_SHORT).show();
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

                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "OTP Invalid", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        otp_textbox_one.setText("");
                        otp_textbox_two.setText("");
                        otp_textbox_three.setText(null);
                        otp_textbox_four.setText("");
                        otp_textbox_one.setCursorVisible(true);
                        ll_otp.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if(response.body().getMessageResponse().equals("invalid method"))
                    {

                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Invalid Method", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "222222222", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse> call, Throwable t) {
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
        SharedPreferences sharedPreferences_three = getSharedPreferences("one_one", MODE_PRIVATE);
        try
        {
            if((sharedPreferences.getString("key_value_three_fn_location", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, LoginGoogleLocationActivity.class);
                startActivity(intent);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

            }
            else  if((sharedPreferences.getString("key_value_three_fn_dashboard", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, DashboardActivity.class);
                startActivity(intent);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "99999999999", Toast.LENGTH_SHORT).show();

            }

            else  if((sharedPreferences_three.getString("key_one_one_one", null) != null ))
            {

                Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, LoginGoogleLocationActivity.class);
                startActivity(intent);


            }

        }
        catch (Exception e)
        { e.printStackTrace(); }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        Intent intent = new Intent(LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.this,LoginOrSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}