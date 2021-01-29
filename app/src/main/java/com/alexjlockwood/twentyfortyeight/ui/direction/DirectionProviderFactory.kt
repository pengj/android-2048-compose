package com.alexjlockwood.twentyfortyeight.ui.direction

import android.content.Context
import com.alexjlockwood.twentyfortyeight.isGoogleServiceAvailable
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication

private const val API_KEY = "client/api_key"
object DirectionProviderFactory {

    fun getDirectionProvider(context: Context, onSwipeListener: (direction: String) -> Boolean) =
        if (isGoogleServiceAvailable(context)) {
            VoiceProvider(onSwipeListener)
        } else {
            val  config  = AGConnectServicesConfig.fromContext(context)
            MLApplication.getInstance().apiKey = config.getString(API_KEY)
            HuaweiVoiceProvider(onSwipeListener)
        }
}