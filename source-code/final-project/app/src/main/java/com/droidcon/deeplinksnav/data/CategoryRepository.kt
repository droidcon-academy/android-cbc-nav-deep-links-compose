package com.droidcon.deeplinksnav.data

import android.util.Log
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.local.DefaultCategories
import com.droidcon.deeplinksnav.data.local.database.Category
import com.droidcon.deeplinksnav.data.local.database.CategoryDao
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.ui.CATEGORY_BOOKS
import com.droidcon.deeplinksnav.ui.CATEGORY_COURSES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Category] repository interface
 */
interface CategoryRepository {
    val categories: Flow<List<Category>>

    suspend fun add(category: Category)

    suspend fun getCategoryByName(name: String): Category?
}

@Singleton
class CategoryLocalDataSource @Inject constructor(){
    val categories: List<Category> = DefaultCategories
}


/**
 * Dummy implementation of category repository useful for testing
 */
class DummyCategoryRepository @Inject constructor(
    private val categoryDataSource: CategoryLocalDataSource
): CategoryRepository{
    override val categories: Flow<List<Category>>
        get() = flowOf(categoryDataSource.categories)

    override suspend fun add(category: Category) {
        throw NotImplementedError()
    }

    override suspend fun getCategoryByName(name: String): Category? {
        return categoryDataSource.categories.find{cat->
            val processedName = cat.name.filterNot{ it.isWhitespace() }.lowercase()
            val nameToTest = name.filterNot { it.isWhitespace() }.lowercase()
            nameToTest == processedName
        }
    }

}


/**
 * default implementation of [CategoryRepository] backed by [androidx.room.RoomDatabase] for use in production
 */
class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
): CategoryRepository {
    override val categories: Flow<List<Category>>
        get() = categoryDao.getCategories()

    override suspend fun add(category: Category) {
        categoryDao.insertOrUpdateCategory(category)
    }

    override suspend fun getCategoryByName(name: String): Category? {
        return categories.first().find {category ->
            category.name.filterNot { it.isWhitespace() }.lowercase() == name.filterNot { it.isWhitespace() }.lowercase()
        }
    }

}