package com.exp.explorista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.exp.explorista.api.ApiService;
import com.exp.explorista.helper.RetrofitClient;
import com.exp.explorista.model.LoginGoogleCheckUserResponse;
import com.exp.explorista.model.LoginPhoneSubmitResponse;
import com.exp.explorista.model.RegistrationPhoneSubmitResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOrSignUpActivity extends AppCompatActivity
{

     GoogleSignInClient mGoogleSignInClient;
     private  static  int RC_SIGN_IN  = 400;
     LinearLayout ll_login ,ll_sign_up,ll_l_p_t,ll_phone_login,ll_phone_sign_up,ll_name_sign_up;
     AppCompatButton btn_login_submit,btn_sign_up_submit;
     AppCompatTextView login , sign_up;
     RelativeLayout relativeLayout;
     EditText et_reg_phone,et_log_phone;
     Boolean isPhoneValid ;
     RelativeLayout rl_no_internet , rl_try_again , rl_one ;
     Button  btn_no_internet , btn_try_again;
     ImageView imageView_one ,imageView_two,imageView_google;
     TextView tv_one ,tv_two,tv_phone_login,tv_name_sign_up,tv_phone_sign_up,tv_skip;
     LinearLayout llShimmer;
     ShimmerFrameLayout shimmerFrameLayout;
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);
        tv_skip = findViewById(R.id.login_or_sign_up_tv_skip);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(PREF_UNIQUE_ID,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key_uuid",uniqueID);
                editor.apply();
                shimmerFrameLayout.startShimmer();
                llShimmer.setVisibility(View.VISIBLE);
                rl_one.setVisibility(View.GONE);
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);
                        rl_one.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginOrSignUpActivity.this,LoginLocationActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginOrSignUpActivity.this, uniqueID, Toast.LENGTH_SHORT).show();
                    }
                },1000);

            }
        });
        imageView_google = findViewById(R.id.img_google);
        imageView_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        rl_one = findViewById(R.id.login_or_sign_up_rl_one);
        rl_no_internet = findViewById(R.id.rl_no_internet);
        rl_try_again = findViewById(R.id.rl_try_again);
        btn_no_internet = findViewById(R.id.btn_no_internet);
        btn_try_again = findViewById(R.id.btn_try_again);
        llShimmer = findViewById(R.id.ll_shimmer_customer_login_or_sign_up);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_login_or_sign_up);
        et_reg_phone = findViewById(R.id.txt_sign_up_customer_phone_number);
        et_log_phone = findViewById(R.id.txt_login_customer_phone_number);
        btn_login_submit = findViewById(R.id.btnLoginCustomerSubmit);
        btn_sign_up_submit = findViewById(R.id.btnSignUpCustomerSubmit);
        ll_phone_login = findViewById(R.id.ll_phone_login);
        ll_phone_sign_up = findViewById(R.id.ll_phone_sign_up);
        tv_phone_login = findViewById(R.id.tv_phone_login);
        tv_phone_sign_up = findViewById(R.id.tv_phone_sign_up);
        tv_one = findViewById(R.id.continue_with_one);
        tv_two = findViewById(R.id.continue_with_two);
        imageView_google = findViewById(R.id.img_google);
        ll_l_p_t = findViewById(R.id.ll_image_plus_text);
        imageView_one = findViewById(R.id.img_facebook_one);
        imageView_two = findViewById(R.id.img_facebook_two);
        relativeLayout = findViewById(R.id.login_or_sign_up_rl_one);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            relativeLayout.setBackgroundResource(R.drawable.bg_fn);
            tv_one.setVisibility(View.GONE);
            tv_two.setVisibility(View.VISIBLE);
            imageView_one.setVisibility(View.GONE);
            imageView_two.setVisibility(View.VISIBLE);
        },800);

        Animation animFadein_fd = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_up_in);
        ll_l_p_t.startAnimation(animFadein_fd);
         login = findViewById(R.id.tv_customer_login);
         sign_up = findViewById(R.id.tv_customer_sign_up);
         ll_login = findViewById(R.id.login_visibility);
         ll_sign_up = findViewById(R.id.sign_up_visibility);
        Animation animFadein_f = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        login.startAnimation(animFadein_f);
        login.setOnClickListener(v ->
        {
               ll_sign_up.setVisibility(View.GONE);
               login.setBackgroundResource(R.drawable.login_or_sign_up_ctm_bg_tv);
               login.startAnimation(animFadein_f);
               sign_up.startAnimation(animFadein_f);
               login.setTextColor(Color.parseColor("#FFFFFF"));
               sign_up.setTextColor(Color.parseColor("#000000"));
               sign_up.setBackgroundResource(0);
               ll_login.setVisibility(View.VISIBLE);
         });

        Animation animFadein_s = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        sign_up.startAnimation(animFadein_s);
        sign_up.setOnClickListener(v ->
        {
             ll_sign_up.setVisibility(View.VISIBLE);
             login.setBackgroundResource(0);
             sign_up.startAnimation(animFadein_s);
             login.startAnimation(animFadein_s);
             login.setTextColor(Color.parseColor("#000000"));
             sign_up.setTextColor(Color.parseColor("#FFFFFF"));
             sign_up.setBackgroundResource(R.drawable.login_or_sign_up_ctm_bg_tv);
             ll_login.setVisibility(View.GONE);

         });

//        EditText state = findViewById(R.id.txt_sign_up_customer_name);
//        state.setFilters(new InputFilter[] {
//                (cs, start, end, spanned, dStart, dEnd) -> {
//                    // TODO Auto-generated method stub
//                    if(cs.equals("")){ // for backspace
//                        return cs;
//                    }
//                    if(cs.toString().matches("[a-zA-Z ]+")){
//                        return cs;
//                    }
//                    return "";
//                }
//        });

        btn_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_log_phone.getText().toString().isEmpty())
                {
                    et_log_phone.setError("Mobile Number is required");
                    isPhoneValid = false;
                }
                else if (!Patterns.PHONE.matcher(et_log_phone.getText().toString()).matches())
                {
                    et_log_phone.setError("Mobile Number Invalid");
                    isPhoneValid = false;
                }
                else if (et_log_phone.getText().toString().length() != 10)
                {
                    et_log_phone.setError("Mobile Number should be 10 digit!");
                    isPhoneValid = false;
                }
                else
                {
                    isPhoneValid = true;
                }

                if(isPhoneValid)
                {
                    shimmerFrameLayout.startShimmer();
                    llShimmer.setVisibility(View.VISIBLE);
                    rl_one.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            data_login();
                        }
                    },0);
                }
            }
        });


        btn_sign_up_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reg_phone.getText().toString().isEmpty())
                {
                    et_reg_phone.setError("Mobile Number is required");
                    isPhoneValid = false;
                }
                else if (!Patterns.PHONE.matcher(et_reg_phone.getText().toString()).matches())
                {
                    et_reg_phone.setError("Mobile Number Invalid");
                    isPhoneValid = false;
                }
                else if (et_reg_phone.getText().toString().length() != 10)
                {
                    et_reg_phone.setError("Mobile Number should be 10 digit!");
                    isPhoneValid = false;
                }
                else
                {
                    isPhoneValid = true;
                }

                if(isPhoneValid)
                {
                    shimmerFrameLayout.startShimmer();
                    llShimmer.setVisibility(View.VISIBLE);
                    rl_one.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            data_register();
                        }
                    },0);
                }
            }
        });
    }

//    public synchronized static String id(Context context) {
//        if (uniqueID == null) {
//            SharedPreferences sharedPrefs = context.getSharedPreferences(
//                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
//            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
//            if (uniqueID == null) {
//                uniqueID = UUID.randomUUID().toString();
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putString(PREF_UNIQUE_ID, uniqueID);
//                editor.apply();
//            }
//        }
//            }return uniqueID;
////



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key_login_google_email",personEmail);
                editor.putString("key_login_google_name",personName);
                editor.apply();
                Toast.makeText(LoginOrSignUpActivity.this, "world world world "+personEmail, Toast.LENGTH_SHORT).show();
            }
             google_login_status();
              //  startActivity(new Intent(LoginOrSignUpActivity.this, LoginGoogleDetailActivity.class));


        } catch (ApiException e) {

            Log.d("message",e.toString());

        }
    }

    private void google_login_status() {
        shimmerFrameLayout.startShimmer();
        llShimmer.setVisibility(View.VISIBLE);
        rl_one.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
            {
                SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                String cust_name = sharedPreferences.getString("key_login_google_name","");
                String cust_email = sharedPreferences.getString("key_login_google_email","");
                final ApiService service = RetrofitClient.getApiService();
                Call<LoginGoogleCheckUserResponse> call = service.getLoginGoogleCheckUser(cust_email);
                call.enqueue(new Callback<LoginGoogleCheckUserResponse>() {
                    @Override
                    public void onResponse(Call<LoginGoogleCheckUserResponse> call, Response<LoginGoogleCheckUserResponse> response)
                    {


                        if(response.body().getMessageResponse().equals("user logged in"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_login_google_email_user_logged",cust_email);
                            editor.putString("key_login_google_name_user_logged",cust_name);
                            editor.apply();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if(response.body().getMessageResponse().equals("user not exist"))
                        {
                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_login_google_email_user_not_exist",cust_email);
                            editor.putString("key_login_google_name_user_not_exist",cust_name);
                            editor.putString("key_value_three","1");
                            editor.apply();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, LoginGoogleDetailActivity.class);
                            startActivity(intent);
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }


                        else if (response.body().getMessageResponse().equals("invalid method"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginGoogleCheckUserResponse> call, Throwable t)
                    {
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);
                        rl_one.setVisibility(View.GONE);
                        rl_try_again.setVisibility(View.VISIBLE);
                        btn_try_again.setOnClickListener(v -> {
                            shimmerFrameLayout.startShimmer();
                            llShimmer.setVisibility(View.VISIBLE);
                            rl_try_again.setVisibility(View.GONE);
                            Handler handler1 = new Handler();
                              handler1.postDelayed(() -> google_login_status(), 0);
                        });
                    }
                });
            }

            else
            {
                shimmerFrameLayout.stopShimmer();
                llShimmer.setVisibility(View.GONE);
                rl_one.setVisibility(View.GONE);
                rl_no_internet.setVisibility(View.VISIBLE);
                btn_no_internet.setVisibility(View.VISIBLE);
                btn_no_internet.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        shimmerFrameLayout.startShimmer();
                        llShimmer.setVisibility(View.VISIBLE);
                        btn_no_internet.setVisibility(View.GONE);
                        rl_no_internet.setVisibility(View.GONE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                      google_login_status();
                            }
                        },500);
                    }
                });
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void data_login() {


        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
            {
                final String cust_phone = et_log_phone.getText().toString();
               // Toast.makeText(this, cust_phone, Toast.LENGTH_SHORT).show();
                final ApiService service = RetrofitClient.getApiService();
                Call<LoginPhoneSubmitResponse> call = service.getLoginPhoneSubmit(cust_phone);
                call.enqueue(new Callback<LoginPhoneSubmitResponse>() {
                    @Override
                    public void onResponse(Call<LoginPhoneSubmitResponse> call, Response<LoginPhoneSubmitResponse> response)
                    {

                   //     assert response.body() != null;

                      //  assert response.body() != null;



                         if(response.body().getMessageResponse().equals("mn existing customer regd otp updated"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_phone_one",et_log_phone.getText().toString());

                            editor.apply();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, LoginPhoneSubmitOTPVerificationActivity.class);
                            startActivity(intent);
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if(response.body().getMessageResponse().equals("mn existing customer regd otp not updated"))
                        {
                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp updated"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_phone_one",et_log_phone.getText().toString());
                            editor.apply();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, LoginPhoneSubmitOTPVerificationActivity.class);
                            startActivity(intent);
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp not updated"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp inserted"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_phone_one",et_log_phone.getText().toString());
                            editor.apply();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, LoginPhoneSubmitOTPVerificationActivity.class);
                            startActivity(intent);
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp not inserted"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }

                        else if (response.body().getMessageResponse().equals("invalid method"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_log_phone.setText("");

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginPhoneSubmitResponse> call, Throwable t)
                    {
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);
                        rl_one.setVisibility(View.GONE);
                        rl_try_again.setVisibility(View.VISIBLE);
                        btn_try_again.setOnClickListener(v -> {
                            shimmerFrameLayout.startShimmer();
                            llShimmer.setVisibility(View.VISIBLE);
                            rl_try_again.setVisibility(View.GONE);
                            Handler handler1 = new Handler();
                          handler1.postDelayed(() -> data_login(), 0);
                        });
                    }
                });
            }

            else
            {
                shimmerFrameLayout.stopShimmer();
                llShimmer.setVisibility(View.GONE);
                rl_one.setVisibility(View.GONE);
                rl_no_internet.setVisibility(View.VISIBLE);
                btn_no_internet.setVisibility(View.VISIBLE);
                btn_no_internet.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        shimmerFrameLayout.startShimmer();
                        llShimmer.setVisibility(View.VISIBLE);
                        btn_no_internet.setVisibility(View.GONE);
                        rl_no_internet.setVisibility(View.GONE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                data_login();
                            }
                        },500);
                    }
                });
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void data_register() {

        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
            {
                final String cust_phone = et_reg_phone.getText().toString();
               // Toast.makeText(this, cust_phone, Toast.LENGTH_SHORT).show();
                final ApiService service = RetrofitClient.getApiService();
                Call<RegistrationPhoneSubmitResponse> call = service.getRegistrationPhoneSubmit(cust_phone);
                call.enqueue(new Callback<RegistrationPhoneSubmitResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationPhoneSubmitResponse> call, Response<RegistrationPhoneSubmitResponse> response) {

                       if(response.body().getMessageResponse().equals("mn existing customer regd otp updated"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, RegistrationPhoneSubmitOTPVerificationActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_phone_two",et_reg_phone.getText().toString());
                            editor.apply();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                        else if(response.body().getMessageResponse().equals("mn existing customer regd otp not updated")){
                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp updated"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, RegistrationPhoneSubmitOTPVerificationActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_phone_two",et_reg_phone.getText().toString());
                            editor.apply();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp not updated"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp inserted"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginOrSignUpActivity.this, RegistrationPhoneSubmitOTPVerificationActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_phone_two",et_reg_phone.getText().toString());
                            editor.apply();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp not inserted"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("invalid method"))
                        {

                            Toast.makeText(LoginOrSignUpActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_reg_phone.setText("");

                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationPhoneSubmitResponse> call, Throwable t)
                    {
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);
                        rl_one.setVisibility(View.GONE);
                        rl_try_again.setVisibility(View.VISIBLE);
                        btn_try_again.setOnClickListener(v -> {
                            shimmerFrameLayout.startShimmer();
                            llShimmer.setVisibility(View.VISIBLE);
                            rl_try_again.setVisibility(View.GONE);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(() -> data_register(), 0);
                        });
                    }
                });
            }
            else {
                shimmerFrameLayout.stopShimmer();
                llShimmer.setVisibility(View.GONE);
                rl_one.setVisibility(View.GONE);
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
                                data_register();
                            }
                        },500);
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
        SharedPreferences sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, MODE_PRIVATE);
        SharedPreferences sharedPreferences_three = getSharedPreferences("one_one", MODE_PRIVATE);
        try
        {
            if((sharedPreferences.getString("key_phone_one", null) != null ))
            {

               //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginOrSignUpActivity.this, LoginPhoneSubmitOTPVerificationActivity.class);
                startActivity(intent);

            }
           else  if((sharedPreferences.getString("key_phone_two", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginOrSignUpActivity.this, RegistrationPhoneSubmitOTPVerificationActivity.class);
                startActivity(intent);

            }

            else  if((sharedPreferences.getString("key_login_google_email_user_logged", null) != null ))
            {

                Intent intent = new Intent(LoginOrSignUpActivity.this, DashboardActivity.class);
                startActivity(intent);

            }


            else  if((sharedPreferences.getString("key_value_three", null) != null ))
            {

                Intent intent = new Intent(LoginOrSignUpActivity.this, LoginGoogleDetailActivity.class);
                startActivity(intent);


            }

            else  if((sharedPreferences_three.getString("key_one_one_one", null) != null ))
            {

                Intent intent = new Intent(LoginOrSignUpActivity.this, LoginGoogleLocationActivity.class);
                startActivity(intent);


            }

            //
            else  if((sharedPreferences_two.getString("key_uuid", null) != null ))
            {
                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginOrSignUpActivity.this, LoginLocationActivity.class);
                startActivity(intent);

            }

        }
        catch (Exception e)
        { e.printStackTrace(); }
    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            //  finishAffinity(); finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                //  finishAffinity();

                // finish();
            }
        }, 2000);
    }
}