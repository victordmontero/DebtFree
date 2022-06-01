package org.phenombytes.debtfree.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.phenombytes.debtfree.models.domain.Account

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAccount(account: Account) : Long

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM accounts ORDER BY id")
    fun getAllAccounts() : LiveData<List<Account>>
}