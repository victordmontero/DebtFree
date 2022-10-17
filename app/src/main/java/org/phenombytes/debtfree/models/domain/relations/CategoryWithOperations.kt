package org.phenombytes.debtfree.models.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation

data class CategoryWithOperations(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val operations: List<Operation>
)
