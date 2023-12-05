package com.droidcon.deeplinksnav.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.deeplinksnav.data.CategoryRepository
import com.droidcon.deeplinksnav.data.local.database.Category
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
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
    ): ViewModel() {

    val uiState: StateFlow<CategoryUiState> = categoryRepository
        .categories.map<List<Category>, CategoryUiState>(CategoryUiState::Success)
        .catch { emit(CategoryUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryUiState.Loading)

    private val _selectedCategory = MutableStateFlow<Category?>(value = null)

    /**
     * Keeps track of the current selected category from the category grid
     */
    val selectedCategory: StateFlow<Category?>
        get() = _selectedCategory

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.add(category)
        }
    }

    /**
     * Update the selected category to the category with the name [categoryName]
     */
    fun updateSelectedCategoryByName(categoryName: String) {
        viewModelScope.launch {
            val search = categoryRepository.getCategoryByName(categoryName)
            search?.let{category->
                _selectedCategory.value = category
            }
        }
    }
}

sealed interface CategoryUiState {
    data object Loading : CategoryUiState
    data class Error(val throwable: Throwable) : CategoryUiState
    data class Success(val data: List<Category>) : CategoryUiState
}
