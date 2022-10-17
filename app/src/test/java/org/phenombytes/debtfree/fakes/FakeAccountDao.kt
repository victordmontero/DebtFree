package org.phenombytes.debtfree.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Operation
import org.phenombytes.debtfree.models.domain.relations.AccountWithOperations

class FakeAccountDao(var accounts: MutableList<Account> = mutableListOf()) : AccountDao {
    private var autoIndex: Long = 1
    private val observableAccounts = MutableLiveData<List<Account>>()

    init {
        if (accounts.isNotEmpty())
            refreshLiveData()
    }

    fun refreshLiveData() {
        observableAccounts.postValue(accounts)
    }

    override suspend fun insertAccount(account: Account): Long {
        account.accountId = autoIndex++
        accounts.add(account)
        refreshLiveData()
        return account.accountId!!
    }

    override suspend fun updateAccount(account: Account): Int {
        accounts.replaceAll {
            if (it.accountId == account.accountId) {
                account
            } else {
                it
            }
        }
        return 1
    }

    override suspend fun deleteAccount(account: Account): Int {
        accounts.remove(account)
        refreshLiveData()
        return 1
    }

    override fun getAllAccounts(): LiveData<List<Account>> = observableAccounts

    override fun getAccount(id: Long): Account = accounts.first { a -> a.accountId == id }

    override fun getAccountsTotal(): LiveData<Double> =
        MutableLiveData(accounts.sumOf { it.balance })

    override fun getAccountWithOperations(id: Long): LiveData<AccountWithOperations> =
        MutableLiveData(AccountWithOperations(Account("Test"), emptyList() ))

    override suspend fun getAllAccountsNormal(): List<Account> = accounts
}