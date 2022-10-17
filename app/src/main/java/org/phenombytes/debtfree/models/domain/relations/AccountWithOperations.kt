package org.phenombytes.debtfree.models.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Operation

data class AccountWithOperations(
    @Embedded
    val account: Account,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "fromAccountId"
    )
    val operations: List<Operation>
)
