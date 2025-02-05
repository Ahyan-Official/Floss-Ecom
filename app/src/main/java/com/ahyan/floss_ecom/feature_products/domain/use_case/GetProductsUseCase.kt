package com.ahyan.floss_ecom.feature_products.domain.use_case

import com.ahyan.floss_ecom.core.util.Resource
import com.ahyan.floss_ecom.feature_products.domain.model.Product
import com.ahyan.floss_ecom.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Product>>> {
        return productsRepository.getProducts()
    }
}