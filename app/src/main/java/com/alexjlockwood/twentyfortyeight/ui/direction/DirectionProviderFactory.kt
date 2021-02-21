package com.alexjlockwood.twentyfortyeight.ui.direction

import android.content.Context
import com.alexjlockwood.twentyfortyeight.domain.Direction
import com.alexjlockwood.twentyfortyeight.isGoogleServiceAvailable
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication

private const val API_KEY = "client/api_key"
object DirectionProviderFactory {

    fun getDirectionProvider(context: Context,
                             onSwipeListener: (direction: Direction) -> Unit) =
        if (isGoogleServiceAvailable(context)) {
            VoiceProvider(context, VoiceDirectionExtractor(), onSwipeListener)
        } else {
            val  config  = AGConnectServicesConfig.fromContext(context)
            MLApplication.getInstance().apiKey = config.getString(API_KEY)
            HuaweiVoiceProvider(context, VoiceDirectionExtractor(), onSwipeListener)
        }
}