package org.phenombytes.debtfree.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    val name: String,
    val balance: Double = 0.0,
    val isFavorite: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id:Long? = null
}
