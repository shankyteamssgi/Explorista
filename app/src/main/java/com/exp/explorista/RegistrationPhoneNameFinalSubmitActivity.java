package com.exp.explorista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.exp.explorista.api.ApiService;
import com.exp.explorista.helper.RetrofitClient;
import com.exp.explorista.model.RegistrationPhoneNameFinalSubmitResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPhoneNameFinalSubmitActivity extends AppCompatActivity
{

    EditText et_reg_name;
    AppCompatButton btnPhoneNameFinal;
    Boolean isNameValid;
    RelativeLayout rl_no_internet , rl_try_again ,rl_phone_name_final;
    Button btn_no_internet , btn_try_again;
    LinearLayout llShimmer , ll_phone_name_final;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView tv_reg_phone;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_phone_name_final_submit);
        imageView = findViewById(R.id.image_back_registration_phone_name_final_submit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
            }
        });
        rl_no_internet = findViewById(R.id.rl_no_internet);
        rl_try_again = findViewById(R.id.rl_try_again);
        btn_no_internet = findViewById(R.id.btn_no_internet);
        btn_try_again = findViewById(R.id.btn_try_again);
        rl_phone_name_final = findViewById(R.id.rl_one_registration_phone_name_final_submit);
        llShimmer = findViewById(R.id.ll_shimmer_customer_registration_phone_name_final_submit);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_customer_registration_phone_name_final_submit);
        et_reg_name = findViewById(R.id.txt_name_registration_phone_name_final_submit);
        tv_reg_phone = findViewById(R.id.tv_phone_customer_registration_phone_name_final_submit);
        btnPhoneNameFinal = findViewById(R.id.btnRegistrationPhoneNameFinalSubmit);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
        String cust_phone = sharedPreferences.getString("key_phone_two","");
        tv_reg_phone.setText(cust_phone);

        btnPhoneNameFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_reg_name.getText().toString().isEmpty() ) {
                    et_reg_name.setError("Name is required");
                    isNameValid = false;
                } else {
                    isNameValid = true;
                }

                if (isNameValid) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            shimmerFrameLayout.startShimmer();
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                            llShimmer.setVisibility(View.VISIBLE);
                            rl_phone_name_final.setVisibility(View.GONE);
                            try {
                                phone_name_final_submit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0);
                }
            }
        });



    }

    private void phone_name_final_submit() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED || (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED  && Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) )
        {
            SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, Context.MODE_PRIVATE);
            String cust_phone = sharedPreferences.getString("key_phone_two","");
            String cust_name = et_reg_name.getText().toString().trim();

            ApiService api = RetrofitClient.getApiService();
            Call<RegistrationPhoneNameFinalSubmitResponse> call = api.getRegistrationPhoneNameFinalSubmit(cust_name,cust_phone);
            call.enqueue(new Callback<RegistrationPhoneNameFinalSubmitResponse>() {
                @Override
                public void onResponse(Call<RegistrationPhoneNameFinalSubmitResponse> call, Response<RegistrationPhoneNameFinalSubmitResponse> response) {

                    if (response.body().getMessageResponse().equals("customer data inserted"))
                    {

                        Toast.makeText(RegistrationPhoneNameFinalSubmitActivity.this, "Data Success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_phone_two_name_submit",cust_phone);
                        editor.apply();
                        //    try {
                        //      if ((sharedPreferences.getString("key_phone_one", null) != null)) {

                        //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                        Intent intent = new Intent(RegistrationPhoneNameFinalSubmitActivity.this, LoginLocationActivity.class);
                        startActivity(intent);

                        //     }
                        //  }
                        //  catch (Exception e){e.printStackTrace();}
                        et_reg_name.setText("");
                        rl_phone_name_final.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }
                    else if(response.body().getMessageResponse().equals("customer data not inserted"))
                    {

                        Toast.makeText(RegistrationPhoneNameFinalSubmitActivity.this, "Data not success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(RegistrationPhoneNameFinalSubmitActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        et_reg_name.setText("");
                        rl_phone_name_final.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }

                    else if(response.body().getMessageResponse().equals("invalid method"))
                    {

                        Toast.makeText(RegistrationPhoneNameFinalSubmitActivity.this, "Invalid Method", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationPhoneNameFinalSubmitActivity.this,LoginOrSignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        et_reg_name.setText("");
                        rl_phone_name_final.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        llShimmer.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<RegistrationPhoneNameFinalSubmitResponse> call, Throwable t) {
                    shimmerFrameLayout.stopShimmer();
                    llShimmer.setVisibility(View.GONE);
                    rl_phone_name_final.setVisibility(View.GONE);
                    rl_try_again.setVisibility(View.VISIBLE);
                    btn_try_again.setOnClickListener(v -> {
                        shimmerFrameLayout.startShimmer();
                        llShimmer.setVisibility(View.VISIBLE);
                        rl_try_again.setVisibility(View.GONE);
                        Handler handler1 = new Handler();
                        handler1.postDelayed(() -> phone_name_final_submit(), 0);
                    });
                }
            });
        }
        else {

            shimmerFrameLayout.stopShimmer();
            llShimmer.setVisibility(View.GONE);
            rl_phone_name_final.setVisibility(View.GONE);
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
                            phone_name_final_submit();
                        }
                    },500);
                }
            });
        }

    }
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(SharedConfig.mypreference, MODE_PRIVATE);
        try
        {
            if((sharedPreferences.getString("key_phone_two_name_submit", null) != null ))
            {

                //  Intent intent1 = new Intent(LoginOrSignUpActivity.this,LoginPhoneOTPVerificationNotExistUserActivity.class);
                Intent intent = new Intent(RegistrationPhoneNameFinalSubmitActivity.this, LoginLocationActivity.class);
                startActivity(intent);

            }

        }
        catch (Exception e)
        { e.printStackTrace(); }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RegistrationPhoneNameFinalSubmitActivity.this,LoginOrSignUpActivity.class);
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