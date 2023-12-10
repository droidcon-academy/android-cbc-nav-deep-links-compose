package com.droidcon.deeplinksnav.data.local.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.ui.CATEGORY_BOOKS
import com.droidcon.deeplinksnav.ui.CATEGORY_COURSES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * This class is responsible for initializing the database with static data on first run of the app
 * See [link](https://hadiyarajesh.hashnode.dev/pre-populating-room-database-with-static-data-in-android-using-hilt-di) for more on pre-populating a Room database with static data
 */
class DbInitializer(
    private val daoHolder: Provider<DaoHolder>,
    ) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        //Insert initial data into the database
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
        courseDao.insertOrUpdateCourse(*DefaultCourses.toTypedArray())
    }

    private suspend fun insertDefaultBooks(bookDao: BookDao){
        bookDao.insertOrUpdateBook(*DefaultBooks.toTypedArray())
    }
}


/**
 * Default books as initial list of books to insert into database
 */
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

/**
 * Default categories as initial list of categories to insert into database
 */
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

/**
 * Default courses as initial list of courses to insert into database
 */
val DefaultCourses = listOf(
    Course("Compose Animations", "Mehdi Haghgoo", R.drawable.compose_animations, null, "Learn how to implement UI animations in Jetpack Compose", instructorImgRes = R.drawable.mehdi),
    Course("The Complete Android Animations Course with Kotlin", "Mehdi Haghgoo", R.drawable.android_animations, null, "This course will teach you how to create animations for Views in Android. You will also learn about activity transitions and more.", instructorImgRes = R.drawable.mehdi),
    Course("Jetpack Compose Developer Course", "Mehdi Haghgoo", R.drawable.compose_dev_course, null, "The course covers the basics of UI development using Android's Jetpack Compose toolkit", instructorImgRes = R.drawable.mehdi),
    Course("The Complete Containers Course for Developers", "Mehdi Haghgoo", null, null, "With this course, you will learn what Linux containers are and how you can create and manage them.", instructorImgRes = R.drawable.mehdi)
)
