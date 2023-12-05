@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.droidcon.deeplinksnav.ui.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.DefaultCategories
import com.droidcon.deeplinksnav.data.local.database.Category
import com.droidcon.deeplinksnav.ui.Screen


/**
 * Grid UI for the Categories
 */
@Composable
fun CategoryGrid(
    items: List<Category>,
    modifier: Modifier = Modifier,
    onCategorySelected: (category: Category) -> Unit,
    onNavigate: (route: String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.categories), style = MaterialTheme.typography.headlineLarge)
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                actions = {
                    IconButton(onClick = { onBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow Back")
                    }
                }
            )
        }
        ) {contentPadding->
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 200.dp),
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){

            items(
                items = items,
                key = { it.uid}
            ){cat->
                Card(
                    onClick = {
                        onCategorySelected(cat)
                        onNavigate(cat.name)
                    }
                ){
                    //Title
                    Text(text = cat.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))

                    //Category Cover
                    if(cat.coverRes != null) {
                        Image(painter = painterResource(id = cat.coverRes), contentDescription = cat.name,
                            modifier = Modifier)
                    }
                    else if (cat.coverUrl != null){
                        AsyncImage(model = cat.coverUrl, contentDescription = cat.name,
                            modifier = Modifier
                        )
                    }

                    //Description
                    Text(text = "${cat.description}", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }

        }

    }

}

@Preview
@Composable
fun CategoryGridPreview() {
    CategoryGrid(items = DefaultCategories, onCategorySelected = {}, onNavigate = {}, onBack = {  })
}