package com.droidcon.deeplinksnav.data

import com.droidcon.deeplinksnav.R
import com.droidcon.deeplinksnav.data.local.database.Book
import com.droidcon.deeplinksnav.data.local.database.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

interface BookRepository {
    val books: Flow<List<Book>>

    suspend fun add(book: Book)

    suspend fun getBookByName(name: String): Book?
}

@Singleton
class BookLocalDataSource @Inject constructor(){
    val books: List<Book> = DefaultBooks
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

class DummyBookRepository @Inject constructor(
    private val bookDataSource: BookLocalDataSource
): BookRepository{
    override val books: Flow<List<Book>>
        get() = flowOf(bookDataSource.books)

    override suspend fun add(book: Book) {
        throw NotImplementedError()
    }

    override suspend fun getBookByName(name: String): Book? {
        return bookDataSource.books.find{cat->
            val processedName = cat.name.filterNot{ it.isWhitespace() }.lowercase()
            val nameToTest = name.filterNot { it.isWhitespace() }.lowercase()
            nameToTest == processedName
        }
    }

}

class DefaultBookRepository @Inject constructor(private val bookDao: BookDao): BookRepository{
    override val books: Flow<List<Book>>
        get() = bookDao.getBooks()

    override suspend fun add(book: Book) {
        bookDao.insertBook(book)
    }

    override suspend fun getBookByName(name: String): Book? {
        var foundBook: Book? = null
        books.collect {list->
            foundBook = list.find { book ->
                val processedName = book.name.filterNot { it.isWhitespace() }.lowercase()
                val nameToTest = name.filterNot { it.isWhitespace() }.lowercase()
                nameToTest == processedName
            }
        };
        return foundBook
    }

}