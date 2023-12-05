@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidcon.deeplinksnav.ui.book

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.droidcon.deeplinksnav.data.local.database.Book
import com.droidcon.deeplinksnav.ui.theme.MyApplicationTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookGrid(
    items: List<Book>,
    onBookSelected: (book: Book) -> Unit,
    onNavigate: (route: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }
    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), modifier = modifier) {
        itemsIndexed(items = items, key = { _, item -> item.uid }) { _, item ->
            Card(
                onClick = {
                    //Navigate to the details screen for this book
                    onBookSelected(item)
                    onNavigate(item.name)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                if (item.coverRes != null) {
                    Image(
                        painter = painterResource(item.coverRes), contentDescription = item.name,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    )
                } else if (item.coverUrl != null) {
                    AsyncImage(
                        model = item.coverUrl, contentDescription = item.name,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                Text(
                    text = item.name, modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = item.author, modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        BookGrid(
            items = listOf(),
            onBookSelected = {},
            onNavigate = {},
            onBack = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
fun PortraitPreview() {
    MyApplicationTheme {
        BookGrid(
            items = listOf(),
            onBookSelected = {},
            onNavigate = {},
            onBack = {}
        )
    }
}
