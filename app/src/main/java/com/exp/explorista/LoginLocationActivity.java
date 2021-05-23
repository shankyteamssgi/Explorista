package com.exp.explorista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginLocationActivity extends AppCompatActivity {


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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_location);
        ll_one = findViewById(R.id.ll_one);
        rl_no_internet = findViewById(R.id.rl_no_internet);
        rl_try_again = findViewById(R.id.rl_try_again);
        btn_no_internet = findViewById(R.id.btn_no_internet);
        btn_try_again = findViewById(R.id.btn_try_again);
        llShimmer = findViewById(R.id.ll_shimmer_customer_login_location);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_login_location);
        imageView = findViewById(R.id.image_back_login_location);
         sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
        sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String cust_phone_one = sharedPreferences_one.getString("key_phone_one","");
        String cust_phone_two = sharedPreferences_one.getString("key_phone_two","");
       // String cust_phone_email = sharedPreferences_one.getString("key_fn","");
        String uuid = sharedPreferences_two.getString("key_uuid","");
        Toast.makeText(this, "hello ----- "+uuid, Toast.LENGTH_SHORT).show();
      //  imageView = findViewById(R.id.image_back_login_exist);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        appCompatButton = findViewById(R.id.btnUserCurrentLocation);
        textView = findViewById(R.id.tv_skip_location);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences_one = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
                SharedPreferences sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, MODE_PRIVATE);
                SharedPreferences.Editor editor_one = sharedPreferences_one.edit();
                SharedPreferences.Editor editor_two = sharedPreferences_two.edit();
                editor_one.putString("key_phone_one_login_location",cust_phone_one);
                editor_one.putString("key_phone_two_login_location",cust_phone_two);
              //  editor_one.putString("key_login_google_email",cust_phone_email);
                editor_two.putString("key_uuid_final",uuid);
                editor_one.apply();
                editor_two.apply();
                try
                {
                    if((sharedPreferences_one.getString("key_phone_one_login_location", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }
                    else if((sharedPreferences_one.getString("key_phone_two_login_location", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }


                    else if((sharedPreferences_two.getString("key_uuid_final", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
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
                SharedPreferences sharedPreferences_two = getSharedPreferences(PREF_UNIQUE_ID, MODE_PRIVATE);
                SharedPreferences.Editor editor_one = sharedPreferences_one.edit();
                SharedPreferences.Editor editor_two = sharedPreferences_two.edit();
                editor_one.putString("key_phone_one_login_location",cust_phone_one);
                editor_one.putString("key_phone_two_login_location",cust_phone_two);
               // editor_one.putString("key_login_google_email",cust_phone_email);
                editor_two.putString("key_uuid_final",uuid);
                editor_one.apply();
                editor_two.apply();
                try
                {
                    if((sharedPreferences_one.getString("key_phone_one_login_location", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }
                    else if((sharedPreferences_one.getString("key_phone_two_login_location", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }


                    else if((sharedPreferences_two.getString("key_uuid_final", null) != null ))
                    {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
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


      //  SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
        try
        {
            if((sharedPreferences_one.getString("key_phone_one_login_location", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                startActivity(intent);

            }
            else if((sharedPreferences_one.getString("key_phone_two_login_location", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                startActivity(intent);

            }

            else if((sharedPreferences_two.getString("key_uuid_final", null) != null ))
            {
                Intent intent = new Intent(LoginLocationActivity.this, DashboardActivity.class);
                startActivity(intent);

            }
        }
        catch (Exception e)
        { e.printStackTrace(); }
    }

    @Override
    public void onBackPressed() {

        if(sharedPreferences_two.getString("key_uuid", null) != null ) {
            super.onBackPressed();
            Intent intent = new Intent(LoginLocationActivity.this, LoginOrSignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
            SharedPreferences sharedPreferences222 = getSharedPreferences(PREF_UNIQUE_ID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences222.edit();
            editor.clear();
            editor.apply();

        }
//       else if(sharedPreferences_one.getString("key_login_google_email", null) != null ) {
//            super.onBackPressed();
//            Intent intent = new Intent(LoginLocationActivity.this, LoginOrSignUpActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            this.finish();
//            SharedPreferences sharedPreferences222 = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences222.edit();
//            editor.clear();
//            editor.apply();
//
//        }


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
