package org.phenombytes.debtfree.models.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation

data class OperationAndCategory(
    @Embedded val operation: Operation,
    @Relation(
        parentColumn = "fromAccountId",
        entityColumn = "accountId"
    )
    val fromAccount: Account,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: Category
)
