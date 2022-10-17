package org.phenombytes.debtfree.ui.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import dagger.hilt.android.AndroidEntryPoint
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.viewmodels.CategoryViewModel

@AndroidEntryPoint
class AddCategoryDialogFragment(var category: Category = Category("")) : DialogFragment() {
    companion object {
        const val TAG = "AddCategoryDialogFragment"
    }

    lateinit var categoryNameTextView: TextView
    lateinit var expenseRadioBtn: RadioButton
    lateinit var incomeRadioBtn: RadioButton
    lateinit var isFavoriteChkBox: CheckBox
    lateinit var categoryIconColor: ColorPickerView
    lateinit var alphaBar: AlphaSlideBar

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val builder = AlertDialog.Builder(it)
            val view = inflater.inflate(R.layout.dialog_add_category, null)
            builder.setView(view)
                .setPositiveButton(getString(R.string.add_account_text),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        try {
                            val cateName = categoryNameTextView.text.toString()
                            val isExpense = expenseRadioBtn.isChecked
                            val isFavorite = isFavoriteChkBox.isChecked
                            val iconColor = categoryIconColor.color

                            if (cateName.isNotBlank() && cateName.isNotEmpty()) {
                                if (category.categoryId == null) {
                                    viewModel.addCategory(
                                        cateName,
                                        if (isExpense)
                                            CategoryType.Expense
                                        else
                                            CategoryType.Income,
                                        isFavorite,
                                        iconColor
                                    )
                                } else {
                                    viewModel.editCategory(
                                        category.categoryId!!,
                                        cateName,
                                        if (isExpense)
                                            CategoryType.Expense
                                        else
                                            CategoryType.Income,
                                        isFavorite,
                                        iconColor
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.category_name_not_empty),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (exceptionNull: NullPointerException) {
                            Toast.makeText(
                                context,
                                R.string.account_name_not_empty,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                .setNegativeButton(getString(R.string.cancel_account_text),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        getDialog()?.cancel()
                    })

            categoryNameTextView = view.findViewById(R.id.category_name)
            expenseRadioBtn = view.findViewById(R.id.expense_radio_btn)
            incomeRadioBtn = view.findViewById(R.id.income_radio_btn)
            categoryIconColor = view.findViewById(R.id.category_colorPickerView)
            isFavoriteChkBox = view.findViewById(R.id.category_favorite_checkbox)

            categoryIconColor.attachBrightnessSlider(view.findViewById(R.id.category_brightnessSlide))

            categoryIconColor.setColorListener(ColorListener { color, fromUser ->

            })

            if (category.categoryName != "") {
                categoryNameTextView.text = category.categoryName
                expenseRadioBtn.isChecked = category.type == CategoryType.Expense
                incomeRadioBtn.isChecked = category.type == CategoryType.Income
                categoryIconColor.setInitialColor(category.categoryIconColor)
                isFavoriteChkBox.isChecked = category.categoryIsFav
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}