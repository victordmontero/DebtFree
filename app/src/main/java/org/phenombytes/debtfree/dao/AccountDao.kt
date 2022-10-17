package org.phenombytes.debtfree.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.relations.AccountWithOperations

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account): Long

    @Update
    suspend fun updateAccount(account: Account): Int

    @Delete
    suspend fun deleteAccount(account: Account): Int

    @Query("SELECT * FROM accounts ORDER BY accountId")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts WHERE accountId = :id")
    fun getAccount(id: Long): Account

    @Query("SELECT * FROM accounts ORDER BY accountId")
    suspend fun getAllAccountsNormal(): List<Account>

    @Query("SELECT SUM(balance) FROM accounts")
    fun getAccountsTotal(): LiveData<Double>

    @Transaction
    @Query("SELECT * FROM accounts WHERE accountId = :id")
    fun getAccountWithOperations(id: Long): LiveData<AccountWithOperations>
}