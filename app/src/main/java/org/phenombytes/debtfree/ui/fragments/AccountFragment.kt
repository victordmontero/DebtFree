package org.phenombytes.debtfree.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.dao.AccountDao
import org.phenombytes.debtfree.databinding.FragmentAccountBinding
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.ui.adapters.AccountItemListener
import org.phenombytes.debtfree.ui.adapters.AccountListAdapter
import org.phenombytes.debtfree.viewmodels.AccountViewModel
import javax.inject.Inject


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

        })

        binding.accountRecycleview.layoutManager = LinearLayoutManager(context)
        binding.accountRecycleview.adapter = adapter

        viewModel.accounts.observe(viewLifecycleOwner, Observer { accounts ->
            adapter.submitList(accounts)
            adapter.notifyDataSetChanged()
        })

        activity?.title = "Accounts"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}