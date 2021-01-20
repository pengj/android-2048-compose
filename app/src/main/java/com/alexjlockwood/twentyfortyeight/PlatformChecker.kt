package com.alexjlockwood.twentyfortyeight

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

fun isGoogleServiceAvailable(context: Context) =
    GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS