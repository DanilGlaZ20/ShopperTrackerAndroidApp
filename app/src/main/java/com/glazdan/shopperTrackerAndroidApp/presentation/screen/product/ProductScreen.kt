package com.glazdan.shopperTrackerAndroidApp.presentation.screen.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.glazdan.shopperTrackerAndroidApp.R
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.InputShopCard
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.OutputShopCard
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleButton
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleTopBar


@Composable

fun ProductScreen(
	modifier: Modifier = Modifier,
	onBackClick: () -> Unit,
	viewModel: ProductViewModel = hiltViewModel()
) {

	val state by viewModel.state.collectAsState()
	val creatingState = state as? ProductState.CreatingProduct

	Scaffold { innerPadding ->
		LazyColumn(
			modifier = modifier
				.fillMaxSize()
				.imePadding()
				.padding(paddingValues = innerPadding)
				.padding(horizontal = 24.dp, vertical = 16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			item{
				SampleTopBar(
					modifier = Modifier.fillMaxWidth(),
					text = stringResource(R.string.shopping_list),
					onBackClick = onBackClick
				)

			}
			item {
				SampleButton(
					modifier = modifier
						.fillMaxWidth()
						.height(50.dp),
					title = if(creatingState?.isFormVisible == true) stringResource(R.string.cancel) else stringResource(R.string.add),
					onClick = {viewModel.proccessCommand(ProductCommand.ToggleFormVisibility)}

				)
			}

			if (creatingState?.isFormVisible == true){
				item{
					InputShopCard(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(R.string.what_is_buy),
						shopText = stringResource(R.string.name_product),
						shopDescription = stringResource(R.string.description),
						badgeText = stringResource(R.string.shop),
						queryText = creatingState.title,
						queryDescription = creatingState.description,
						queryBadge = creatingState.shop,
						buttonText = stringResource(R.string.save),
						onTextChanged = {viewModel.proccessCommand(ProductCommand.InputTitle(it))},
						onDescriptionChanged ={viewModel.proccessCommand(ProductCommand.InputDescription(it))},
						onShopChanged = {viewModel.proccessCommand(ProductCommand.InputShop(it))},
						onAddItem = {viewModel.proccessCommand(ProductCommand.SaveProduct)}
					)

				}
			}

			if(creatingState?.isLoading == false && creatingState?.productsList?.isEmpty() ==true && creatingState?.isFormVisible ==false)
			{
				item {
					Image(
						modifier = Modifier
							.size(500.dp)
							.fillMaxWidth(),
						painter = painterResource(id = R.drawable.im_create_offlineproduct_screen),
						contentDescription = stringResource(R.string.offline_product_illustration),

						colorFilter = ColorFilter.tint(
							color = MaterialTheme.colorScheme.background,
							blendMode = BlendMode.Multiply
						)

					)

				}

			}

			if (creatingState?.productsList?.isNotEmpty() == true) {
				item {
					Text(
						text = "В вашем списке:",
						fontWeight = FontWeight.Bold,
						fontSize = 18.sp,
						color = MaterialTheme.colorScheme.onSurface,
						modifier = Modifier.padding(top = 8.dp)
					)
				}
			}
			items(creatingState?.productsList ?: emptyList()) { product ->
				OutputShopCard (
					title = product.productTitle,
					description = product.productDescription,
					badgeText = product.productShop,
					isBought = product.isBought,
					hasBorder = false,
					onCheckedClick = { isChecked ->
						viewModel.proccessCommand(ProductCommand.RemoveProduct(product))
					},
					onClick = { viewModel.proccessCommand(ProductCommand.EditProduct(product)) }
				)
			}




		}


	}
}