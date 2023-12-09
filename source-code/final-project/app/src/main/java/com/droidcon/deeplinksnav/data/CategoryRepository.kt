package com.droidcon.deeplinksnav.data

import androidx.compose.ui.text.toLowerCase
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.local.database.Category
import com.droidcon.deeplinksnav.data.local.database.CategoryDao
import com.droidcon.deeplinksnav.ui.CATEGORY_BOOKS
import com.droidcon.deeplinksnav.ui.CATEGORY_COURSES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

interface CategoryRepository {
    val categories: Flow<List<Category>>

    suspend fun add(category: Category)

    suspend fun getCategoryByName(name: String): Category?
}

@Singleton
class CategoryLocalDataSource @Inject constructor(){
    val categories: List<Category> = DefaultCategories
}

val DefaultCategories = listOf(
    Category(
        name = CATEGORY_COURSES,
        coverRes = R.drawable.online_courses,
        coverUrl = null,
        description = "Catalog of online courses"
    ),
    Category(
        name = CATEGORY_BOOKS,
        coverRes = R.drawable.books,
        coverUrl = null,
        description = "Catalog of written books"
    )
)

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


class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
): CategoryRepository {
    override val categories: Flow<List<Category>>
        get() = categoryDao.getCategories()

    override suspend fun add(category: Category) {
        categoryDao.insertCategory(category)
    }

    override suspend fun getCategoryByName(name: String): Category? {
        var category: Category? = null
        categoryDao.getCategories().collect { list ->
            category = list.find { category ->
                val processedName = category.name.filterNot { it.isWhitespace() }.lowercase()
                val nameToTest = name.filterNot { it.isWhitespace() }.lowercase()
                nameToTest == processedName
            }
        }
        return category
    }

}