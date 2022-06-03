package org.phenombytes.debtfree.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.dao.DebtFreeDatabase
import org.phenombytes.debtfree.models.domain.Account
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val accountDao: AccountDao) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    val accounts:LiveData<List<Account>>
        get() = accountDao.getAllAccounts()

    private val _addAccountEvent = MutableLiveData<Boolean>()
    val addAccountEvent: LiveData<Boolean>
        get() = _addAccountEvent

    init {
        Log.d("AccountViewModel", "-> $accountDao")
        _addAccountEvent.postValue(false)
    }

    fun showAddAccountDialog(){
        _addAccountEvent.postValue(true)
    }

    fun doneShowAddAccountDialog(){
        _addAccountEvent.postValue(false)
    }

    suspend fun addAccountAsync(account: Account){
        withContext(Dispatchers.IO){
            val id = accountDao.insertAccount(account)
        }
    }

    fun addAccount(accountName: String, balance: Double, isFavorite: Boolean) {
        scope.launch {
            addAccountAsync(Account(accountName,balance,isFavorite))
        }
    }



}