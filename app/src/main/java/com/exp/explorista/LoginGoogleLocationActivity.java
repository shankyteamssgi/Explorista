package com.exp.explorista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginGoogleLocationActivity extends AppCompatActivity {


    AppCompatButton appCompatButton;
    TextView textView;
    ImageView imageView;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences_one ,sharedPreferences_two ;
    LinearLayout llShimmer,ll_one;
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rl_no_internet , rl_try_again , rl_one ;
    Button btn_no_internet , btn_try_again;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google_location);
        ll_one = findViewById(R.id.ll_one);
        rl_no_internet = findViewById(R.id.rl_no_internet);
        rl_try_again = findViewById(R.id.rl_try_again);
        btn_no_internet = findViewById(R.id.btn_no_internet);
        btn_try_again = findViewById(R.id.btn_try_again);
        llShimmer = findViewById(R.id.ll_shimmer_customer_login_google_location);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_login_google_location);
        imageView = findViewById(R.id.image_back_login_google_location);
        sharedPreferences_one = getSharedPreferences("one_one", Context.MODE_PRIVATE);
        sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences_three = getSharedPreferences("one_one", MODE_PRIVATE);
        try
        {
            if((sharedPreferences_one.getString("key_value_three_fn_location", null) != null ))
            {

                SharedPreferences sharedPreferences_one = getSharedPreferences("one_one", MODE_PRIVATE);
                //  String cust_phone_one_fn = sharedPreferences_one.getString("key_value_three","");
                SharedPreferences.Editor editor_one = sharedPreferences_one.edit();
                editor_one.putString("key_one_one_one","5");
                editor_one.apply();
                SharedPreferences sharedPreferences222 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences222.edit();
                editor.clear();
                editor.apply();

            }

        }
        catch (Exception e)
        { e.printStackTrace(); }
//        SharedPreferences sharedPreferences222 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences222.edit();
//        editor.clear();
//        editor.apply();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //   SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
        //  SharedPreferences.Editor editor = sharedPreferences.edit();

        // editor.apply();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginGoogleLocationActivity.this);
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

     //   sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String cust_phone_one = sharedPreferences_one.getString("key_value_three","");

        //  imageView = findViewById(R.id.image_back_login_exist);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        appCompatButton = findViewById(R.id.btnUserCurrentGoogleLocation);
        textView = findViewById(R.id.tv_skip_google_location);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
                SharedPreferences.Editor editor_one = sharedPreferences_one.edit();
                editor_one.putString("key_value_three_fn_location_data",cust_phone_one);
                editor_one.apply();
                try
                {
                    if((sharedPreferences_one.getString("key_value_three_fn_location_data", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginGoogleLocationActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }

                }
                catch (Exception e)
                { e.printStackTrace(); }

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
                SharedPreferences.Editor editor_one = sharedPreferences_one.edit();
                editor_one.putString("key_value_three_fn_location_data",cust_phone_one);
                editor_one.apply();
                try
                {
                    if((sharedPreferences_one.getString("key_value_three_fn_location_data", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginGoogleLocationActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }

                }
                catch (Exception e)
                { e.printStackTrace(); }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


         SharedPreferences sharedPreferences_three = getSharedPreferences("one_one", MODE_PRIVATE);
        try
        {
            if((sharedPreferences_one.getString("key_value_three_fn_location_data", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginGoogleLocationActivity.this, DashboardActivity.class);
                startActivity(intent);

            }

        }
        catch (Exception e)
        { e.printStackTrace(); }
    }

    @Override
    public void onBackPressed() {


//       else if(sharedPreferences_one.getString("key_login_google_email", null) != null ) {
//            super.onBackPressed();
//            Intent intent = new Intent(LoginLocationActivity.this, LoginOrSignUpActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            this.finish();
       //     SharedPreferences sharedPreferences222 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
       //     SharedPreferences.Editor editor = sharedPreferences222.edit();
        //    editor.clear();
         //   editor.apply();
//
//        }




        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
             finishAffinity();
            SharedPreferences sharedPreferences_one = getSharedPreferences("one_one", MODE_PRIVATE);
          //  String cust_phone_one_fn = sharedPreferences_one.getString("key_value_three","");
            SharedPreferences.Editor editor_one = sharedPreferences_one.edit();
            editor_one.putString("key_one_one_one","5");
            editor_one.apply();
            SharedPreferences sharedPreferences222 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences222.edit();
            editor.clear();
            editor.apply();
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




    //     if(sharedPreferences.getString("key_uuid", null) != null ) {
//
//        //  super.onBackPressed();
//        shimmerFrameLayout.startShimmer();
//        llShimmer.setVisibility(View.VISIBLE);
//        ll_one.setVisibility(View.GONE);
//        shimmerFrameLayout.setVisibility(View.VISIBLE);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                shimmerFrameLayout.stopShimmer();
//                llShimmer.setVisibility(View.GONE);
//                ll_one.setVisibility(View.VISIBLE);
//                shimmerFrameLayout.setVisibility(View.GONE);
//                SharedPreferences sharedPreferences222 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences222.edit();
//                editor.clear();
//                editor.apply();
//                Intent intent = new Intent(LoginLocationActivity.this, LoginOrSignUpActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        },500);
//    }

}
