package org.phenombytes.debtfree.dao

import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation
import org.phenombytes.debtfree.enums.*
import java.util.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class OperationDaoTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DebtFreeDatabase
    private lateinit var dao: OperationDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DebtFreeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.operationDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertOperationWithCategoryTest() = runTest {
        val cate = Category("Shopping")
        cate.categoryId = database.categoryDao().insertCategory(cate)
        val acct = Account("Card",25000.00, true)
        acct.accountId = database.accountDao().insertAccount(acct)
        val oper = Operation(
            "Test",
            10000.00,
            Date(2022,7,26),
            OperationType.Expense,
            acct.accountId,
            null,
            cate.categoryId
        )

        val id = dao.insertOperation(oper)

        val operation = dao.getOperation(id)
        val operation2 = dao.getOperationWithCategory(id).getOrAwaitValue()

        assert(operation.operationId == id)
        assert(operation.amount == 10000.00)
        assert(operation.description == "Test")
        assert(operation.date == Date(2022,7,26))
        assert(operation.fromAccountId == acct.accountId)
        assert(operation.categoryId == cate.categoryId)

        assert(operation2.operation.operationId == id)
        assert(operation2.operation.amount == 10000.00)
        assert(operation2.operation.description == "Test")
        assert(operation2.operation.date == Date(2022,7,26))
        assert(operation2.operation.fromAccountId == acct.accountId)
        assert(operation2.operation.categoryId == cate.categoryId)

        assert(operation2.category?.categoryName == "Shopping")
        assert(operation2.fromAccount.accountName == "Card")
        assert(operation2.fromAccount.balance == 25000.00)
        assert(operation2.fromAccount.accountIsFav == true)

    }

    @Test
    fun insertOperationWithAccountsTest() = runTest {
        val fromAcct = Account("Payroll",25000.00, true)
        fromAcct.accountId = database.accountDao().insertAccount(fromAcct)
        val toAcct = Account("Wallet",5000.00)
        toAcct.accountId = database.accountDao().insertAccount(toAcct)
        val oper = Operation(
            "Test",
            10000.00,
            Date(2022,7,26),
            OperationType.Expense,
            fromAcct.accountId,
            toAcct.accountId,
            null
        )

        val id = dao.insertOperation(oper)

        val operation = dao.getOperation(id)
        val operation2 = dao.getOperationWithAccounts(id).getOrAwaitValue()

        assert(operation.operationId == id)
        assert(operation.amount == 10000.00)
        assert(operation.description == "Test")
        assert(operation.date == Date(2022,7,26))
        assert(operation.fromAccountId == fromAcct.accountId)
        assert(operation.toAccountId == toAcct.accountId)

        assert(operation2.operation.operationId == id)
        assert(operation2.operation.amount == 10000.00)
        assert(operation2.operation.description == "Test")
        assert(operation2.operation.date == Date(2022,7,26))
        assert(operation2.operation.fromAccountId == fromAcct.accountId)
        assert(operation2.operation.toAccountId == toAcct.accountId)

        assert(operation2.fromAccount.accountName == "Payroll")
        assert(operation2.fromAccount.balance == 25000.00)
        assert(operation2.fromAccount.accountIsFav == true)

        assert(operation2.toAccount.accountName == "Wallet")
        assert(operation2.toAccount.balance == 5000.00)
        assert(operation2.toAccount.accountIsFav == false)

    }

    @Test
    fun deleteOperationTest() = runTest {
        val cate = Category("Shopping")
        val acct = Account("Card")
        val oper = Operation(
            "Test",
            10000.00,
            Date(2022,7,26),
            OperationType.Expense,
            acct.accountId,
            null,
            cate.categoryId
        )
        val id = dao.insertOperation(oper)
        oper.operationId = id

        assert(dao.getOperations().getOrAwaitValue().contains(oper))

        dao.deleteOperation(oper)

        val operations = dao.getOperations().getOrAwaitValue()

        assert(!(operations.contains(oper)))
    }
}