package com.ahyan.floss_ecom.feature_cart.domain.repository

import com.ahyan.floss_ecom.core.util.Resource
import com.ahyan.floss_ecom.feature_cart.domain.model.CartProduct
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getAllCartItems(id: Int): Flow<Resource<List<CartProduct>>>
}