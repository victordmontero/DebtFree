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

    private var currentAccountId = 0L

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

        val adapter = AccountListAdapter(AccountItemListener { view, accountId ->
            if(!actionModeCallback.isInSelectectionMode){
                actionModeCallback.selectedView = view
                actionModeCallback.accountId = accountId!!
                currentAccountId = accountId
                requireActivity().startActionMode(actionModeCallback)
            } else
                return@AccountItemListener false

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

        viewModel.editAccountEvent.observe(viewLifecycleOwner, Observer { t ->
            if(t){
                val fragment = AddAccountDialogFragment(viewModel.getAccount(currentAccountId))
                fragment.show(parentFragmentManager, AddAccountDialogFragment.TAG)
                currentAccountId = 0L
                viewModel.doneShowEditAccountDialog()
            }
        })

        activity?.title = activity?.getString(R.string.account_text)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val actionModeCallback = object : ActionMode.Callback{

        var selectedView: View? = null
        var isInSelectectionMode: Boolean = false
        var accountId:Long = 0

        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.edit_context_menu, menu)
            isInSelectectionMode = true
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, p1: Menu?): Boolean {
            selectedView!!.setBackgroundResource(android.R.color.darker_gray)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when(item.itemId){
                R.id.edit -> {
                    //Edit
                    viewModel.showEditAccountDialog()
                    mode.finish()
                    true
                }
                R.id.delete -> {
                    //Delete
                    viewModel.deleteAccount(accountId)
                    mode.finish()
                    true
                }
                R.id.transfer -> {
                    //Transfer
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            isInSelectectionMode = false
            selectedView!!.setBackgroundResource(android.R.color.transparent)
            selectedView = null
        }
    }
}