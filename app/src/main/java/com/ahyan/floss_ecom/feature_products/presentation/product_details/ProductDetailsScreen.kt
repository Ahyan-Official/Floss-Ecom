package com.ahyan.floss_ecom.feature_products.presentation.product_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.ahyan.floss_ecom.R
import com.ahyan.floss_ecom.core.presentation.ui.theme.FlossTheme
import com.ahyan.floss_ecom.core.presentation.ui.theme.GrayColor
import com.ahyan.floss_ecom.core.presentation.ui.theme.MainWhiteColor
import com.ahyan.floss_ecom.core.presentation.ui.theme.YellowMain
import com.ahyan.floss_ecom.feature_products.domain.model.Product
import com.ahyan.floss_ecom.feature_products.domain.model.Rating
import com.ahyan.floss_ecom.feature_wish_list.data.mapper.toWishlistRating
import com.ahyan.floss_ecom.feature_wish_list.domain.model.Wishlist
import com.ahyan.floss_ecom.feature_wish_list.presentation.wishlist.WishlistViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProductDetailsScreen(
    product: Product,
    navigator: DestinationsNavigator,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    val inWishlist = viewModel.inWishlist(product.id).observeAsState().value != null

    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navigator.popBackStack()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(
                    onClick = {
                        if (inWishlist) {
                            viewModel.deleteFromWishlist(
                                Wishlist(
                                    image = product.image,
                                    title = product.title,
                                    id = product.id,
                                    liked = true,
                                    price = product.price,
                                    description = product.description,
                                    category = product.category,
                                    rating = product.rating.toWishlistRating()
                                )
                            )
                        } else {
                            viewModel.insertFavorite(
                                Wishlist(
                                    image = product.image,
                                    title = product.title,
                                    id = product.id,
                                    liked = true,
                                    price = product.price,
                                    description = product.description,
                                    category = product.category,
                                    rating = product.rating.toWishlistRating()
                                )
                            )
                        }
                    },
                ) {
                    Icon(
                        painter = if (inWishlist) {
                            painterResource(id = R.drawable.ic_heart_fill)
                        } else {
                            painterResource(id = R.drawable.ic_heart)
                        },
                        tint = if (inWishlist) {
                            YellowMain
                        } else {
                            GrayColor
                        },
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) {
        DetailsScreenContent(
            product = product,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DetailsScreenContent(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = product.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_placeholder)
                        }).build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Inside
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "$${product.price}",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = product.title,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        val rating: Float by remember { mutableStateOf(product.rating.rate.toFloat()) }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(
                value = rating,
                config = RatingBarConfig()
                    .activeColor(YellowMain)
                    .inactiveColor(GrayColor)
                    .stepSize(StepSize.HALF)
                    .numStars(5)
                    .isIndicator(true)
                    .size(16.dp)
                    .padding(3.dp)
                    .style(RatingBarStyle.HighLighted),
                onValueChange = {},
                onRatingChanged = {}
            )
            Text(
                text = "(${product.rating.count})",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = product.description,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(34.dp))
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /* Add to cart functionality */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = YellowMain,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.add_to_cart),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailsScreenContent() {
    FlossTheme {
        DetailsScreenContent(
            product = Product(
                id = 1,
                image = "https://via.placeholder.com/150",
                title = "Sample Product",
                price = 19.99,
                description = "This is a sample product description.",
                category = "Category",
                rating = Rating(4, 0.65)  // Ensure ProductRating matches your actual data class structure.
            )
        )
    }
}
