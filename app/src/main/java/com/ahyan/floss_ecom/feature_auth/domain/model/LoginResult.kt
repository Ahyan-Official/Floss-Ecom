package com.ahyan.floss_ecom.feature_auth.domain.model

import com.ahyan.floss_ecom.core.util.Resource

data class LoginResult(
    val passwordError: String? = null,
    val usernameError: String? = null,
    val result: Resource<Unit>? = null
)
