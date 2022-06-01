package org.phenombytes.debtfree.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.models.domain.Account
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(accountDao: AccountDao) : ViewModel() {


    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private var _accounts = MutableLiveData<List<Account>>()
    val accounts:LiveData<List<Account>>
        get() = _accounts

    init {
        Log.d("AccountViewModel", "-> $accountDao")
        var accountList = mutableListOf(
            Account("Wallet",false),
            Account("Cash",false),
            Account("Savings",false))

        accountList[0].id = 1
        accountList[1].id = 2
        accountList[2].id = 3

        _accounts.value = accountList
    }

    fun addAccount(account: Account) = scope.launch {

    }

}