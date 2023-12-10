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

package com.droidcon.deeplinksnav.data.local.di

import android.content.Context
import androidx.room.Room
import com.droidcon.deeplinksnav.data.local.database.DbInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.droidcon.deeplinksnav.data.local.database.AppDatabase
import com.droidcon.deeplinksnav.data.local.database.BookDao
import com.droidcon.deeplinksnav.data.local.database.CategoryDao
import com.droidcon.deeplinksnav.data.local.database.CourseDao
import com.droidcon.deeplinksnav.data.local.database.DaoHolder
import javax.inject.Provider
import javax.inject.Singleton


/**
 * Hilt module providing app's [androidx.room.RoomDatabase] and its [androidx.room.Dao]s
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideCourseDao(appDatabase: AppDatabase): CourseDao {
        return appDatabase.courseDao()
    }

    @Provides
    fun provideBookDao(appDatabase: AppDatabase): BookDao {
        return appDatabase.bookDao()
    }


    /**
     * @param appContext Application context used to create the Room database
     * @param daosProvider Custom provider that allows providing [androidx.room.Dao]s used by database initializer
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
            @ApplicationContext appContext: Context,
            daosProvider: Provider<DaoHolder>
                           ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "links_app_db"
        )
            .addCallback(DbInitializer(daosProvider))
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun provideDaos(db: AppDatabase): DaoHolder {
        return DaoHolder(
            categoryDao = db.categoryDao(),
            bookDao = db.bookDao(),
            courseDao = db.courseDao()
        )
    }

}
