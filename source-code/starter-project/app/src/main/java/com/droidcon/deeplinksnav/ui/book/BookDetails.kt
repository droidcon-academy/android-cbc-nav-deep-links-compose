@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.droidcon.deeplinksnav.ui.book

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.DefaultBooks
import com.droidcon.deeplinksnav.data.local.database.Book

@Composable
fun BookDetails(
    modifier: Modifier = Modifier,
    book: Book,
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(text = book.name, modifier = Modifier.padding(8.dp))
            },
                actions = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.arrow_back
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            )
        }

    ) { contentPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)) {
            //title
            item {
                Text(
                    text = book.name,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            //cover
            item {
                if (book.coverRes != null) {
                    Image(painter = painterResource(book.coverRes), contentDescription = book.name)
                } else if (book.coverUrl != null) {
                    AsyncImage(model = book.coverUrl, contentDescription = book.name)
                }
            }

            //Instructor
            item {
                Row(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(
                        text = book.author, modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                        ,
                        style = MaterialTheme.typography.titleLarge
                    )

                    //Instructor photo
                    if (book.authorPicRes != null) {
                        Image(
                            painter = painterResource(book.authorPicRes),
                            contentDescription = book.author,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .clip(CircleShape)
                        )
                    } else if (book.authorPicUrl != null) {
                        AsyncImage(
                            model = book.authorPicUrl, contentDescription = book.author,
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(8.dp)
                        )
                    }

                }
            }

            //Description
            item {
                book.description?.let { description ->
                    Text(text = description, modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium)
                }
            }

        }

    }
}

@Preview
@Composable
fun BookDetailsPreview() {
    BookDetails(book = DefaultBooks[0], onBack = {})
}