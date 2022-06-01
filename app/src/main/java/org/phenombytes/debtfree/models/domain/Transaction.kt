package org.phenombytes.debtfree.models.domain

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Transaction(
    val date: Date,
    val amount: Double,
    val fromAccount: Account,
    val toCategory: Category,
    val notes: String,
    val billImage: Bitmap
){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
}
