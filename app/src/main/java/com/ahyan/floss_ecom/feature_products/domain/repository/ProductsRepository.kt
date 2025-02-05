package com.ahyan.floss_ecom.feature_products.domain.repository

import com.ahyan.floss_ecom.core.util.Resource
import com.ahyan.floss_ecom.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProducts(): Flow<Resource<List<Product>>>
    suspend fun getProductCategories(): List<String>
}