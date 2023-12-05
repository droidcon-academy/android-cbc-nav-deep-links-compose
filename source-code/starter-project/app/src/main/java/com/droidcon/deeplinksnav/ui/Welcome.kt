package com.droidcon.deeplinksnav.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.deeplinksnav.R

@Composable
fun Welcome(modifier: Modifier = Modifier,
            onNavigate: (String) -> Unit) {
    Scaffold(modifier = modifier) {paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {

            Column(
                modifier = Modifier
                .align(Alignment.TopCenter)
            ){
                Image(
                    painter = painterResource(id = R.drawable.mehdi),
                    contentDescription = "My Photo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = stringResource(R.string.my_demo), modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }

            TextButton(onClick = {
                onNavigate(Screen.Category.route)
            }, modifier = Modifier
                .align(Alignment.Center)) {
                Text(text = stringResource(R.string.start_here), modifier = Modifier
                    .padding(8.dp)
                    ,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview
@Composable
fun WelcomePreview() {
    Welcome(onNavigate = {})
}