package com.alexjlockwood.twentyfortyeight.ui.observer

import android.content.Context
import com.google.android.gms.common.ConnectionResult.SUCCESS
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication

private const val API_KEY = "client/api_key"
object ObserverFactory {

    fun getDirectionObserver(context: Context, onSwipeListener: (direction: String) -> Boolean) =
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == SUCCESS) {
            VoiceObserver(onSwipeListener)
        } else {
            val  config  = AGConnectServicesConfig.fromContext(context)
            MLApplication.getInstance().apiKey = config.getString(API_KEY)
            HuaweiVoiceObserver(onSwipeListener)
        }
}