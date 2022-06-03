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

    private lateinit var database : DebtFreeDatabase
    private lateinit var dao: AccountDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
        DebtFreeDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.accountDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertAccountTest() = runTest {
        val accountItem = Account("Wallet",0.00, true)
        dao.insertAccount(accountItem)

        val accounts = dao.getAllAccounts().getOrAwaitValue()

        assert(accounts.contains(accountItem))
    }

    @Test
    fun deleteAccountTest() = runTest {
        val accountItem = Account("Wallet",0.00, true)
        val accountId:Long? = dao.insertAccount(accountItem)
        accountItem.id = accountId
        dao.deleteAccount(accountItem)

        val accounts = dao.getAllAccounts().getOrAwaitValue()

        assert(!(accounts.contains(accountItem)))
    }

    @Test
    fun queryAccountByNameTest() = runTest {
        val accountItem1 = Account("Wallet", 0.00,true)
        val accountItem2 = Account("Savings", 0.00,true)
        val accountItem3 = Account("Cash", 0.00,false)

        dao.insertAccount(accountItem1)
        dao.insertAccount(accountItem2)
        dao.insertAccount(accountItem3)

        val accounts = dao.getAllAccounts().getOrAwaitValue()

        assert(accounts.count() == 3)
    }


}