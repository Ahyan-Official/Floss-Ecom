package com.ahyan.floss_ecom.feature_auth.domain.use_case

import com.ahyan.floss_ecom.core.util.Resource
import com.ahyan.floss_ecom.feature_auth.domain.repository.LoginRepository

class AutoLoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return loginRepository.autoLogin()
    }
}