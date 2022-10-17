package org.phenombytes.debtfree.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.databinding.FragmentOperationBinding
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.ui.adapters.CategoryItemListener
import org.phenombytes.debtfree.ui.adapters.CategoryListAdapter
import org.phenombytes.debtfree.ui.adapters.OperationListAdapter
import org.phenombytes.debtfree.viewmodels.OperationViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [OperationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class OperationFragment : Fragment() {
    private var _binding : FragmentOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel : OperationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_operation,
            container,
            false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = OperationListAdapter()

        binding.operationRecycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.operationRecycleview.adapter = adapter

        viewModel.operations.observe(viewLifecycleOwner, Observer { operations ->
            adapter.submitList(operations)
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }
}