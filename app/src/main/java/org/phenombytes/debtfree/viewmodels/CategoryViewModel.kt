package org.phenombytes.debtfree.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.phenombytes.debtfree.dao.CategoryDao
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.models.domain.Category
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryDao: CategoryDao) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _lastOperationWasSuccessful = MutableLiveData<Boolean?>(null)
    val lastOperationWasSuccessful: LiveData<Boolean?>
        get() = _lastOperationWasSuccessful

    val categories:LiveData<List<Category>>
        get() = categoryDao.getAllCategory()

    private val _addCategoryEvent = MutableLiveData<Boolean>()
    val addCategoryEvent : LiveData<Boolean>
        get() = _addCategoryEvent

    private val _editCategoryEvent = MutableLiveData<Boolean>()
    val editCategoryEvent : LiveData<Boolean>
        get() = _editCategoryEvent

    init {
        _addCategoryEvent.postValue(false)
        _editCategoryEvent.postValue(false)
    }

    fun showAddCategoryDialog(){
        _addCategoryEvent.postValue(true)
    }

    fun doneShowAddCategoryDialog(){
        _addCategoryEvent.postValue(false)
    }

    fun showEditCategoryDialog(){
        _editCategoryEvent.postValue(true)
    }

    fun doneShowEditCategoryDialog(){
        _editCategoryEvent.postValue(false)
    }

    fun addCategory(categoryName: String, type: CategoryType, isFav: Boolean = false, iconColor: Int = Color.BLACK){
        try {
            if(categoryName.isNotEmpty() && categoryName.isNotBlank())
                scope.launch {
                    categoryDao.insertCategory(Category(categoryName, type, isFav, iconColor))
                }
            else
                throw Exception("Empty name")
            _lastOperationWasSuccessful.postValue(true)
        } catch (ex: Exception) {
            _lastOperationWasSuccessful.postValue(false)
            ex.printStackTrace()
        }
    }

    fun editCategory(categoryId: Long, categoryName: String, type: CategoryType, isFav: Boolean = false, iconColor: Int = Color.BLACK){
        try {
            if (categoryId > 0 && categoryName.isNotEmpty() && categoryName.isNotBlank())
                scope.launch {
                    categoryDao.updateCategory(Category(categoryName,type,isFav,iconColor)
                        .also {
                            it.categoryId = categoryId
                        })
                }
            else
                throw Exception("Empty name or invalid id")
            _lastOperationWasSuccessful.postValue(true)
        }catch (ex:Exception){
            _lastOperationWasSuccessful.postValue(false)
        }
    }

    fun deleteCategory(categoryId: Long){
        try {
            if (categoryId > 0)
                scope.launch {
                    categoryDao.deleteCategory(categoryId)
                }
            else
                throw Exception("invalid id")
            _lastOperationWasSuccessful.postValue(true)
        }catch (ex:Exception){
            _lastOperationWasSuccessful.postValue(false)
        }
    }

    fun getCategory(id: Long) = categoryDao.getCategory(id)

}