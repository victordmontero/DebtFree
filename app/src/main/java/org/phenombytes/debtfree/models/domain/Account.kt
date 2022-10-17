package org.phenombytes.debtfree.models.domain

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    val accountName: String,
    val balance: Double = 0.0,
    val accountIsFav: Boolean = false,
    val accountIconColor: Int = Color.GRAY
){
    @PrimaryKey(autoGenerate = true)
    var accountId:Long? = null
    var accountIconResId: Int? = null
}
