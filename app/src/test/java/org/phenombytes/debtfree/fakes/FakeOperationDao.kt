package org.phenombytes.debtfree.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.phenombytes.debtfree.dao.OperationDao
import org.phenombytes.debtfree.models.domain.Operation
import org.phenombytes.debtfree.models.domain.relations.OperationAndAccount
import org.phenombytes.debtfree.models.domain.relations.OperationAndCategory

class FakeOperationDao(var operations : MutableList<Operation> = mutableListOf()) : OperationDao{
    private var autoIndex: Long = 1
    private val observableOperation = MutableLiveData<List<Operation>>()

    init {
        if(operations.isEmpty())
            refreshLiveData()
    }

    fun refreshLiveData(){
        observableOperation.postValue(operations)
    }

    override suspend fun insertOperation(operation: Operation): Long {
        operation.operationId = autoIndex++
        operations.add(operation)
        refreshLiveData()
        return operation.operationId!!
    }

    override suspend fun updateOperation(operation: Operation): Int {
        operations.replaceAll {
            if(it.categoryId == operation.categoryId){
                operation
            }else{
                it
            }
        }
        refreshLiveData()
        return 1
    }

    override suspend fun deleteOperation(operation: Operation): Int {
        operations.remove(operation)
        refreshLiveData()
        return 1
    }

    override suspend fun deleteOperation(operId: Long): Int {
        operations.removeIf { o -> o.operationId == operId }
        refreshLiveData()
        return 1
    }

    override fun getOperations(): LiveData<List<Operation>> =
        observableOperation

    override suspend fun getOperation(operId: Long): Operation =
        operations.first { o -> o.operationId == operId }

    override fun getOperationWithAccounts(operId: Long): LiveData<OperationAndAccount> {
        TODO("Not yet implemented")
    }

    override fun getOperationWithCategory(operId: Long): LiveData<OperationAndCategory> {
        TODO("Not yet implemented")
    }

}