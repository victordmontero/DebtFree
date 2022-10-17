package org.phenombytes.debtfree.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.databinding.FragmentCategoryBinding
import org.phenombytes.debtfree.ui.adapters.CategoryItemListener
import org.phenombytes.debtfree.ui.adapters.CategoryListAdapter
import org.phenombytes.debtfree.ui.fragments.dialogs.AddCategoryDialogFragment
import org.phenombytes.debtfree.viewmodels.CategoryViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding:FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CategoryViewModel by viewModels()

    private var currentCategoryId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_category,
            container,
            false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = CategoryListAdapter(CategoryItemListener { view: View, categoryId: Long? ->
            if(!actionModeCallback.isInSelectionMode){
                actionModeCallback.categoryId = categoryId!!
                actionModeCallback.selectedView = view
                actionModeCallback.isInSelectionMode = true
                currentCategoryId = categoryId
                requireActivity().startActionMode(actionModeCallback)
                return@CategoryItemListener true
            }else
                return@CategoryItemListener false
        })

        binding.categoryRecycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecycleview.adapter = adapter

        viewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            adapter.submitList(categories)
        })

        viewModel.addCategoryEvent.observe(viewLifecycleOwner, Observer { t ->
            if(t){
                val fragment = AddCategoryDialogFragment()
                fragment.show(parentFragmentManager, AddCategoryDialogFragment.TAG)
                viewModel.doneShowAddCategoryDialog()
            }
        })

        viewModel.editCategoryEvent.observe(viewLifecycleOwner, Observer { t ->
            if(t){
                val fragment = AddCategoryDialogFragment(viewModel.getCategory(currentCategoryId))
                fragment.show(parentFragmentManager, AddCategoryDialogFragment.TAG)
                viewModel.doneShowEditCategoryDialog()
            }
        })

        activity?.title = activity?.getString(R.string.category_text)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private val actionModeCallback = object : ActionMode.Callback{

        var selectedView: View? = null
        var isInSelectionMode : Boolean = false
        var categoryId: Long = 0

        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.edit_context_menu, menu)
            isInSelectionMode = true
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
           selectedView!!.setBackgroundResource(android.R.color.darker_gray)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {
            return when(menuItem.itemId){
                R.id.transfer -> {
                    mode.finish()
                    true
                }
                R.id.edit -> {
                    viewModel.showEditCategoryDialog()
                    mode.finish()
                    true
                }
                R.id.delete ->{
                    viewModel.deleteCategory(currentCategoryId)
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            isInSelectionMode = false
            selectedView!!.setBackgroundResource(android.R.color.transparent)
            selectedView = null
        }
    }
}