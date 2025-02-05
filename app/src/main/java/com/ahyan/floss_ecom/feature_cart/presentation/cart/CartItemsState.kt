package com.ahyan.floss_ecom.feature_cart.presentation.cart

import com.ahyan.floss_ecom.feature_cart.domain.model.CartProduct

data class CartItemsState(
    val cartItems: List<CartProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)