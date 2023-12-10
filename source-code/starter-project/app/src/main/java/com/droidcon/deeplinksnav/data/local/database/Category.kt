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
 * A single category entity
 */
@Entity
data class Category(
    val name: String,
    val coverRes: Int?,
    val coverUrl: String?,
    val description: String?,
) {
    @PrimaryKey
    var uid: String = UUID.randomUUID().toString()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY uid DESC LIMIT 10")
    fun getCategories(): Flow<List<Category>>

    @Upsert
    suspend fun insertOrUpdateCategory(vararg item: Category)
}
