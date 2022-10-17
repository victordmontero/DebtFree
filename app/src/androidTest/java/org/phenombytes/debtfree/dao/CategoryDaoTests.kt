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
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.enums.OperationType
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation
import java.util.*


@RunWith(AndroidJUnit4::class)
@SmallTest
class CategoryDaoTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DebtFreeDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DebtFreeDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.categoryDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertCategoryTest() = runTest{
        val cateId = dao.insertCategory(Category("Clothes"))

        val categories = dao.getAllCategory().getOrAwaitValue()
        val category = dao.getCategory(cateId)

        assert(categories.contains(category))
        assert(category.categoryId == cateId)
        assert(category.type == CategoryType.Expense)
        assert(category.categoryName == "Clothes")
        assert(!category.categoryIsFav)
    }

    @Test
    fun updateCategoryTest() = runTest{
        val cateId = dao.insertCategory(Category("Clothes"))

        val category = Category(
            "Picota",
            CategoryType.Income,
            true)
        category.categoryId = cateId

        dao.updateCategory(category)

        val updatedCategory = dao.getCategory(cateId)

        assert(updatedCategory.categoryId == cateId)
        assert(updatedCategory.categoryName == "Picota")
        assert(updatedCategory.type == CategoryType.Income)
        assert(updatedCategory.categoryIsFav)
    }

    @Test
    fun deleteCategoryTest() = runTest {
        val category = Category("Food")
        val id: Long? = dao.insertCategory(category)
        category.categoryId = id
        dao.deleteCategory(category)

        val categories = dao.getAllCategory().getOrAwaitValue()
        assert(!(categories.contains(category)))
    }

    @Test
    fun tansferToCategoryTest() = runTest {
        val category = Category("Food")
        val id: Long? = dao.insertCategory(category)
        val account = Account("Food",100.0)
        val acccountId: Long? = database.accountDao().insertAccount(account)

        val operation = Operation(
            "Test",
            1.00,
            Date(),
            OperationType.Expense,
            acccountId,
            null,
            id
        )
        database.operationDao().insertOperation(operation)

        val categories = dao.getCategoryWithOperations(id!!).getOrAwaitValue()
        val operations = categories.operations
        assert(categories.category.categoryName.equals("Food"))
        assert(operations.first().description.equals("Test"))
        assert(operations.first().amount == 1.00)
        assert(operations.first().fromAccountId == acccountId)
        assert(operations.first().categoryId == id)
        assert(operations.first().operationType == OperationType.Expense)
    }

    @Test
    fun tansferFromCategoryTest() = runTest {
        val category = Category("Food")
        val id: Long? = dao.insertCategory(category)
        val account = Account("Food",100.0)
        val acccountId: Long? = database.accountDao().insertAccount(account)

        val operation = Operation(
            "Test",
            1.00,
            Date(),
            OperationType.Income,
            null,
            acccountId,
            id
        )
        database.operationDao().insertOperation(operation)

        val categories = dao.getCategoryWithOperations(id!!).getOrAwaitValue()
        val operations = categories.operations
        assert(categories.category.categoryName.equals("Food"))
        assert(operations.first().description.equals("Test"))
        assert(operations.first().amount == 1.00)
        assert(operations.first().toAccountId == acccountId)
        assert(operations.first().categoryId == id)
        assert(operations.first().operationType == OperationType.Income)
    }
}