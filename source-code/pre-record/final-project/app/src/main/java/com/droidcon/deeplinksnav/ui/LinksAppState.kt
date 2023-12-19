package com.droidcon.deeplinksnav.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Screen class used to define destinations in the navigation graph
 */
sealed class Screen(val route: String) {
    data object Landing: Screen(route = "landing")
    data object Course: Screen(route = "courses")
    data object Category: Screen(route = "categories")

    data object SubCategory: Screen(route = "categories/{categoryName}"){
        fun createRoute(categoryName: String) = "categories/$categoryName"
    }
    data object Book: Screen(route = "books")

    //Used to display a details page for items like books and courses
    data object ItemDetails: Screen(route = "categories/{categoryName}/{itemName}"){
        fun createRoute(categoryName: String, itemName: String) = "categories/$categoryName/$itemName"
    }


}

@Composable
fun rememberLinksAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController){
    LinksAppState(navController)
}

class LinksAppState(
    val navController: NavHostController
){
    companion object{
        const val TAG = "LinksAppState"

        /**
         * Removes whitespaces from [name] and makes it lowercase to be used as route
         */
        fun normalize(name: String): String{
            return name.filterNot { it.isWhitespace() }.lowercase()
        }
    }



    /**
     * Navigate to course details page
     */
    fun navigateToCourse(courseName: String){
        Log.d(TAG, "navigateToCourse: $courseName")
        navController.navigate(Screen.ItemDetails.createRoute(CATEGORY_COURSES,courseName))
    }

    /**
     * Navigate to the selected sub-category
     */
    fun navigateToSubCategory(categoryName: String){
        Log.d(TAG, "navigateToSubCategory: $categoryName")
        navController.navigate(Screen.SubCategory.createRoute(normalize(categoryName)))
    }

    /**
     * Navigates to a book details
     */
    fun navigateToBook(bookName: String){
        Log.d(TAG, "navigateToBook: $bookName")
        navController.navigate(Screen.ItemDetails.createRoute(CATEGORY_BOOKS, bookName))
    }


    /**
     * Navigate back by popping one item off the back stack
     */
    fun navigateBack() {
        Log.d(TAG, "navigateBack: ")
        navController.popBackStack()
    }

    /**
     * Navigate to a screen specified by [route]
     */
    fun navigate(route: String) {
        Log.d(TAG, "navigate: $route")
        navController.navigate(route)
    }


}
