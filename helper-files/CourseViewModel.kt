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

package com.droidcon.deeplinksnav.ui.course

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.deeplinksnav.data.CourseRepository
import com.droidcon.deeplinksnav.data.local.database.Course
import com.droidcon.deeplinksnav.ui.course.CourseUiState.Error
import com.droidcon.deeplinksnav.ui.course.CourseUiState.Loading
import com.droidcon.deeplinksnav.ui.course.CourseUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    val uiState: StateFlow<CourseUiState> = courseRepository
        .courses.map<List<Course>, CourseUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private var _selectedCourse: MutableStateFlow<Course?> = MutableStateFlow(null)
    val selectedCourse: StateFlow<Course?>
        get() = _selectedCourse

    /**
     * Find the course given by [name] and set it as current selected course
     * @param name Name of the course to be set as the current course
     */
    fun updateSelectedCourseByName(name: String){
        viewModelScope.launch {
            val find = courseRepository.getCourseByName(name)
            Log.d("CourseViewModel", "updateSelectedCourseByName: Found course: ${find?.name}")
            find?.let{
                _selectedCourse.value = it
            }
        }
    }

    fun addCourse(course: Course) {
        viewModelScope.launch {
            courseRepository.add(course)
        }
    }

}

sealed interface CourseUiState {
    data object Loading : CourseUiState
    data class Error(val throwable: Throwable) : CourseUiState
    data class Success(val data: List<Course>) : CourseUiState
}
