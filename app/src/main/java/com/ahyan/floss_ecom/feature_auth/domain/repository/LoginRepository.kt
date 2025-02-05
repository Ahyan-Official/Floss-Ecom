package com.ahyan.floss_ecom.feature_auth.domain.repository

import com.ahyan.floss_ecom.core.util.Resource
import com.ahyan.floss_ecom.feature_auth.data.remote.request.LoginRequest

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest, rememberMe: Boolean): Resource<Unit>
    suspend fun autoLogin(): Resource<Unit>
    suspend fun logout(): Resource<Unit>
}
