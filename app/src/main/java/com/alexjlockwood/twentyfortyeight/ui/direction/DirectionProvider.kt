package com.alexjlockwood.twentyfortyeight.ui.direction

import android.content.Context

interface DirectionProvider {
    fun init(context: Context)
    fun start()
    fun stop()
    fun destroy()
}