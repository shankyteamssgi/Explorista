package com.exp.explorista;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.exp.explorista.api.ApiService;
import com.exp.explorista.helper.RetrofitClient;
import com.exp.explorista.model.CheckOTPExpirationResponse;
import com.exp.explorista.model.LoginGoogleSubmitResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginGoogleDetailActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {

    GoogleSignInClient mGoogleSignInClient;
    private final static int RESOLVE_HINT = 1011;
    String mobNumber;
    LinearLayout ll_login ,ll_sign_up,ll_l_p_t,ll_phone_login,ll_phone_sign_up,ll_name_sign_up;
    AppCompatButton btn_login_submit,btn_sign_up_submit,btn_login_google_submit;
    AppCompatTextView login , sign_up;
    RelativeLayout relativeLayout;
    EditText et_reg_phone,et_log_phone,et_phone_login_google;
    Boolean isPhoneValid ;
    RelativeLayout rl_no_internet , rl_try_again , rl_one ;
    Button btn_no_internet , btn_try_again;
    ImageView imageView_one ,imageView_two,imageView_google,image_view_back_login_google_detail;
    TextView tv_one ,tv_two,tv_phone_login,tv_name_sign_up,tv_phone_sign_up;
    TextView tv_name_login_google , tv_gmail_login_google;
    LinearLayout llShimmer,get_numbers,ll_back_login_google_detail;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView imageView;
    String personName,personEmail;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google_detail);

        imageView = findViewById(R.id.image_view_back_login_google_detail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        get_numbers = findViewById(R.id.get_phone_numbers);
        rl_one = findViewById(R.id.rl_one_login_google_detail);
        rl_no_internet = findViewById(R.id.rl_no_internet);
        rl_try_again = findViewById(R.id.rl_try_again);
        btn_no_internet = findViewById(R.id.btn_no_internet);
        btn_try_again = findViewById(R.id.btn_try_again);
        llShimmer = findViewById(R.id.ll_shimmer_customer_login_google_detail);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_login_google_detail);
        et_phone_login_google = findViewById(R.id.txt_phone_login_google_detail);
        et_phone_login_google.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                // Here's the key code
                requestPhoneNumber();
            }

        });
        tv_name_login_google = findViewById(R.id.tv_name_login_google_detail);
        tv_gmail_login_google = findViewById(R.id.tv_gmail_login_google_detail);
        btn_login_google_submit = findViewById(R.id.btnLoginGoogleDetailSubmit);
       // SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
      //  SharedPreferences.Editor editor = sharedPreferences.edit();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

     //   SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
      //  SharedPreferences.Editor editor = sharedPreferences.edit();

        // editor.apply();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginGoogleDetailActivity.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
             personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            tv_name_login_google.setText(personName);
            tv_gmail_login_google.setText(personEmail);
          //  editor.putString("key_name_google",personName);
          //  editor.putString("key_email_google",personEmail);
        //    editor.putString("key_phone_one",et_phone_login_google.getText().toString());
          //  editor.apply();
        }


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

        btn_login_google_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phone_login_google.getText().toString().isEmpty())
                {
                    requestPhoneNumber();
                    et_phone_login_google.setError("Mobile Number is required");
                    isPhoneValid = false;
                }
                else if (!Patterns.PHONE.matcher(et_phone_login_google.getText().toString()).matches())
                {
                    et_phone_login_google.setError("Mobile Number Invalid");
                    isPhoneValid = false;
                }
                else if (et_phone_login_google.getText().toString().length() != 10)
                {
                    et_phone_login_google.setError("Mobile Number should be 10 digit!");
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
                            data_login_google();
                        }
                    },0);
                }
            }
        });
    }


    protected void requestPhoneNumber() {

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mGoogleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    mobNumber = credential.getId();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String phone =  PhoneNumberUtils.formatNumber(mobNumber, Locale.getDefault().getCountry());
                        try {
                            // phone must begin with '+'
                            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, "");
                            int countryCode = numberProto.getCountryCode();
                            String nationalNumber = String.valueOf(numberProto.getNationalNumber());
                            et_phone_login_google.setText(nationalNumber);
                            Toast.makeText(this, nationalNumber, Toast.LENGTH_SHORT).show();
                            Log.i("code", "code " + countryCode);
                            Log.i("code", "national number " + nationalNumber);
                        } catch (NumberParseException e) {
                            System.err.println("NumberParseException was thrown: " + e.toString());
                        }
                    } else {
//Deprecated method
                        String phone =  PhoneNumberUtils.formatNumber(mobNumber);
                        try {
                            // phone must begin with '+'
                            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, "");
                            int countryCode = numberProto.getCountryCode();
                            String nationalNumber = String.valueOf(numberProto.getNationalNumber());
                            et_phone_login_google.setText(nationalNumber);
                            Toast.makeText(this, nationalNumber, Toast.LENGTH_SHORT).show();
                            Log.i("code", "code " + countryCode);
                            Log.i("code", "national number " + nationalNumber);
                        } catch (NumberParseException e) {
                            System.err.println("NumberParseException was thrown: " + e.toString());
                        }
                    }
                } else {
                    //textView.setText("No phone number available");
                    Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void data_login_google() {


        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
            {
                SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
                String cust_name = sharedPreferences.getString("key_login_google_name_user_not_exist","");
                String cust_email = sharedPreferences.getString("key_login_google_email_user_not_exist","");
                //key_login_google_email
                String cust_email_final = sharedPreferences.getString("key_login_google_email","");
                String unique_one = sharedPreferences.getString("key_value_three","");
                final String cust_phone = et_phone_login_google.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key_phone_three",et_phone_login_google.getText().toString());
                editor.apply();
                final ApiService service = RetrofitClient.getApiService();

                Call<LoginGoogleSubmitResponse> call = service.getLoginGoogleSubmit(cust_phone,cust_email);
                call.enqueue(new Callback<LoginGoogleSubmitResponse>() {
                    @Override
                    public void onResponse(Call<LoginGoogleSubmitResponse> call, Response<LoginGoogleSubmitResponse> response) {

                        if(response.body().getMessageResponse().equals("user already exist"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginOrSignUpActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_login_google_phone_fn",et_phone_login_google.getText().toString());
                            editor.putString("key_name_google",cust_name);
                            editor.putString("key_email_google",cust_email);
                            editor.putString("key_value_three_fn",unique_one);
                            editor.apply();
                            SharedPreferences sharedPreferences111 = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor111 = sharedPreferences111.edit();
                            editor111.clear();
                            editor111.apply();
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(LoginGoogleDetailActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            Toast.makeText(LoginGoogleDetailActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                            tv_name_login_google.setText(cust_name);
                            tv_gmail_login_google.setText(cust_email);

                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }


                       else if(response.body().getMessageResponse().equals("mn existing customer regd otp updated"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_login_google_phone_fn",et_phone_login_google.getText().toString());
                                editor.putString("key_name_google",cust_name);
                                editor.putString("key_email_google",cust_email);
                               editor.putString("key_value_three_fn",unique_one);
                                editor.apply();
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(LoginGoogleDetailActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            Toast.makeText(LoginGoogleDetailActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                            tv_name_login_google.setText(cust_name);
                            tv_gmail_login_google.setText(cust_email);

                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                        else if(response.body().getMessageResponse().equals("mn existing customer regd otp not updated")){
                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp updated"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_login_google_phone_fn",et_phone_login_google.getText().toString());
                            editor.putString("key_name_google",cust_name);
                            editor.putString("key_email_google",cust_email);
                            editor.putString("key_value_three_fn",unique_one);
                            editor.apply();
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(LoginGoogleDetailActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            Toast.makeText(LoginGoogleDetailActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                            tv_name_login_google.setText(cust_name);
                            tv_gmail_login_google.setText(cust_email);
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp not updated"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp inserted"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_login_google_phone_fn",et_phone_login_google.getText().toString());
                            editor.putString("key_name_google",cust_name);
                            editor.putString("key_email_google",cust_email);
                            editor.putString("key_value_three_fn",unique_one);
                            editor.apply();
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(LoginGoogleDetailActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            Toast.makeText(LoginGoogleDetailActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                            tv_name_login_google.setText(cust_name);
                            tv_gmail_login_google.setText(cust_email);

                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("not mn existing customer regd otp not inserted"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                        else if (response.body().getMessageResponse().equals("invalid method"))
                        {

                            Toast.makeText(LoginGoogleDetailActivity.this, response.body().getMessageResponse(), Toast.LENGTH_SHORT).show();
                            rl_one.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            et_phone_login_google.setText("");

                        }
                    }
                    @Override
                    public void onFailure(Call<LoginGoogleSubmitResponse> call, Throwable t) {
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);
                        rl_one.setVisibility(View.GONE);
                        rl_try_again.setVisibility(View.VISIBLE);
                        btn_try_again.setOnClickListener(v ->
                        {
                            shimmerFrameLayout.startShimmer();
                            llShimmer.setVisibility(View.VISIBLE);
                            rl_try_again.setVisibility(View.GONE);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(() -> data_login_google(), 0);
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
                                data_login_google();
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
        SharedPreferences sharedPreferences_three = getSharedPreferences("one_one", MODE_PRIVATE);
        try
        {
            if((sharedPreferences.getString("key_value_three_fn", null) != null ))
            {
                data_value_three_fn_one();
                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);


            }
            else  if((sharedPreferences_three.getString("key_one_one_one", null) != null ))
            {


                data_value_three_fn_two();
             //   Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.class);
               // startActivity(intent);


            }

        }
        catch (Exception e)
        { e.printStackTrace(); }

    }

    private void data_value_three_fn_two() {

        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
        String cust_phone = sharedPreferences.getString("key_phone_three","");

        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) ) {

                rl_one.setVisibility(View.GONE);
                shimmerFrameLayout.startShimmer();
                llShimmer.setVisibility(View.VISIBLE);
                ApiService api = RetrofitClient.getApiService();
                Call<CheckOTPExpirationResponse> call = api.getCheckOTPExpiration(cust_phone);
                call.enqueue(new Callback<CheckOTPExpirationResponse>() {
                    @Override
                    public void onResponse(Call<CheckOTPExpirationResponse> call, Response<CheckOTPExpirationResponse> response) {

                        assert response.body() != null;
                        if (response.body().getMessageResponse().equals("otp not expired")) {


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.class);
                                    startActivity(intent);

                                    rl_one.setVisibility(View.GONE);
                                    shimmerFrameLayout.startShimmer();
                                    llShimmer.setVisibility(View.VISIBLE);
                                }
                            }, 500);


                        } else if (response.body().getMessageResponse().equals("otp expired")) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                                    //  @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String personEmail_fn = sharedPreferences.getString("key_login_google_email_user_not_exist","");
                                    String personName_fn = sharedPreferences.getString("key_login_google_name_user_not_exist","");
                                    tv_name_login_google.setText(personName_fn);
                                    tv_gmail_login_google.setText(personEmail_fn);
                                    rl_one.setVisibility(View.VISIBLE);
                                    shimmerFrameLayout.stopShimmer();
                                    llShimmer.setVisibility(View.GONE);
                                    Toast.makeText(LoginGoogleDetailActivity.this, "otp expired 123", Toast.LENGTH_SHORT).show();
                                }
                            }, 500);


                        }


                    }

                    @Override
                    public void onFailure(Call<CheckOTPExpirationResponse> call, Throwable t) {

                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);
                        rl_one.setVisibility(View.GONE);
                        rl_try_again.setVisibility(View.VISIBLE);
                        btn_try_again.setOnClickListener(v -> {
                            shimmerFrameLayout.startShimmer();
                            llShimmer.setVisibility(View.VISIBLE);
                            rl_try_again.setVisibility(View.GONE);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(() -> data_value_three_fn_two(), 0);
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
                                data_value_three_fn_two();
                            }
                        },500);
                    }
                });

            }
        }catch (Exception e){e.printStackTrace();}

    }

    private void data_value_three_fn_one() {


            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
            String cust_phone = sharedPreferences.getString("key_phone_three","");

            try{
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                        Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) ) {

                    rl_one.setVisibility(View.GONE);
                    shimmerFrameLayout.startShimmer();
                    llShimmer.setVisibility(View.VISIBLE);
                    ApiService api = RetrofitClient.getApiService();
                    Call<CheckOTPExpirationResponse> call = api.getCheckOTPExpiration(cust_phone);
                    call.enqueue(new Callback<CheckOTPExpirationResponse>() {
                        @Override
                        public void onResponse(Call<CheckOTPExpirationResponse> call, Response<CheckOTPExpirationResponse> response) {

                            assert response.body() != null;
                            if (response.body().getMessageResponse().equals("otp not expired")) {


                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(LoginGoogleDetailActivity.this, LoginGoogleSubmitBeforeOTPVerificationNotExistUserActivity.class);
                                        startActivity(intent);
                                        mGoogleSignInClient.signOut()
                                                .addOnCompleteListener(LoginGoogleDetailActivity.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        // ...
                                                        Toast.makeText(LoginGoogleDetailActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                        rl_one.setVisibility(View.GONE);
                                        shimmerFrameLayout.startShimmer();
                                        llShimmer.setVisibility(View.VISIBLE);
                                    }
                                }, 500);


                            } else if (response.body().getMessageResponse().equals("otp expired")) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                                      //  @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                        String personEmail_fn = sharedPreferences.getString("key_login_google_email_user_not_exist","");
                                        String personName_fn = sharedPreferences.getString("key_login_google_name_user_not_exist","");
                                        tv_name_login_google.setText(personName_fn);
                                        tv_gmail_login_google.setText(personEmail_fn);
                                        rl_one.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout.stopShimmer();
                                        llShimmer.setVisibility(View.GONE);
                                        Toast.makeText(LoginGoogleDetailActivity.this, "otp expired 123", Toast.LENGTH_SHORT).show();
                                    }
                                }, 500);


                            }


                        }

                        @Override
                        public void onFailure(Call<CheckOTPExpirationResponse> call, Throwable t) {

                            shimmerFrameLayout.stopShimmer();
                            llShimmer.setVisibility(View.GONE);
                            rl_one.setVisibility(View.GONE);
                            rl_try_again.setVisibility(View.VISIBLE);
                            btn_try_again.setOnClickListener(v -> {
                                shimmerFrameLayout.startShimmer();
                                llShimmer.setVisibility(View.VISIBLE);
                                rl_try_again.setVisibility(View.GONE);
                                Handler handler1 = new Handler();
                                handler1.postDelayed(() -> data_value_three_fn_one(), 0);
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
                                    data_value_three_fn_one();
                                }
                            },500);
                        }
                    });

                }
            }catch (Exception e){e.printStackTrace();}


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(LoginGoogleDetailActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        Intent intent = new Intent(LoginGoogleDetailActivity.this,LoginOrSignUpActivity.class);
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