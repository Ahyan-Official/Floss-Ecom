package com.ahyan.floss_ecom.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String
)