package org.phenombytes.debtfree.ui.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar
import dagger.hilt.android.AndroidEntryPoint
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.viewmodels.AccountViewModel

@AndroidEntryPoint
class AddAccountDialogFragment(var account: Account = Account("")) : DialogFragment() {

    companion object {
        const val TAG = "AddAccountDialogFragment"
    }

    lateinit var accountNameText: EditText
    lateinit var accountBalanceText: EditText
    lateinit var accountIsFavorite: CheckBox
//    lateinit var accountColorPickerView: ColorPickerView
//    lateinit var accountBrightnessSlideBar: BrightnessSlideBar

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val builder = AlertDialog.Builder(it)
            val view = inflater.inflate(R.layout.dialog_add_account, null)
            builder.setView(view)
                .setPositiveButton(R.string.add_account_text,
                    DialogInterface.OnClickListener { dialog, id ->

                        try {
                            val accountName = accountNameText.text.toString()
                            val accountBalance = accountBalanceText.text
                                .toString()
                                .toDoubleOrNull()

                            if (accountName.isNotEmpty()) {
                                if (account.accountId == null)
                                    viewModel.addAccount(
                                        accountName,
                                        accountBalance ?: 0.00,
                                        accountIsFavorite.isChecked)
                                else
                                    viewModel.editAccount(
                                        account.accountId!!,
                                        accountName,
                                        accountBalance ?: 0.00,
                                        accountIsFavorite.isChecked)
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.account_name_not_empty,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (exceptionNull: NullPointerException) {

                        }
                    })
                .setNegativeButton(R.string.cancel_account_text,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })

            accountNameText = view.findViewById(R.id.account_name)
            accountBalanceText = view.findViewById(R.id.account_balance)
            accountIsFavorite = view.findViewById(R.id.account_favorite_checkbox)
//            accountColorPickerView = view.findViewById(R.id.account_colorPickerView)
//            accountBrightnessSlideBar = view.findViewById(R.id.account_brightnessSlide)

//            accountColorPickerView.attachBrightnessSlider(accountBrightnessSlideBar)
//            accountColorPickerView.setColorListener(ColorListener { color, fromUser ->
//
//            })

            if (account.accountName != "") {
                accountNameText.text = SpannableStringBuilder(account.accountName)
                accountBalanceText.text = SpannableStringBuilder(account.balance.toString())
//                accountColorPickerView.setInitialColor(account.accountIconColor)
                accountIsFavorite.isChecked = account.accountIsFav
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}