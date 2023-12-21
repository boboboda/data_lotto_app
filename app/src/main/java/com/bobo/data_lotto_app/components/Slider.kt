package com.bobo.data_lotto_app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SliderAdvanced() {

    var sliderPosition = remember { mutableStateOf(0f) }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically) {
        val value = when(sliderPosition.value) {
            0f -> {"모두"}
            10f -> {"최근800회차"}
            20f -> {"최근400회차"}
            30f -> {"최근200회차"}
            40f -> {"최근100회차"}
            50f -> {"최근50회차"}
            60f -> {"최근10회차"}
            else -> { "" }
        }

        Text(
            modifier = Modifier.weight(0.2f),
            text = value,
            textAlign = TextAlign.Center)

        Slider(
            modifier = Modifier.weight(0.8f),
            value = sliderPosition.value,
            onValueChange = { sliderPosition.value = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 5,
            valueRange = 0f..60f
        )





    }
}
