package com.droidcon.deeplinksnav.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.local.database.Book
import com.droidcon.deeplinksnav.data.local.database.BookDao
import com.droidcon.deeplinksnav.data.local.database.Category
import com.droidcon.deeplinksnav.data.local.database.CategoryDao
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.data.local.database.CourseDao
import com.droidcon.deeplinksnav.data.local.database.DaoHolder
import com.droidcon.deeplinksnav.ui.CATEGORY_BOOKS
import com.droidcon.deeplinksnav.ui.CATEGORY_COURSES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

class DbInitializer(
    private val daoHolder: Provider<DaoHolder>,
    ) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            insertDefaultCategories(daoHolder.get().categoryDao)
            insertDefaultCourses(daoHolder.get().courseDao)
            insertDefaultBooks(daoHolder.get().bookDao)
        }

    }

    private suspend fun insertDefaultCategories(categoryDao: CategoryDao){
        categoryDao.insertOrUpdateCategory(*DefaultCategories.toTypedArray())
    }

    private suspend fun insertDefaultCourses(courseDao: CourseDao){
        DefaultCourses.forEach { courseDao.insertCourse(it) }
    }

    private suspend fun insertDefaultBooks(bookDao: BookDao){
        DefaultBooks.forEach { bookDao.insertBook(it) }
    }
}


val DefaultBooks = listOf(
    Book(
        name = "Jetpack Compose Developer Handbook",
        author = "Mehdi Haghgoo",
        authorPicRes = R.drawable.mehdi,
        authorPicUrl = null,
        coverRes = R.drawable.compose_book,
        coverUrl = null,
        description = "Coming Soon"
    ),
    Book(
        name = "The Little Book of Containers",
        author = "Mehdi Haghgoo",
        authorPicRes = R.drawable.mehdi,
        authorPicUrl = null,
        coverRes = R.drawable.containers_book,
        coverUrl = null,
        description = "Coming Soon"
    )
)

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

val DefaultCourses = listOf(
    Course("Compose Animations", "Mehdi Haghgoo", R.drawable.compose_animations, null, "Learn how to implement UI animations in Jetpack Compose", instructorImgRes = R.drawable.mehdi),
    Course("The Complete Android Animations Course with Kotlin", "Mehdi Haghgoo", R.drawable.android_animations, null, "This course will teach you how to create animations for Views in Android. You will also learn about activity transitions and more.", instructorImgRes = R.drawable.mehdi),
    Course("Jetpack Compose Developer Course", "Mehdi Haghgoo", R.drawable.compose_dev_course, null, "The course covers the basics of UI development using Android's Jetpack Compose toolkit", instructorImgRes = R.drawable.mehdi),
    Course("The Complete Containers Course for Developers", "Mehdi Haghgoo", null, null, "With this course, you will learn what Linux containers are and how you can create and manage them.", instructorImgRes = R.drawable.mehdi)
)
