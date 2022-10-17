package org.phenombytes.debtfree.models.domain

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.phenombytes.debtfree.enums.CategoryType

@Entity(tableName = "categories")
data class Category(
    val categoryName: String,
    val type: CategoryType = CategoryType.Expense,
    val categoryIsFav: Boolean = false,
    val categoryIconColor: Int = Color.GRAY
){
    @PrimaryKey(autoGenerate = true)
    var categoryId:Long? = null
    var categoryIconResId: Int? = null
}
