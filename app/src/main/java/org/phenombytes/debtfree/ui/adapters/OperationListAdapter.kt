package org.phenombytes.debtfree.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.phenombytes.debtfree.databinding.OperationItemBinding
import org.phenombytes.debtfree.models.domain.relations.OperationAndAccount
import org.phenombytes.debtfree.models.domain.relations.OperationAndCategory
import org.phenombytes.debtfree.other.Convertions.setImageTint

class OperationListAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(OperationDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> holder.bind(getItem(position))
        }
    }

    class ViewHolder private constructor(val binding: OperationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any) {
            when (item) {
                is OperationAndCategory -> bind(item)
                is OperationAndAccount -> bind(item)
            }
            binding.executePendingBindings()
        }

        fun bind(item: OperationAndAccount) {
            binding.operation = item.operation
            binding.itemOperationCategory.text = item.fromAccount.accountName
            binding.itemOperationAcount.text = item.toAccount.accountName
            binding.itemOperationAmount.text = item.operation.amount.toString()
        }

        fun bind(item: OperationAndCategory) {
            binding.operation = item.operation
            binding.itemOperationCategory.text = item.category.categoryName
            binding.itemOperationAcount.text = item.fromAccount.accountName
            binding.itemOperationAmount.text = item.operation.amount.toString()
            binding.operationImg.setColorFilter(item.category.categoryIconColor)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OperationItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class OperationDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when (oldItem) {
            is OperationAndAccount -> {
                val nItem = newItem as OperationAndAccount
                oldItem.operation.operationId == nItem.operation.operationId
            }
            is OperationAndCategory -> {
                val nItem = newItem as OperationAndCategory
                oldItem.operation.operationId == nItem.operation.operationId
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when (oldItem) {
            is OperationAndAccount -> {
                val nItem = newItem as OperationAndAccount
                oldItem.operation == nItem.operation
                        && oldItem.toAccount == nItem.toAccount
                        && oldItem.fromAccount == nItem.fromAccount
            }
            is OperationAndCategory -> {
                val nItem = newItem as OperationAndCategory
                oldItem.operation == nItem.operation
                        && oldItem.category == nItem.category
                        && oldItem.fromAccount == nItem.fromAccount
            }
            else -> false
        }
    }

}

