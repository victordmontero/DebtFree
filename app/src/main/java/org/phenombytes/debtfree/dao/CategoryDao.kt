package org.phenombytes.debtfree.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.relations.CategoryWithOperations

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category): Int

    @Delete
    suspend fun deleteCategory(category: Category): Int

    @Query("DELETE FROM categories WHERE categoryId = :id")
    suspend fun deleteCategory(id: Long): Int

    @Query("SELECT * FROM categories ORDER BY categoryId")
    fun getAllCategory(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY categoryId")
    fun getAllCategoryByType(type: CategoryType): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    fun getCategory(id: Long): Category

    @Transaction
    @Query("SELECT * FROM categories WHERE categoryId = :id")
    fun getCategoryWithOperations(id: Long): LiveData<CategoryWithOperations>
}