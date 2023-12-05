@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.droidcon.deeplinksnav.ui.course

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.DefaultCourses
import com.droidcon.deeplinksnav.data.local.database.Course

@Composable
fun CourseDetails(
    course: Course,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = course.name, modifier = Modifier.padding(8.dp))
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
                    text = course.name,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            //cover
            item {
                if (course.coverRes != null) {
                    Image(
                        painter = painterResource(course.coverRes),
                        contentDescription = course.description
                    )
                } else if (course.coverUrl != null) {
                    AsyncImage(model = course.coverUrl, contentDescription = course.description)
                }
            }

            item {
                Row(Modifier.fillMaxWidth()) {
                    //Instructor name
                    Text(
                        text = course.instructor, modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.titleLarge
                    )

                    //Cover
                    if (course.instructorImgRes != null) {
                        Image(
                            painter = painterResource(course.instructorImgRes),
                            contentDescription = course.description,
                            modifier = Modifier.clip(CircleShape)
                        )
                    } else if (course.instructorImgUrl != null) {
                        AsyncImage(
                            model = course.instructorImgUrl,
                            contentDescription = course.description,
                            modifier = Modifier.clip(CircleShape)
                        )
                    }

                }
            }

            item{
                if (course.description != null){
                    Text(text = course.description, modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium)
                }
            }

        }

    }

}

@Preview
@Composable
fun CourseDetailsPreview() {
    CourseDetails(course = DefaultCourses[0], onBack = {})
}