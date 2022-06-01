package org.phenombytes.debtfree.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.phenombytes.debtfree.dao.DebtFreeDatabase
import org.phenombytes.debtfree.other.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDebtFreeDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
            context,
            DebtFreeDatabase::class.java,
            Constants.DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideAccountDao(database: DebtFreeDatabase) = database.accountDao()
}