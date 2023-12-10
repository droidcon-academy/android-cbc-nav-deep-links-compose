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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.data.local.database.CourseDao

/**
 * Unit tests for [DefaultCourseRepository].
 */
class DefaultCourseRepositoryTest {

    @Test
    fun courses_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultCourseRepository(FakeCourseDao())

//        repository.add("Repository")

        assertEquals(repository.courses.first().size, 1)
    }

}

private class FakeCourseDao : CourseDao {

    private val data = mutableListOf<Course>()

    override fun getCourses(): Flow<List<Course>> = flow {
        emit(data)
    }

    override suspend fun insertOrUpdateCourse(item: Course) {
        data.add(0, item)
    }
}
