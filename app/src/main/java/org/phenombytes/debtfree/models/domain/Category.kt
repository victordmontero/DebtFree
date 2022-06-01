package org.phenombytes.debtfree.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.phenombytes.debtfree.enums.CategoryType

@Entity
data class Category(
    val name: String,
    val type: CategoryType,
    val isFavorite: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
}
