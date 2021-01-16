package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId

@Composable
fun ActionSwitch(text: String, id: String, checked: Boolean,
                 onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.layoutId(id),
    ) {
        Switch(checked = checked,
            color = MaterialTheme.colors.primaryVariant,
            onCheckedChange = onCheckedChange
        )
        Text(text = text)
    }
}