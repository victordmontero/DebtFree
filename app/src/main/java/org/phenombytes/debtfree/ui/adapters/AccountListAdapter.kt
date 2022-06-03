package org.phenombytes.debtfree.ui.adapters

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
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
            val imgView = binding.accountImg
            imgView.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
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

class AccountItemListener(val clickListener: (accountId: Long?) -> Boolean) {
    fun onClick(account: Account):Boolean = clickListener(account.id)
}

class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }
}