package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId

@Composable
fun DebugView(text: String) {
    Row(
    ) {
        Text(text = text)
    }
}