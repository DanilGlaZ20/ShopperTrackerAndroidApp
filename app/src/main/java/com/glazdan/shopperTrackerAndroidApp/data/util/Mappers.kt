package com.glazdan.shopperTrackerAndroidApp.data.util

import com.glazdan.shopperTrackerAndroidApp.data.local.OfflineProductDbModel
import com.glazdan.shopperTrackerAndroidApp.data.remote.dto.CategoryDto
import com.glazdan.shopperTrackerAndroidApp.data.remote.dto.ProductDto
import com.glazdan.shopperTrackerAndroidApp.data.remote.dto.UserDto
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Category
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.OfflineProduct
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User
import com.glazdan.shopperTrackerAndroidApp.presentation.navigation.Screen


fun UserDto.toModel(): User{
    return  User(
        id = id,
        username = username,
        password = password,
        phone = phone
    )

}

fun CategoryDto.toModel(): Category{
    return Category(
        categoryId = categoryId,
        userId = userId,
        categoryTitle = categoryTitle,
        categoryDescription = categoryDescription
    )
}
fun ProductDto.toModel(): Product{
    return Product(
        productId = productId,
        categoryId = categoryId,
        productTitle = productTitle,
        productDescription = productDescription
    )
}
fun OfflineProduct.toDbModel(): OfflineProductDbModel{
    return OfflineProductDbModel(id,title,description,shop,isBought)
}

fun OfflineProductDbModel.toEntity(): OfflineProduct{
    return OfflineProduct(id, title, shop,description, isBought)
}