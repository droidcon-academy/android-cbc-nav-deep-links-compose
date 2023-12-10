package com.droidcon.deeplinksnav.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * A single book entity
 */
@Entity
data class Book(
    val name: String,
    val author: String,
    val authorPicRes: Int?,
    val authorPicUrl: String?,
    val coverRes: Int?,
    val coverUrl: String?,
    val description: String?,
) {
    @PrimaryKey
    var uid: String = UUID.randomUUID().toString()
}

@Dao
interface BookDao {
    @Query("SELECT * FROM book ORDER BY uid DESC LIMIT 10")
    fun getBooks(): Flow<List<Book>>

    @Upsert
    suspend fun insertOrUpdateBook(vararg item: Book)
}
