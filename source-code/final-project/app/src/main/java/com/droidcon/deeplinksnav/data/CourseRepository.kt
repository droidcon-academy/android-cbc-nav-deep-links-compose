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

import android.util.Log
import com.droidcon.deeplinksnav.data.local.database.DefaultCourses
import kotlinx.coroutines.flow.Flow
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.data.local.database.CourseDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * [Course] repository interface
 */
interface CourseRepository {
    val courses: Flow<List<Course>>

    suspend fun add(name: Course)

    suspend fun getCourseByName(name: String): Course?
}

/**
 * default implementation of [CourseRepository] backed by [androidx.room.RoomDatabase] for use in production
 */
class DefaultCourseRepository @Inject constructor(
    private val courseDao: CourseDao
) : CourseRepository {

    override val courses: Flow<List<Course>> =
        courseDao.getCourses()

    override suspend fun add(name: Course) {
        courseDao.insertOrUpdateCourse(name)
    }

    override suspend fun getCourseByName(name: String): Course? {
        val c = courses.first()
        Log.d("CourseRepository", "getCourseByName: Passed name is: $name")
        Log.d("CourseRepository", "getCourseByName: number of courses in flow: ${c.size}")
        return courses.first().find {course->
            course.name.filterNot{it.isWhitespace()}.lowercase() == name.filterNot { it.isWhitespace() }.lowercase()
        }

    }
}


/**
 * Dummy implementation of [CourseRepository] useful for testing the app
 */
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


