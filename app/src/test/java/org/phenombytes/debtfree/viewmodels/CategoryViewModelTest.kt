package org.phenombytes.debtfree.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.phenombytes.MainCoroutineRule
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.fakes.FakeCategoryDao
import org.phenombytes.debtfree.models.domain.Category

@ExperimentalCoroutinesApi
class CategoryViewModelTest {

    private val TAG = "CategoryViewModelTest"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var viewModel: CategoryViewModel? = null
    private var fakeCategoryDao: FakeCategoryDao? = null

    @Before
    fun setUp() {
        fakeCategoryDao = FakeCategoryDao()
        viewModel = CategoryViewModel(fakeCategoryDao!!)
    }

    @After
    fun tearDown() {
        fakeCategoryDao = null
        viewModel = null
    }

    @Test
    fun `insert category with all fields, returns true`(){
        viewModel?.addCategory("Salary", CategoryType.Income,true)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)
    }

    @Test
    fun `insert category with empty category name, returns false`(){
        viewModel?.addCategory("",CategoryType.Expense,false)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `edit category with all fields, returns true`(){
        viewModel?.addCategory("Wallet",CategoryType.Income,false)
        var value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)

        viewModel?.editCategory(1,"Family", CategoryType.Expense,true)
        value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)
    }

    @Test
    fun `edit category with empty category name, returns false`(){
        viewModel?.addCategory("A",CategoryType.Expense, false)
        var value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)

        viewModel?.editCategory(1,"",CategoryType.Income,true)
        value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `edit category with id zero, returns false`(){
        viewModel?.addCategory("A",CategoryType.Expense, false)
        var value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)

        viewModel?.editCategory(0,"Leisure",CategoryType.Expense,false)
        value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `edit category with negative id, returns false`(){
        viewModel?.addCategory("A",CategoryType.Expense, false)
        var value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(value!!)

        viewModel?.editCategory(-1,"Restaurant",CategoryType.Expense,false)
        value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `delete category with id zero, returns false`(){
        val categories = mutableListOf(
            Category("Salary",CategoryType.Income, true),
            Category("Gifts",CategoryType.Expense, false),
            Category("Diezmo",CategoryType.Expense, true))

        categories.forEach { cat ->
            viewModel?.addCategory(cat.categoryName, cat.type, cat.categoryIsFav)
        }

        viewModel?.deleteCategory(0)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `delete category with negative id, returns false`(){
        val categories = mutableListOf(
            Category("Salary",CategoryType.Income, true),
            Category("Gifts",CategoryType.Expense, false),
            Category("Diezmo",CategoryType.Expense, true))

        categories.forEach { cat ->
            viewModel?.addCategory(cat.categoryName, cat.type, cat.categoryIsFav)
        }

        viewModel?.deleteCategory(-1)
        val value = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(!(value!!))
    }

    @Test
    fun `delete category with positive id, returns true`(){
        val categories = mutableListOf(
            Category("Salary",CategoryType.Income, true),
            Category("Gifts",CategoryType.Expense, false),
            Category("Diezmo",CategoryType.Expense, true))

        categories.forEach { cat ->
            viewModel?.addCategory(cat.categoryName, cat.type, cat.categoryIsFav)
        }

        viewModel?.deleteCategory(1)
        Thread.sleep(2)
        val result = viewModel?.lastOperationWasSuccessful?.getOrAwaitValueUnitTest()
        assert(result!!)
        val values = viewModel?.categories?.getOrAwaitValueUnitTest()
        val count = values?.count()
        assert(count == 2)
    }
}