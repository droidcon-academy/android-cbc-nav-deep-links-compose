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

package com.droidcon.deeplinksnav.data

import com.droidcon.deeplinksnav.R
import kotlinx.coroutines.flow.Flow
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.data.local.database.CourseDao
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface CourseRepository {
    val courses: Flow<List<Course>>

    suspend fun add(name: Course)

    suspend fun getCourseByName(name: String): Course?
}

class DefaultCourseRepository @Inject constructor(
    private val courseDao: CourseDao
) : CourseRepository {

    override val courses: Flow<List<Course>> =
        courseDao.getCourses()

    override suspend fun add(name: Course) {
        courseDao.insertCourse(name)
    }

    override suspend fun getCourseByName(name: String): Course? {
        return courses.first().find { course-> course.name.find { it.isWhitespace() }?.lowercase() == name.filterNot { it.isWhitespace() }.lowercase() }

    }
}


class DummyCourseRepository @Inject constructor(
    private val courseDataSource: CourseLocalDataSource
) : CourseRepository {
    override val courses: Flow<List<Course>> = flowOf(courseDataSource.courses)

    override suspend fun add(name: Course) {
        throw NotImplementedError()
    }

    override suspend fun getCourseByName(name: String): Course? {
        return courseDataSource.courses.find{course->
            val processedName = course.name.filterNot{ it.isWhitespace() }.lowercase()
            val nameToTest = name.filterNot { it.isWhitespace() }.lowercase()
            nameToTest == processedName
        }
    }
}

class CourseLocalDataSource @Inject constructor(){
    val courses: List<Course> = DefaultCourses
}


val DefaultCourses = listOf(
    Course("Compose Animations", "Mehdi Haghgoo", R.drawable.compose_animations, null, "Learn how to implement UI animations in Jetpack Compose", instructorImgRes = R.drawable.mehdi),
    Course("The Complete Android Animations Course with Kotlin", "Mehdi Haghgoo", R.drawable.android_animations, null, "This course will teach you how to create animations for Views in Android. You will also learn about activity transitions and more.", instructorImgRes = R.drawable.mehdi),
    Course("Jetpack Compose Developer Course", "Mehdi Haghgoo", R.drawable.compose_dev_course, null, "The course covers the basics of UI development using Android's Jetpack Compose toolkit", instructorImgRes = R.drawable.mehdi),
    Course("The Complete Containers Course for Developers", "Mehdi Haghgoo", null, null, "With this course, you will learn what Linux containers are and how you can create and manage them.", instructorImgRes = R.drawable.mehdi)
)