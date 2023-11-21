package com.bobo.data_lotto_app.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.android.billingclient.api.ProductDetails
import com.bobo.data_lotto_app.MainRouteAction
import com.bobo.data_lotto_app.MyApplication
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.ViewModel.NoticeViewModel


@Composable
fun PaymentScreen(onPurchaseButtonClicked: (ProductDetails) -> Unit
) {

    val productList = MyApplication.instance.billingClientLifecycle.fetchedProductList.collectAsState()

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {

        item {
            Button(onClick = {
                MyApplication.instance.billingClientLifecycle.fetchAvailableProducts()
            }) {
                Text(text = "구매가능 아이템 불러오기")
            }
        }

        items(productList.value) {
            Column {
                Text(text = "id : ${it.productId}")
                Text(text = "title : ${it.title}")
                Text(text = "name : ${it.name}")
                Text(text = "type : ${it.productType}")
                Text(text = "description : ${it.description}")
                Text(text = "subscriptionOfferDetails : ${it.subscriptionOfferDetails}")
                Text(text = "oneTimePurchaseOfferDetails : ${it.oneTimePurchaseOfferDetails}")
                Button(onClick = {
                    Log.d("TAG", "MainScreen: ${it.productId}")
                    onPurchaseButtonClicked.invoke(it)
                }) {
                    Text(text = it.name)
                }
            }
        }

    }

}
