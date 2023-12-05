package com.droidcon.deeplinksnav.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.deeplinksnav.data.BookRepository
import com.droidcon.deeplinksnav.data.local.database.Book
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
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {

    val uiState: StateFlow<BookUiState> = bookRepository
        .books.map<List<Book>, BookUiState>(BookUiState::Success)
        .catch { emit(BookUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BookUiState.Loading)

    private val _selectedBook = MutableStateFlow<Book?>(value = null)
    val selectedBook: StateFlow<Book?>
        get() = _selectedBook

    fun updateSelectedBookByName(name: String){
        viewModelScope.launch{
            val selectedBook: Book? = bookRepository.getBookByName(name)
            if(selectedBook != null){
                _selectedBook.value = selectedBook
            }
        }
    }



    fun addBook(book: Book) {
        viewModelScope.launch {
            bookRepository.add(book)
        }
    }
}

sealed interface CategoryUiState {
    data object Loading : CategoryUiState
    data class Error(val throwable: Throwable) : CategoryUiState
    data class Success(val data: List<Category>) : CategoryUiState
}

sealed interface BookUiState {
    data object Loading : BookUiState
    data class Error(val throwable: Throwable) : BookUiState
    data class Success(val data: List<Book>) : BookUiState
}
