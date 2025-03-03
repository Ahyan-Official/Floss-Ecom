package com.ahyan.floss_ecom.feature_products.presentation.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ahyan.floss_ecom.R
import com.ahyan.floss_ecom.core.presentation.ui.theme.DarkBlue
import com.ahyan.floss_ecom.core.presentation.ui.theme.FlossTheme
import com.ahyan.floss_ecom.core.presentation.ui.theme.MainWhiteColor
import com.ahyan.floss_ecom.core.presentation.ui.theme.YellowMain
import com.ahyan.floss_ecom.core.util.LoadingAnimation
import com.ahyan.floss_ecom.core.util.UiEvents
import com.ahyan.floss_ecom.destinations.ProductDetailsScreenDestination
import com.ahyan.floss_ecom.feature_products.domain.model.Product
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var filtersExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val productsState = viewModel.productsState.value
    val categories = viewModel.categoriesState.value

    Scaffold(
        topBar = {
            MyTopAppBar(
                currentSearchText = viewModel.searchTerm.value,
                onSearchTextChange = {
                    viewModel.setSearchTerm(it)
                },
                onSearch = {
                    keyboardController?.hide()
                    viewModel.getProducts(
                        searchTerm = viewModel.searchTerm.value
                    )
                },
                onToggleExpand = {
                    filtersExpanded = !filtersExpanded
                },
            )
        },
    ) {
        val scaffoldState = rememberScaffoldState()
        LaunchedEffect(key1 = true) {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvents.SnackbarEvent -> {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                    else -> {}
                }
            }
        }

        DropdownMenu(
            expanded = filtersExpanded,
            offset = DpOffset(x = 200.dp, y = -600.dp),
            onDismissRequest = {
                filtersExpanded = !filtersExpanded
            }
        ) {
            DropdownMenuItem(
                content = { Text("Clothes") },
                onClick = { Toast.makeText(context, "Clothes", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                content = { Text("Shoes") },
                onClick = { Toast.makeText(context, "Shoes", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                content = { Text("Electronics") },
                onClick = { Toast.makeText(context, "Electronics", Toast.LENGTH_SHORT).show() }
            )
        }

        HomeScreenContent(
            categories = categories,
            productsState = productsState,
            navigator = navigator,
            bannerImageUrl = viewModel.bannerImageState.value,
            selectedCategory = viewModel.selectedCategory.value,
            onSelectCategory = { category ->
                viewModel.setCategory(category)
                viewModel.getProducts(viewModel.selectedCategory.value)
            }
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeScreenContent(
    categories: List<String>,
    productsState: ProductsState,
    navigator: DestinationsNavigator,
    bannerImageUrl: String,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jo),  // Replace with your drawable resource ID
                        contentScale = ContentScale.Crop,
                        contentDescription = "Black Friday Banner"
                    )

                }

            } // Header for some banner Image

            // Some spacer
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item(span = { GridItemSpan(2) }) {
                Categories(
                    categories = categories,
                    onSelectCategory = onSelectCategory,
                    selectedCategory = selectedCategory
                )
            } // Header with a lazyRow for product categories

            // Some spacer
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Actual product items list
            items(productsState.products) { product ->
                ProductItem(
                    product = product,
                    navigator = navigator,
                    modifier = Modifier
                        .width(150.dp)
                )
            }
        }

        if (productsState.isLoading) {
            LoadingAnimation(
                modifier = Modifier.align(Center),
                circleSize = 16.dp,
            )
        }

        if (productsState.error != null) Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Center)
                .padding(16.dp),
            text = productsState.error,
            color = Color.Red
        )
    }
}

@Composable
private fun ProductItem(
    product: Product,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(7.dp)
            .clickable {
                navigator.navigate(ProductDetailsScreenDestination(product))
            },
        elevation = 10.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = product.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.ic_placeholder)
                            crossfade(true)
                        }).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(CenterHorizontally),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.category,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .align(CenterVertically),
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    tint = YellowMain
                )
                Text(
                    modifier = Modifier.align(CenterVertically),
                    text = "${product.rating.rate} (${product.rating.count})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${product.price}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp), // Adds horizontal padding to the row
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /* Do something when button is clicked */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp), // Specifies the height, full width is achieved with fillMaxWidth
                    shape = RoundedCornerShape(10.dp), // Rounded corners with a radius of 20dp
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = YellowMain, // Background color of the button
                        contentColor = Color.White // Color of the content (icon and text)
                    )
                ) {

                    Text(
                        text = "Add to Cart", // Text to display on the button
                        modifier = Modifier.padding(start = 8.dp) // Padding between the icon and the text
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

    }
}

@Composable
fun MyTopAppBar(
    currentSearchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onToggleExpand: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jo), // Replace with your drawable resource
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(35.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Hi, User", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_allert),
                contentDescription = null,
                tint = DarkBlue
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            Modifier
                .fillMaxWidth()  // Ensure the Row expands to fill the available width
                .padding(horizontal = 0.dp),  // Remove any padding from the Row
            horizontalArrangement = Arrangement.Start,  // Start alignment to make sure TextField is aligned to the left
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = currentSearchText,
                onValueChange = {
                    onSearchTextChange(it)
                },
                placeholder = {
                    Text(text = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth()  // TextField will take full width within the Row
                    .background(MainWhiteColor, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        // You can add click functionality here if needed
                    },
                shape = RoundedCornerShape(size = 8.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    disabledTextColor = MainWhiteColor,
                    backgroundColor = MainWhiteColor,
                    focusedIndicatorColor = MainWhiteColor,
                    unfocusedIndicatorColor = MainWhiteColor,
                    disabledIndicatorColor = MainWhiteColor
                ),
                textStyle = TextStyle(color = Color.Black),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = DarkBlue
                    )
                }
            )
        }

    }
}


@Composable
fun Categories(
    categories: List<String>,
    onSelectCategory: (String) -> Unit,
    selectedCategory: String,
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories) { category ->
            Text(
                text = category,
                style = typography.body1.merge(),
                color = Color.Black,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            size = 8.dp,
                        ),
                    )
                    .clickable {
                        onSelectCategory(category)
                    }
                    .background(
                        if (category == selectedCategory) {
                            YellowMain
                        } else {
                            MainWhiteColor
                        }
                    )
                    .padding(
                        10.dp
                    )
            )
        }
    }
}

