package com.exp.explorista.api;

import com.exp.explorista.model.LoginGoogleCheckUserResponse;
import com.exp.explorista.model.LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse;
import com.exp.explorista.model.LoginGoogleSubmitResponse;
import com.exp.explorista.model.LoginPhoneNameFinalSubmitResponse;
import com.exp.explorista.model.LoginPhoneSubmitOTPResendResponse;
import com.exp.explorista.model.LoginPhoneSubmitOTPVerificationResponse;
import com.exp.explorista.model.LoginPhoneSubmitResponse;
import com.exp.explorista.model.RegistrationPhoneNameFinalSubmitResponse;
import com.exp.explorista.model.RegistrationPhoneSubmitOTPResendResponse;
import com.exp.explorista.model.RegistrationPhoneSubmitOTPVerificationResponse;
import com.exp.explorista.model.RegistrationPhoneSubmitResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {


    @FormUrlEncoded
    @POST("login_google_check_user.php")
    Call<LoginGoogleCheckUserResponse> getLoginGoogleCheckUser
            (@Field("cust_email") String cust_email);

    @FormUrlEncoded
    @POST("login_google_submit.php")
    Call<LoginGoogleSubmitResponse> getLoginGoogleSubmit
            (@Field("cust_phone") String cust_phone);

    @FormUrlEncoded
    @POST("login_google_submit_otp_verification.php")
    Call<LoginGoogleSubmitBeforeOTPVerificationNotExistUserResponse> getLoginGoogleSubmitBeforeOTPVerificationNotExistUser
            (@Field("cust_name") String cust_name ,@Field("cust_email") String cust_email ,@Field("cust_phone") String cust_phone, @Field("cust_otp") String cust_otp);

    // login api service.......

    @FormUrlEncoded
    @POST("login_phone_submit.php")
    Call<LoginPhoneSubmitResponse> getLoginPhoneSubmit
            (@Field("cust_phone") String cust_phone);

    @FormUrlEncoded
    @POST("login_phone_submit_otp_verification.php")
    Call<LoginPhoneSubmitOTPVerificationResponse> getLoginPhoneSubmitOTPVerification
            (@Field("cust_phone") String cust_phone , @Field("cust_otp") String cust_otp);


    @FormUrlEncoded
    @POST("login_phone_submit_otp_resend.php")
    Call<LoginPhoneSubmitOTPResendResponse> getLoginPhoneSubmitOTPResend
            (@Field("cust_phone") String cust_phone);


    @FormUrlEncoded
    @POST("login_phone_name_final_submit.php")
    Call<LoginPhoneNameFinalSubmitResponse> getLoginPhoneNameFinalSubmit
            (@Field("cust_name") String cust_name , @Field("cust_phone") String cust_phone);


    // register api service......


    @FormUrlEncoded
    @POST("register_phone_submit.php")
    Call<RegistrationPhoneSubmitResponse> getRegistrationPhoneSubmit
            (@Field("cust_phone") String cust_phone);


    @FormUrlEncoded
    @POST("register_phone_submit_otp_verification.php")
    Call<RegistrationPhoneSubmitOTPVerificationResponse> getRegistrationPhoneSubmitOTPVerification
            (@Field("cust_phone") String cust_phone , @Field("cust_otp") String cust_otp);

    @FormUrlEncoded
    @POST("register_phone_submit_otp_resend.php")
    Call<RegistrationPhoneSubmitOTPResendResponse> getRegistrationPhoneSubmitOTPResend
            (@Field("cust_phone") String cust_phone);


    @FormUrlEncoded
    @POST("register_phone_name_final_submit.php")
    Call<RegistrationPhoneNameFinalSubmitResponse> getRegistrationPhoneNameFinalSubmit
            (@Field("cust_name") String cust_name , @Field("cust_phone") String cust_phone);

}
