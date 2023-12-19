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

package com.droidcon.deeplinksnav.data.di

import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.BookRepository
import com.droidcon.deeplinksnav.data.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.droidcon.deeplinksnav.data.CourseRepository
import com.droidcon.deeplinksnav.data.DefaultBookRepository
import com.droidcon.deeplinksnav.data.DefaultCategoryRepository
import com.droidcon.deeplinksnav.data.DefaultCourseRepository
import com.droidcon.deeplinksnav.data.DummyCategoryRepository
import com.droidcon.deeplinksnav.data.DummyBookRepository
import com.droidcon.deeplinksnav.data.DummyCourseRepository
import com.droidcon.deeplinksnav.data.local.database.Course
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsCourseRepository(
        courseRepository: DefaultCourseRepository
    ): CourseRepository

    @Singleton
    @Binds
    fun bindsCategoryRepository(
        categoryRepository: DefaultCategoryRepository
    ): CategoryRepository

    @Singleton
    @Binds
    fun bindsBookRepository(
        bookRepository: DefaultBookRepository
    ): BookRepository

}