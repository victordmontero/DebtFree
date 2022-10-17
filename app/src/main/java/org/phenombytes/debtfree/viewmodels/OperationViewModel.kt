package org.phenombytes.debtfree.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.dao.OperationDao
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.enums.OperationType
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation
import org.phenombytes.debtfree.models.domain.relations.OperationAndAccount
import org.phenombytes.debtfree.models.domain.relations.OperationAndCategory
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    val operationDao: OperationDao,
    val accountDao: AccountDao
) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _lastOperationWasSuccessful = MutableLiveData<Boolean?>(null)
    val lastOperationWasSuccessful: LiveData<Boolean?>
        get() = _lastOperationWasSuccessful

    private var _operations = MutableLiveData<List<Any>>()
    val operations: LiveData<List<Any>>
        get() = _operations

    init {
        val account = Account("Wallet",500.0)
        account.accountId = 1
        val account1 = Account("Card",5000.0)
        account1.accountId = 2
        val category = Category("Food")
        category.categoryId = 1

        val operation = Operation(
            "Dee",
            110.0,
            Date(),
            OperationType.Expense,
            account.accountId,
            categoryId = category.categoryId
        ).also { it.operationId = 1 }

        val operation2 = Operation(
            "Daa",
            1000.00,
            Date(),
            OperationType.Transfer,
            account1.accountId,
            account.accountId
        ).also { it.operationId = 2 }

        val operationAndCategory = OperationAndCategory(
            operation,
            account,
            category
        )

        val operationAndAccount = OperationAndAccount(
            operation2,
            account,
            account1
        )

        _operations.postValue(
            listOf(
                operationAndCategory,
                operationAndAccount))
    }

    fun addOperation(
        desc: String,
        amount: Double,
        date: Date,
        type: OperationType,
        fromAccountId: Long,
        toAccountOrCateId: Long
    ) {
        try {
            if ((desc.isNotEmpty() || desc.isNotBlank())
                && amount > 0.00
                && fromAccountId > 0
                && toAccountOrCateId > 0
            )
                scope.launch {
                    lateinit var operation: Operation
                    lateinit var account: Account
                    lateinit var account2: Account
                    when (type) {
                        OperationType.Expense -> {
                            operation = Operation(
                                desc,
                                amount,
                                date,
                                type,
                                fromAccountId,
                                null,
                                toAccountOrCateId
                            )
                            accountDao.getAccount(fromAccountId).let {
                                account = Account(
                                    it.accountName,
                                    it.balance - amount,
                                    it.accountIsFav
                                )
                            }
                        }
                        OperationType.Income -> {
                            operation = Operation(
                                desc,
                                amount,
                                date,
                                type,
                                null,
                                fromAccountId,
                                toAccountOrCateId
                            )
                            accountDao.getAccount(fromAccountId).let {
                                account = Account(
                                    it.accountName,
                                    it.balance + amount,
                                    it.accountIsFav
                                )
                            }
                        }
                        OperationType.Transfer -> {
                            operation = Operation(
                                desc,
                                amount,
                                date,
                                type,
                                fromAccountId,
                                toAccountOrCateId
                            )
                            accountDao.getAccount(fromAccountId).let {
                                account = Account(
                                    it.accountName,
                                    it.balance - amount,
                                    it.accountIsFav
                                )
                            }
                            accountDao.getAccount(toAccountOrCateId).let {
                                account2 = Account(
                                    it.accountName,
                                    it.balance + amount,
                                    it.accountIsFav
                                )
                            }
                        }
                    }
                    operationDao.insertOperation(operation)
                    accountDao.updateAccount(account)
                    accountDao.updateAccount(account2)
                }
            else
                throw Exception("Missing value adding operation")

            _lastOperationWasSuccessful.postValue(true)
        } catch (ex: Exception) {
            _lastOperationWasSuccessful.postValue(false)
        }
    }

    fun deleteOperation(operationId: Long) {
        try {
            if (operationId > 0)
                scope.launch {
                    operationDao.deleteOperation(operationId)
                }
            else
                throw Exception("Operation Id must not be less than 1")

            _lastOperationWasSuccessful.postValue(true)
        } catch (ex: Exception) {
            _lastOperationWasSuccessful.postValue(false)
        }
    }

}