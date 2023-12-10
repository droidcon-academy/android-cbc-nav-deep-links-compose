@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.droidcon.deeplinksnav.ui.course

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import com.droidcon.deeplinksnav.data.local.database.DefaultCourses
import com.droidcon.deeplinksnav.data.local.database.Course

/**
 * Details screen for an individual [Course] item
 */
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
                    Text(text = course.name, modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
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
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                    ){
                        //Instructor name
                        Box(Modifier
                            .weight(1f)
                        ){
                            Text(
                                text = stringResource(R.string.instructor),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.BottomCenter)
                                ,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Text(
                            text = course.instructor,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                            ,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    //Cover
                    Box(Modifier
                        .weight(1f)
                    ){
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
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(
                                    R.string.person
                                )
                            )
                        }
                    }


                }
            }

            item{
                if (course.description != null){
                    Text(text = course.description, modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
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