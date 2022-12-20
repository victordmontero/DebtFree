package org.phenombytes.debtfree.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueUnitTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.phenombytes.MainCoroutineRule
import org.phenombytes.debtfree.enums.OperationType
import org.phenombytes.debtfree.fakes.FakeAccountDao
import org.phenombytes.debtfree.fakes.FakeCategoryDao
import org.phenombytes.debtfree.fakes.FakeOperationDao
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation
import java.util.*

class OperationViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var viewModel: OperationViewModel? = null
    private var fakeOperationDao: FakeOperationDao? = null
    private var fakeAccountDao: FakeAccountDao? = null
    private var fakeCategoryDao: FakeCategoryDao? = null

    @Before
    fun setUp() {
        fakeOperationDao = FakeOperationDao()
        fakeAccountDao = FakeAccountDao()
        fakeCategoryDao = FakeCategoryDao()
        viewModel = OperationViewModel(fakeOperationDao!!, fakeAccountDao!!, fakeCategoryDao!!)
    }

    @After
    fun tearDown() {
        viewModel = null
        fakeOperationDao = null
    }

    @Test
    fun `insert operation with all fields, returns true`(){

        viewModel?.addOperation("Picapollo",
            215.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(value!!)
    }

    @Test
    fun `insert operation with all fields income, returns true`(){

        viewModel?.addOperation("Picapollo",
            215.00,
            Date(2022,9,15),
            OperationType.Income,1,1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(value!!)
    }

    @Test
    fun `insert operation without category, returns false`(){
        viewModel?.addOperation("Picapollo",
            215.00,
            Date(2022,9,15),
            OperationType.Expense,1,0)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(!value!!)
    }

    @Test
    fun `insert operation without accounts, returns false`(){
        viewModel?.addOperation("Picapollo",
            215.00,
            Date(2022,9,15),
            OperationType.Expense,0,1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(!value!!)
    }

    @Test
    fun `insert operation with amount zero, returns false`(){
        viewModel?.addOperation("Picapollo",
            0.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(!value!!)
    }

    @Test
    fun `insert operation with negative amount, returns false`(){
        viewModel?.addOperation("Picapollo",
            -10.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(!value!!)
    }

    @Test
    fun `delete operation, returns true`(){
        var operation = Operation("Picapollo",
            -10.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)
        var id = 0L

        runBlocking {
            id = fakeOperationDao?.insertOperation(operation) ?: 0
        }

        viewModel?.deleteOperation(id)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(value!!)
    }

    @Test
    fun `delete operation with negative id, returns false`(){
        var operation = Operation("Picapollo",
            -10.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)
        var id = 0L

        runBlocking {
            id = fakeOperationDao?.insertOperation(operation) ?: 0
        }

        viewModel?.deleteOperation(-1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(!value!!)
    }

    @Test
    fun `delete operation with zero id, returns false`(){
        var operation = Operation("Picapollo",
            -10.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)
        var id = 0L

        runBlocking {
            id = fakeOperationDao?.insertOperation(operation) ?: 0
        }

        viewModel?.deleteOperation(0)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        assert(!value!!)
    }

    @Test
    fun `insert operation decreases amount, returns true`(){
        val amount = 300.00
        var accountBalance = 0.00
        var id = 0L

        runBlocking {
            id = fakeAccountDao?.insertAccount(Account("Wallet", amount)) ?: 0
        }

        viewModel?.addOperation("Picapollo",
            215.00,
            Date(2022,9,15),
            OperationType.Expense,1,1)

        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()

        runBlocking {
            accountBalance = fakeAccountDao?.getAccount(id)?.balance ?: 0.00
        }

        assert(accountBalance == (amount-215.00))
        assert(value!!)
    }

}