@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.droidcon.deeplinksnav.ui.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.CategoryLocalDataSource
import com.droidcon.deeplinksnav.data.local.database.Category

/**
 * Details screen for an individual [Category] item
 */
@Composable
fun CategoryDetails(
    category: Category,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(id = R.string.app_name))
            },
                actions = {
                    Row{
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Arrow Back"
                            )
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        Card(Modifier.padding(contentPadding)) {
            Column {
                //title
                Text(text = category.name, modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineLarge)
                //Separator
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.onSecondary))

                //Cover
                if (category.coverRes != null) {
                    Image(painter = painterResource(id = category.coverRes), contentDescription = null, modifier = Modifier.padding(8.dp))
                }
                else if (category.coverUrl != null){
                    AsyncImage(model = category.coverUrl, contentDescription = null)
                }
                //Description
                category.description?.let{
                    Text(text = it, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodyLarge)
                }

            }
        }
    }
}

@Preview
@Composable
fun CategoryDetailsPreview() {
    CategoryDetails(category = CategoryLocalDataSource().categories[0],
        onBack = {})
}

@Preview
@Composable
fun CategoryDetailsPreview2() {
    CategoryDetails(category = CategoryLocalDataSource().categories[1],
        onBack = {})
}