package com.ahyan.floss_ecom.feature_products.domain.use_case

import com.ahyan.floss_ecom.feature_products.domain.repository.ProductsRepository

class GetCategoriesUseCase(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<String> {
        val categories = productsRepository.getProductCategories()
        return listOf("All") + categories
    }
}