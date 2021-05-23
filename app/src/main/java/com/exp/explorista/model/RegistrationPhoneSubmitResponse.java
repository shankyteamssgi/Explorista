package com.exp.explorista.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationPhoneSubmitResponse {


    @SerializedName("successResponse")
    @Expose
    private Integer successResponse;
    @SerializedName("messageResponse")
    @Expose
    private String messageResponse;

    public Integer getSuccessResponse()
    {
        return successResponse;
    }

    public void setSuccessResponse(Integer successResponse)
    {
        this.successResponse = successResponse;
    }

    public String getMessageResponse()
    {
        return messageResponse;
    }

    public void setMessageResponse(String messageResponse)
    {
        this.messageResponse = messageResponse;
    }
}
