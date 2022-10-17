package org.phenombytes.debtfree.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.phenombytes.debtfree.models.domain.Account

@RunWith(AndroidJUnit4::class)
@SmallTest
class AccountDaoTests {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DebtFreeDatabase
    private lateinit var dao: AccountDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DebtFreeDatabase::class.java
        )
            .allowMainThreadQueries().build()
        dao = database.accountDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAccountTest() = runTest {
        val accountItem = Account("Wallet", 0.00, true)
        dao.insertAccount(accountItem)

        val accounts = dao.getAllAccounts().getOrAwaitValue()

        assert(accounts.contains(accountItem))
    }

    @Test
    fun deleteAccountTest() = runTest {
        val accountItem = Account("Wallet", 0.00, true)
        val accountId: Long? = dao.insertAccount(accountItem)
        accountItem.accountId = accountId
        dao.deleteAccount(accountItem)

        val accounts = dao.getAllAccounts().getOrAwaitValue()

        assert(!(accounts.contains(accountItem)))
    }

    @Test
    fun updateAccountTest() = runTest {
        val accountItem = Account("Wallet", 0.00, true)
        val accountId: Long? = dao.insertAccount(accountItem)
        accountItem.accountId = accountId

        var acct = Account("Cartera", 10.0, true)
        acct.accountId = accountId

        dao.updateAccount(acct)

        val accounts = dao.getAllAccounts().getOrAwaitValue()

        assert(accounts.contains(acct))
        acct = accounts.first { it.accountId == accountId }

        assert(acct.accountName == "Cartera")
        assert(acct.balance == 10.0)
        assert(acct.accountIsFav == true)
    }

    @Test
    fun getAccountTotalTest() = runTest {
        val accounts = listOf(
            Account("T1", 1000.00),
            Account("T1", 500.00),
            Account("T1", -2000.00)
        )
        accounts.forEach { dao.insertAccount(it) }

        assert(dao.getAccountsTotal().getOrAwaitValue() == -500.00)
    }



}