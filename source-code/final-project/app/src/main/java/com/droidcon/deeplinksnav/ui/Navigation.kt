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

package com.droidcon.deeplinksnav.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.droidcon.deeplinksnav.ui.book.BookDetails
import com.droidcon.deeplinksnav.ui.book.BookGrid
import com.droidcon.deeplinksnav.ui.book.BookUiState
import com.droidcon.deeplinksnav.ui.book.BookViewModel
import com.droidcon.deeplinksnav.ui.category.CategoryGrid
import com.droidcon.deeplinksnav.ui.category.CategoryUiState
import com.droidcon.deeplinksnav.ui.category.CategoryViewModel
import com.droidcon.deeplinksnav.ui.course.CourseDetails
import com.droidcon.deeplinksnav.ui.course.CourseGrid
import com.droidcon.deeplinksnav.ui.course.CourseUiState
import com.droidcon.deeplinksnav.ui.course.CourseViewModel

@Composable
fun MyLinksApp(
    appState: LinksAppState = rememberLinksAppState(),
    bookViewModel: BookViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val selectedCategory by categoryViewModel.selectedCategory.collectAsStateWithLifecycle()

    val selectedCourse by courseViewModel.selectedCourse.collectAsStateWithLifecycle()

    val selectedBook by bookViewModel.selectedBook.collectAsStateWithLifecycle()

    val categories by categoryViewModel.uiState.collectAsStateWithLifecycle()

    val courses by courseViewModel.uiState.collectAsStateWithLifecycle()

    val books by bookViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    NavHost(navController = appState.navController, startDestination = Screen.Landing.route) {
//        composable(route = Screen.Landing.route){
//            Welcome(onNavigate = {
//                appState.navigate(it)
//            })
//        }

        composable(route = Screen.Landing.route){
            Welcome(onNavigate = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setData(Uri.parse("pubs://linksapp/categories/books"))
                }
                context.startActivity(intent)
            })
        }


        composable(route = Screen.ItemDetails.route,
            arguments = listOf(
                navArgument("categoryName"){ type = NavType.StringType},
                navArgument("itemName"){ type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink{ uriPattern = "$BASE_URL_MYAPP/categories/{categoryName}/{itemName}" },
                navDeepLink { uriPattern = "$BASE_URL/categories/{categoryName}/{itemName}" }
            )){entry->

            val categoryName = entry.arguments?.getString("categoryName")
            val itemName = entry.arguments?.getString("itemName")

            categoryName?.let{
                categoryViewModel.updateSelectedCategoryByName(it)
            }

            selectedCategory?.name?.let{catName->
                itemName?.let{
                    when(catName){
                        CATEGORY_COURSES -> {
                            courseViewModel.updateSelectedCourseByName(itemName)
                            selectedCourse?.let{
                                CourseDetails(course = it) {
                                    appState.navigateBack()
                                }
                            }
                        }

                        CATEGORY_BOOKS -> {
                            bookViewModel.updateSelectedBookByName(itemName)
                            selectedBook?.let{
                                BookDetails(book = it) {
                                    appState.navigateBack()
                                }
                            }
                        }
                    }
                }
            }
        }


        composable(route = Screen.Category.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "$BASE_URL/categories" },
                navDeepLink { uriPattern = "$BASE_URL_MYAPP/categories" }
            )
        ) {
            if (categories is CategoryUiState.Success) {
                CategoryGrid(
                    items = (categories as CategoryUiState.Success).data,
                    onCategorySelected = { category ->
//                        This will navigate to a different category based on user selection
                        categoryViewModel.updateSelectedCategoryByName(category.name)
//                        appState.updateSelectedCategory(category)
                    },
                    onNavigate = { categoryName ->
                        appState.navigateToSubCategory(categoryName)
                    },
                    onBack = {
                        appState.navigateBack()
                    }
                )
            }
            else {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
        }

        //Route for all sub-categories including courses and books, depending on passed parameter
        composable(route = Screen.SubCategory.route,
            arguments = listOf(
                navArgument("categoryName") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "$BASE_URL/categories/{categoryName}" }, //The app link
                navDeepLink { uriPattern = "$BASE_URL_MYAPP/categories/{categoryName}" } //The deep link
            )) { backStackEntry ->
            backStackEntry.arguments?.getString("categoryName")?.let {
//                categoryViewModel.updateSelectedCategory()
                //We extract the category name from the back stack entry here,
                //but for details screen of courses and books we don't need to extract,
                //because we know from the view model which ones are selected

                //We only update current selected category when it is coming from a deep link or when an item is selected from the category grid
                val categoryName = backStackEntry.arguments?.getString("categoryName")
                categoryName?.let{
                    //Update selected category from the link
                    categoryViewModel.updateSelectedCategoryByName(it)
                }
                when (categoryName?.lowercase()) {
                    Screen.Course.route ->
                        if (courses is CourseUiState.Success) {
                            CourseGrid(
                                items = (courses as CourseUiState.Success).data,
                                onCourseSelected = { course ->
                                    courseViewModel.updateSelectedCourseByName(course.name)
                                },
                                onNavigate = { courseName ->
                                    appState.navigateToCourse(courseName)
                                },
                                onBack = {
                                    appState.navigateBack()
                                })
                        }
                        else {
                            LoadingIndicator()
                        }

                    Screen.Book.route ->
                        if (books is BookUiState.Success) {
                            BookGrid(
                                (books as BookUiState.Success).data,
                                onBookSelected = { book ->
                                    bookViewModel.updateSelectedBookByName(book.name)
//                            appState.updateSelectedBook(book)
                                },
                                onNavigate = { bookName ->
                                    appState.navigateToBook(bookName)
                                },
                                onBack = {
                                    appState.navigateBack()
                                })
                        }
                        else {
                            LoadingIndicator()
                        }
                }
            }

        }


    }
}



