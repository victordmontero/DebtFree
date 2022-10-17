package org.phenombytes.debtfree.dao


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.models.domain.Operation
import org.phenombytes.debtfree.other.Converters

@Database(entities = [
    Account::class,
    Category::class,
    Operation::class], version = 1)
@TypeConverters(Converters::class)
abstract class DebtFreeDatabase : RoomDatabase() {
    abstract fun accountDao() : AccountDao
    abstract fun categoryDao() : CategoryDao
    abstract fun operationDao(): OperationDao
}