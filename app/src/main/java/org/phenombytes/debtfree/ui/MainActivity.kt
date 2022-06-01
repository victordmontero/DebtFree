package org.phenombytes.debtfree.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.viewmodels.AccountViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var accountDao: AccountDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "Injected $accountDao")
    }
}