package org.phenombytes.debtfree.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.phenombytes.MainCoroutineRule
import org.phenombytes.debtfree.fakes.FakeAccountDao
import org.phenombytes.debtfree.models.domain.Account


@ExperimentalCoroutinesApi
class AccountViewModelTest {

    private val TAG = "AccountViewModelTest"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var viewModel: AccountViewModel? = null
    private var fakeAccountDao: FakeAccountDao? = null

    @Before
    fun setUp() {
        fakeAccountDao = FakeAccountDao()
        viewModel = AccountViewModel(fakeAccountDao!!)
    }

    @After
    fun tearDown(){
        fakeAccountDao = null
        viewModel = null
    }

    @Test
    fun `insert account with all fields, returns true`(){
        viewModel?.addAccount("Wallet",0.0,false)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)
    }

    @Test
    fun `insert account with empty account name, returns false`(){
        viewModel?.addAccount("",0.0,false)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `edit account with all fields, returns true`(){
        viewModel?.addAccount("Wallet",0.0,false)
        var value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)

        viewModel?.editAccount(1,"Cartera",1000.0,true)
        value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)
    }

    @Test
    fun `edit account with empty account name, returns false`(){
        viewModel?.addAccount("",0.0,false)
        var value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))

        viewModel?.editAccount(1,"",0.0,false)
        value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `delete account with id zero, returns false`(){
        val accounts = mutableListOf(
            Account("Wallet",500.0, true),
            Account("Credit",-15000.0, false),
            Account("Saving",900000.0, false))

        viewModel = AccountViewModel(FakeAccountDao(accounts))

        viewModel?.deleteAccount(0)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `delete account with negative id, returns false`(){
        val accounts = mutableListOf(
            Account("Wallet",500.0, true),
            Account("Credit",-15000.0, false),
            Account("Saving",900000.0, false))

        viewModel = AccountViewModel(FakeAccountDao(accounts))

        viewModel?.deleteAccount(-1)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `delete account with positive id, returns true`(){
        val accounts = mutableListOf(
            Account("Wallet",500.0, true),
            Account("Credit",-15000.0, false),
            Account("Saving",900000.0, false))

        var i : Long = 1
        accounts.forEach { a -> a.accountId = i++ }

        viewModel = AccountViewModel(FakeAccountDao(accounts))

        viewModel?.deleteAccount(1)
        val result = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(result!!)
        val values = viewModel?.accounts?.getOrAwaitValueUnitTest()
        Thread.sleep(1)
//        println("count:${values?.count()}")
        val count = values?.count()
//        assert(result!!)
//        println("count:${values?.count()}")
        assert(count == 2)
    }
}