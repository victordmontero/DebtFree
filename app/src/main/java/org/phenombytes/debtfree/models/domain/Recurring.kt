package org.phenombytes.debtfree.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.phenombytes.debtfree.enums.RepeatType

@Entity
data class Recurring(
    val amount: Double,
    val fromAccount: Account,
    val toCategory: Category,
    val repeatType: RepeatType,
    val notes: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
}
