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

package com.droidcon.deeplinksnav.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Entity
data class Course(
    val name: String,
    val instructor: String,
    val coverRes: Int?,
    val coverUrl: String?,
    val description: String?,
    val instructorImgRes: Int? = null,
    val instructorImgUrl: String? = null
) {
    @PrimaryKey
    var uid: String = UUID.randomUUID().toString()
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM course ORDER BY uid DESC LIMIT 10")
    fun getCourses(): Flow<List<Course>>

    @Insert
    suspend fun insertCourse(item: Course)
}
