package com.tech.sonet.ui.subscription_module.sub_manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.gson.Gson

//class SubscriptionManager(
//    val context: Context, val onSubscriptionPurchased: SubscriptionListener
//) : PurchasesUpdatedListener {
//
//    private lateinit var plansList: MutableList<ProductDetails>
//    private val billingClient =
//        BillingClient.newBuilder(context).setListener(this).enablePendingPurchases().build()
//
//
//    fun startConnection(onConnected: () -> Unit) {
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    onConnected()
//                } else {
//                    Log.d("startConnection", "onConnected: ")
//                }
//            }
//
//            override fun onBillingServiceDisconnected() {
//
//                Log.d("startConnection", "onBillingServiceDisconnected: ")
//            }
//        })
//    }
//
//    // Fetch available subscriptions
//    fun queryAvailableSubscriptions(callback: (List<ProductDetails>) -> Unit) {
//        val params = QueryProductDetailsParams.newBuilder().setProductList(
//            listOf(
//                QueryProductDetailsParams.Product.newBuilder().setProductId("com.sonet.monthly")
//                    .setProductType(BillingClient.ProductType.SUBS).build()
//
//            )
//        ).build()
//
//        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                plansList = productDetailsList
//                Log.d("plansList", "queryAvailableSubscriptions: ${Gson().toJson(plansList)}")
//                callback(productDetailsList)
//            } else {
//                Log.d("billingResult", "queryAvailableSubscriptions: ${billingResult.responseCode}")
//            }
//        }
//    }
//
//    fun purchaseSubscription(activity: Activity, planId: String?) {
//
//        val productDetails = plansList.find { it.productId == planId }
//        val billingFlowParams = BillingFlowParams.newBuilder()
//
//            .setProductDetailsParamsList(
//                listOf(
//                    BillingFlowParams.ProductDetailsParams.newBuilder()
//                        .setProductDetails(productDetails!!)
//                        .setOfferToken(productDetails.subscriptionOfferDetails?.get(0)?.offerToken!!)
//                        .build()
//                )
//            ).build()
//
//        billingClient.launchBillingFlow(activity, billingFlowParams)
//    }
//
//    override fun onPurchasesUpdated(
//        billingResult: BillingResult,
//        purchases: MutableList<Purchase>?,
//    ) {
//        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
//            for (purchase in purchases) {
//                handlePurchase(purchase)
//            }
//        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//
//
//        } else {
//
//        }
//    }
//
//    private fun handlePurchase(purchase: Purchase) {
//        // Acknowledge the purchase if it hasn’t been acknowledged
//        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
//            val acknowledgePurchaseParams =
//                AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
//                    .build()
//            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
//                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    onSubscriptionPurchased.subscriptionsPurchasedData(purchase)
//                }
//            }
//        }
//    }
//
//
//    fun restorePurchases(callback: (Boolean) -> Unit) {
//        if (!billingClient.isReady) {
//            Log.e("Billing", "Billing Client not ready, reconnecting...")
//            startConnection {
//                restorePurchases(callback) // Retry after connecting
//            }
//            return
//        }
//
//        billingClient.queryPurchasesAsync(BillingClient.ProductType.SUBS) { billingResult, purchases ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                if (purchases.isNotEmpty()) {
//                    var hasActiveSubscription = false
//                    for (purchase in purchases) {
//                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
//                            handlePurchase(purchase) // Process only active subscriptions
//                            hasActiveSubscription = true
//                        }
//                    }
//                    callback(hasActiveSubscription) // Notify UI
//                } else {
//                    Log.i("Billing", "No active subscriptions found.")
//                    callback(false)
//                }
//            } else {
//                Log.e("Billing", "Error restoring purchases: ${billingResult.responseCode}")
//                callback(false)
//            }
//        }
//    }
//
//
//    fun openSubscriptionManagementPage(activity: Activity) {
//        val packageName = context.packageName
//        val sku = "com.sonet.monthly"
//        val subscriptionUrl =
//            "https://play.google.com/store/account/subscriptions?sku=$sku&package=$packageName"
//
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(subscriptionUrl))
//        activity.startActivity(intent)
//    }
//
//
//
//}