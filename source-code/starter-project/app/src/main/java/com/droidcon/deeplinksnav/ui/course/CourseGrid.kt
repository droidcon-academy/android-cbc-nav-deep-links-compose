/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidcon.deeplinksnav.ui.course

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.DefaultCourses
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CourseGrid(
    items: List<Course>,
    modifier: Modifier = Modifier,
    onCourseSelected: (course: Course) -> Unit,
    onNavigate: (route: String) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler {
        onBack()
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.courses))
            },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            )
        }
    ) {contentPadding->
        LazyVerticalGrid(
            modifier = modifier.padding(contentPadding),
            columns = GridCells.Adaptive(200.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {

            itemsIndexed(items = items, key = { _, item -> item.uid }) { _, item ->
                Text("Number of items: ${items.size}")
                Card(
                    onClick = {
                        onCourseSelected(item)
                        onNavigate(item.name)
                    },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        //Cover
                        if (item.coverRes != null) {
                            Image(
                                painter = painterResource(item.coverRes),
                                contentDescription = item.description
                            )
                        } else if (item.coverUrl != null) {
                            AsyncImage(model = item.coverUrl, contentDescription = item.description)
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.placeholder),
                                contentDescription = null
                            )
                        }

                        //Title
                        Text(
                            item.name,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            style = MaterialTheme.typography.labelLarge
                        )

                        //Instructor name and image
                        Row(Modifier.fillMaxWidth()) {
                            Spacer(Modifier.weight(1f))
                            Text(
                                item.instructor,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(4.dp)
                                    .weight(1f),
                                style = MaterialTheme.typography.labelMedium
                            )
                            if (item.instructorImgRes != null) {
                                Image(
                                    painter = painterResource(item.instructorImgRes),
                                    contentDescription = item.instructor,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .weight(1f),
                                    contentScale = ContentScale.Inside
                                )
                            } else if (item.instructorImgUrl != null) {
                                AsyncImage(
                                    model = item.instructorImgUrl,
                                    contentDescription = item.instructor,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .weight(1f)
                                )

                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.instructor_placeholder),
                                    contentDescription = item.instructor,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .weight(1f)
                                )
                            }

                        }
                        item.description?.let {
                            Text(
                                it,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2
                            )
                        }

                        //Details button
                        TextButton(onClick = { onCourseSelected(item); onNavigate(item.name) }) {
                            Text(text = stringResource(id = R.string.course_details),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(8.dp)
                                )
                        }

                    }
                }


            }


        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        CourseGrid(
            items = DefaultCourses,
            onCourseSelected = {},
            onNavigate = {},
            onBack = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        CourseGrid(
            DefaultCourses,
            onCourseSelected = {},
            onNavigate = {},
            onBack = {}
        )
    }
}
