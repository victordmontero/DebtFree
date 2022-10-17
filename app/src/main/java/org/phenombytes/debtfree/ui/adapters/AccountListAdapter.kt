package org.phenombytes.debtfree.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.phenombytes.debtfree.databinding.AccountItemBinding
import org.phenombytes.debtfree.models.domain.Account

class AccountListAdapter(val clickListener: AccountItemListener) :
    ListAdapter<Account, RecyclerView.ViewHolder>(AccountDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position)
                holder.bind(clickListener, item)
            }
        }
    }

    class ViewHolder private constructor(val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AccountItemListener, item: Account) {
            binding.account = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AccountItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class AccountItemListener(val clickListener: (view: View, accountId: Long?) -> Boolean) {
    fun onClick(view: View, account: Account):Boolean = clickListener(view, account.accountId)
}

class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.accountId == newItem.accountId
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }
}