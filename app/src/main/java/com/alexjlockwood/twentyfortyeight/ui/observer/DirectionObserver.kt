package com.alexjlockwood.twentyfortyeight.ui.observer

import android.content.Context

interface DirectionObserver {
    fun init(context: Context)
    fun start()
    fun stop()
    fun destroy()
}