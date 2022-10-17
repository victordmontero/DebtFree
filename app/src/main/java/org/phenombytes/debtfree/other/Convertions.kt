package org.phenombytes.debtfree.other

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.enums.CategoryType
import org.phenombytes.debtfree.enums.OperationType
import java.text.DecimalFormat


object Convertions {
    @JvmStatic
    fun convert(double: Double): String {
        val format = DecimalFormat("#,###.00")
        format.isDecimalSeparatorAlwaysShown = false
        return format.format(double).toString()
    }

    @JvmStatic
    @BindingAdapter("app:tint")
    fun ImageView.setImageTint(@ColorInt color: Int) {
        setColorFilter(color)
    }

    @JvmStatic
    @BindingAdapter("app:src")
    fun ImageView.setSource(categoryType: CategoryType) {
        if (categoryType == CategoryType.Expense)
            setImageResource(R.drawable.ic_expense_icon)
        else
            setImageResource(R.drawable.ic_income_icon)
    }

    @JvmStatic
    @BindingAdapter("app:src")
    fun ImageView.setSource(operationType: OperationType) {
        when(operationType){
            OperationType.Transfer -> setImageResource(R.drawable.ic_baseline_repeat_24)
            OperationType.Expense -> setImageResource(R.drawable.ic_expense_icon)
            OperationType.Income -> setImageResource(R.drawable.ic_income_icon)
        }
    }
}

