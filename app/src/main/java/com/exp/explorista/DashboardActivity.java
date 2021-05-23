package com.exp.explorista;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    GoogleSignInClient mGoogleSignInClient;
    Button btn_app_logout,btn_session_logout;
     SharedPreferences sharedPreferences_one,sharedPreferences_two,sharedPreferences_three;
    SharedPreferences.Editor editor_one ,editor_two,editor_three;
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
      //  sharedPreferences_three = getSharedPreferences("one_one", Context.MODE_PRIVATE);
        sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
        sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String cust_phone_one = sharedPreferences_one.getString("key_phone_one","");
        String cust_phone_two = sharedPreferences_one.getString("key_phone_two","");
      //  String key_one_one_one_fn = sharedPreferences_three.getString("key_one_one_one","");
      //   String uuid = sharedPreferences_two.getString("key_email","");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DashboardActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

        }

        btn_session_logout = findViewById(R.id.session_logout);
        btn_app_logout = findViewById(R.id.app_logout);
        btn_session_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(DashboardActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finishAffinity();
                                Toast.makeText(DashboardActivity.this, "Session logged out", Toast.LENGTH_SHORT).show();
                            }
                        });

                sharedPreferences_three = getSharedPreferences("one_one", MODE_PRIVATE);
                sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
                editor_one = sharedPreferences_one.edit();
                editor_three = sharedPreferences_three.edit();
                editor_three.clear();
                editor_three.apply();
                // SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                //   SharedPreferences.Editor editor = sharedPreferences.edit();
                editor_one.clear();
                editor_one.apply();
                sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, MODE_PRIVATE);
                editor_two = sharedPreferences_two.edit();
                // SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                //   SharedPreferences.Editor editor = sharedPreferences.edit();
                editor_two.clear();
                editor_two.apply();

//                try
//                {
//                    if((sharedPreferences_one.getString("key_fn", null) != null ))
//                    {
//
//                        SharedPreferences sharedPreferences111 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
//                        SharedPreferences.Editor editor111 = sharedPreferences111.edit();
//                        // SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
//                        //   SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor111.clear();
//                        editor111.apply();
//                        //      Toast.makeText(DashboardActivity.this, "Logged out current gmail account", Toast.LENGTH_SHORT).show();
//                        Intent intent = getIntent();
//                        startActivity(intent);
//                        finish();
//                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
//                        mGoogleSignInClient.signOut()
//                                .addOnCompleteListener(DashboardActivity.this, new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        // ...
//
//                                    }
//                                });
//
//
//                    }
//                     if((sharedPreferences_one.getString("key_login_google_email_user_logged", null) != null )) {
//
//
//                        mGoogleSignInClient.signOut()
//                                .addOnCompleteListener(DashboardActivity.this, new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        // ...
//                                        Toast.makeText(DashboardActivity.this, "hiii", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                    }
//
//                }
//                catch (Exception e)
//                { e.printStackTrace(); }



            }


            //      finishAffinity();
        });


        btn_app_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DashboardActivity.this, "App Logged Out", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });
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