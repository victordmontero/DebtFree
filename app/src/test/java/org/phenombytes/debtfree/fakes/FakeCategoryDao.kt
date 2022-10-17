package org.phenombytes.debtfree.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.phenombytes.debtfree.dao.CategoryDao
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.relations.CategoryWithOperations


class FakeCategoryDao(var categories : MutableList<Category> = mutableListOf()) : CategoryDao {
    private var autoIndex: Long = 1
    private val observableCategories = MutableLiveData<List<Category>>()

    init {
        if(categories.isNotEmpty())
            refreshLiveData()
    }

    fun refreshLiveData() {
        observableCategories.postValue(categories)
    }

    override suspend fun insertCategory(category: Category): Long {
        category.categoryId = autoIndex++
        categories.add(category)
        refreshLiveData()
        return category.categoryId!!
    }

    override suspend fun updateCategory(category: Category): Int {
        categories.replaceAll {
            if (it.categoryId == category.categoryId) {
                category
            } else {
                it
            }
        }
        return 1
    }

    override suspend fun deleteCategory(category: Category): Int {
        categories.remove(category)
        refreshLiveData()
        return 1
    }

    override suspend fun deleteCategory(id: Long): Int {
        categories.removeIf { cat -> cat.categoryId == id }
        refreshLiveData()
        return 1
    }

    override fun getAllCategory(): LiveData<List<Category>> = observableCategories

    override fun getAllCategoryByType(type: CategoryType): LiveData<List<Category>>
        = MutableLiveData<List<Category>>(categories.filter { cat -> cat.type == type })

    override fun getCategory(id: Long): Category
        = categories.first { cat -> cat.categoryId == id }

    override fun getCategoryWithOperations(id: Long): LiveData<CategoryWithOperations> =
        MutableLiveData(CategoryWithOperations(Category("Test"), emptyList()))
}