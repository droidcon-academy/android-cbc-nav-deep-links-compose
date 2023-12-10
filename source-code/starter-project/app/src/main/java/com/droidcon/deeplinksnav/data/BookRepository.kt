package com.droidcon.deeplinksnav.data

import com.droidcon.deeplinksnav.data.local.database.DefaultBooks
import com.droidcon.deeplinksnav.data.local.database.Book
import com.droidcon.deeplinksnav.data.local.database.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton
/**
 * [Book] repository interface
 */
interface BookRepository {
    val books: Flow<List<Book>>

    suspend fun add(book: Book)

    suspend fun getBookByName(name: String): Book?
}

@Singleton
class BookLocalDataSource @Inject constructor(){
    val books: List<Book> = DefaultBooks
}


/**
 * Dummy implementation of [BookRepository] useful for testing the app
 */
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

/**
 * default implementation of [BookRepository] backed by [androidx.room.RoomDatabase] for use in production
 */
class DefaultBookRepository @Inject constructor(private val bookDao: BookDao): BookRepository{
    override val books: Flow<List<Book>>
        get() = bookDao.getBooks()

    override suspend fun add(book: Book) {
        bookDao.insertOrUpdateBook(book)
    }

    override suspend fun getBookByName(name: String): Book? {
        return books.first().find { book->
            book.name.filterNot { it.isWhitespace() }.lowercase() == name.filterNot { it.isWhitespace() }.lowercase()
        }
    }

}