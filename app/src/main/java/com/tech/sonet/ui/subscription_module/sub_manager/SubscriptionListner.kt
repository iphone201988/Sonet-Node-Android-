package com.tech.sonet.ui.subscription_module.sub_manager

import com.android.billingclient.api.Purchase

interface SubscriptionListener {
    fun subscriptionsPurchasedData(purchase: Purchase)
}