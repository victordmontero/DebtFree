package org.phenombytes.debtfree.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.phenombytes.debtfree.models.domain.Operation
import org.phenombytes.debtfree.models.domain.relations.OperationAndAccount
import org.phenombytes.debtfree.models.domain.relations.OperationAndCategory

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: Operation):Long

    @Update
    suspend fun updateOperation(operation: Operation):Int

    @Delete
    suspend fun deleteOperation(operation: Operation):Int

    @Query("SELECT * FROM operations ORDER BY operationId")
    fun getOperations():LiveData<List<Operation>>

    @Query("SELECT * FROM operations WHERE operationId = :operId")
    suspend fun getOperation(operId:Long):Operation

    @Query("DELETE FROM operations WHERE operationId = :operId")
    suspend fun deleteOperation(operId:Long):Int

    @Transaction
    @Query("SELECT * FROM operations WHERE operationId = :operId")
    fun getOperationWithAccounts(operId: Long):LiveData<OperationAndAccount>

    @Transaction
    @Query("SELECT * FROM operations WHERE operationId = :operId")
    fun getOperationWithCategory(operId: Long):LiveData<OperationAndCategory>
}