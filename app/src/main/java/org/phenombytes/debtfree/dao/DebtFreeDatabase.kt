package org.phenombytes.debtfree.dao


import androidx.room.Database
import androidx.room.RoomDatabase
import org.phenombytes.debtfree.models.domain.Account

@Database(entities = [Account::class], version = 1)
abstract class DebtFreeDatabase : RoomDatabase() {
    abstract fun accountDao() : AccountDao
}