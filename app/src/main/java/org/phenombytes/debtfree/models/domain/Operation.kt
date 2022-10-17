package org.phenombytes.debtfree.models.domain

import androidx.room.*
import org.phenombytes.debtfree.enums.OperationType
import java.util.*

@Entity(tableName = "operations")
data class Operation (
    val description: String,
    val amount: Double,
    val date: Date,
    val operationType: OperationType = OperationType.Expense,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val categoryId: Long? = null
){
    @PrimaryKey(autoGenerate = true)
    var operationId:Long? = null
}
