package com.ahyan.floss_ecom.feature_products.domain.model

import com.ahyan.floss_ecom.R
import com.ahyan.floss_ecom.destinations.*

sealed class BottomNavItem(var icon: Int, var destination: Destination) {
    object Home : BottomNavItem(
        icon = R.drawable.ic_home,
        destination = HomeScreenDestination
    )

    object Wishlist : BottomNavItem(
        icon = R.drawable.ic_heart,
        destination = WishlistScreenDestination
    )

    object Cart : BottomNavItem(
        icon = R.drawable.ic_basket,
        destination = CartScreenDestination
    )

    object Account : BottomNavItem(
        icon = R.drawable.ic_user,
        destination = AccountScreenDestination
    )
}
