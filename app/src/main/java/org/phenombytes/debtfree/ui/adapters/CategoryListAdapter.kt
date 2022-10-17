package org.phenombytes.debtfree.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.phenombytes.debtfree.databinding.CategoryItemBinding
import org.phenombytes.debtfree.models.domain.Category

class CategoryListAdapter(val clickListener: CategoryItemListener) :
    ListAdapter<Category, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val item = getItem(position)
                holder.bind(clickListener, item)
            }
        }
    }

    class ViewHolder private constructor(val binding: CategoryItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: CategoryItemListener, item: Category){
            binding.category = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class CategoryItemListener(val clickListener: (view: View, categoryId: Long?) -> Boolean){
    fun onClick(view: View, category: Category) = clickListener(view, category.categoryId)
}

class CategoryDiffCallback :DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem.categoryId == newItem.categoryId

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem == newItem
}
