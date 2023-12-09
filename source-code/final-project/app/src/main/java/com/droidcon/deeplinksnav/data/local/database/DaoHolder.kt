package com.droidcon.deeplinksnav.data.local.database

/**
 * Holds DAOs used to pre-populate database
 */
data class DaoHolder(
    val categoryDao: CategoryDao,
    val bookDao: BookDao,
    val courseDao: CourseDao
)