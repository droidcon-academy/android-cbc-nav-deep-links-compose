package com.droidcon.deeplinksnav.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(text = "Please wait", modifier = Modifier.padding(8.dp).align(Alignment.TopCenter),
            textAlign = TextAlign.Center, style = MaterialTheme.typography.displayMedium
        )
        CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.5f).align(Alignment.Center),
            color = MaterialTheme.colorScheme.onPrimary, trackColor = MaterialTheme.colorScheme.outline
            )
    }
}