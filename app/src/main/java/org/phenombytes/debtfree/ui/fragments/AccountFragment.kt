package org.phenombytes.debtfree.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.databinding.FragmentAccountBinding
import org.phenombytes.debtfree.ui.adapters.AccountItemListener
import org.phenombytes.debtfree.ui.adapters.AccountListAdapter
import org.phenombytes.debtfree.ui.fragments.dialogs.AddAccountDialogFragment
import org.phenombytes.debtfree.viewmodels.AccountViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account,
            container,
            false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = AccountListAdapter(AccountItemListener { accountId ->
            return@AccountItemListener true
        })

        binding.accountRecycleview.layoutManager = LinearLayoutManager(context)
        binding.accountRecycleview.adapter = adapter

        viewModel.accounts.observe(viewLifecycleOwner, Observer { accounts ->
            adapter.submitList(accounts)
            adapter.notifyDataSetChanged()
        })

        viewModel.addAccountEvent.observe(viewLifecycleOwner, Observer { t ->
            if(t){
                val fragment = AddAccountDialogFragment()
//                fragment.setTargetFragment(this,0)
                fragment.show(parentFragmentManager, AddAccountDialogFragment.TAG)
                viewModel.doneShowAddAccountDialog()
            }
        })

        activity?.title = activity?.getString(R.string.account_text)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}