package org.phenombytes.debtfree.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.dao.OperationDao
import org.phenombytes.debtfree.models.domain.Account
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val operationDao: OperationDao) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    val accounts: LiveData<List<Account>>
        get() = accountDao.getAllAccounts()

    val accountsTotal: LiveData<Double>
        get() = accountDao.getAccountsTotal()

    private val _addAccountEvent = MutableLiveData<Boolean>()
    val addAccountEvent: LiveData<Boolean>
        get() = _addAccountEvent

    private val _editAccountEvent = MutableLiveData<Boolean>()
    val editAccountEvent: LiveData<Boolean>
        get() = _editAccountEvent

    private val _lastOperationWasSuccessful = MutableLiveData<Boolean?>(null)
    val lastOperationWasSuccessful: LiveData<Boolean?>
        get() = _lastOperationWasSuccessful

    init {
        _addAccountEvent.postValue(false)
        _editAccountEvent.postValue(false)
    }

    fun showAddAccountDialog() {
        _addAccountEvent.postValue(true)
    }

    fun doneShowAddAccountDialog() {
        _addAccountEvent.postValue(false)
    }

    fun showEditAccountDialog() {
        _editAccountEvent.postValue(true)
    }

    fun doneShowEditAccountDialog() {
        _editAccountEvent.postValue(false)
    }

    fun addAccount(accountName: String, balance: Double, isFavorite: Boolean, iconColor: Int = Color.BLACK) {
        try {
            if (accountName.isNotEmpty() && accountName.isNotBlank())
                scope.launch {
                    accountDao.insertAccount(Account(accountName, balance, isFavorite, iconColor))
                }
            else throw Exception("Account name must not be empty")
            _lastOperationWasSuccessful.postValue(true)
        } catch (ex: Exception) {
            _lastOperationWasSuccessful.postValue(false)
            ex.printStackTrace()
        }
    }

    fun deleteAccount(accountId: Long) {
        try {
            if (accountId > 0) {
                scope.launch {
                    val account = accountDao.getAccount(accountId)
                    operationDao.deleteOperationByAccountId(accountId)
                    accountDao.deleteAccount(account)
                }
            } else throw Exception("Invalid account id")
            _lastOperationWasSuccessful.postValue(true)
        } catch (ex: Exception) {
            _lastOperationWasSuccessful.postValue(false)
        }
    }

    fun editAccount(accountId: Long, accountName: String, balance: Double, isFavorite: Boolean, iconColor: Int = Color.BLUE) {
        try {
            if (accountId > 0 && accountName.isNotEmpty()) {
                scope.launch {
                    val newAccount = Account(accountName, balance, isFavorite, iconColor)
                    newAccount.accountId = accountId
                    accountDao.updateAccount(newAccount)
                }
            } else throw Exception("Invalid account id")
            _lastOperationWasSuccessful.postValue(true)
        } catch (ex: Exception) {
            _lastOperationWasSuccessful.postValue(false)
        }
    }

    fun getAccount(id:Long) = accountDao.getAccount(id)

    companion object {
        private const val TAG = "AccountViewModel"
    }
}