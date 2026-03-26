package org.phenombytes.debtfree.ui.fragments.dialogs

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.phenombytes.debtfree.R
import org.phenombytes.debtfree.enums.OperationType
import org.phenombytes.debtfree.models.domain.Account
import org.phenombytes.debtfree.models.domain.Category
import org.phenombytes.debtfree.viewmodels.OperationViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddOperationDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AddOperationDialogFragment : DialogFragment() {

    private val viewModel: OperationViewModel by viewModels()

    private lateinit var datePicker : DatePicker
    private lateinit var operationAmount: EditText
    private lateinit var operationDescription: EditText

    private lateinit var accountsAlertDialogBuilder: AlertDialog.Builder
    private lateinit var categoriesAlertDialogBuilder: AlertDialog.Builder
    private lateinit var operationTypeAlertDialogBuilder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_operation, container, false)

        val cancelButton = view.findViewById<Button>(R.id.cancel_operation_btn)
        val addButton = view.findViewById<Button>(R.id.add_operation_btn)
        val fromAccountButton = view.findViewById<Button>(R.id.image_capture_button)
        val toAccountButton = view.findViewById<Button>(R.id.video_capture_button)
        val operationTypeButton = view.findViewById<Button>(R.id.image_capture_button)

        var accounts : List<Account>
        var categories: List<Category>
        var operationTypes = OperationType.values()

        runBlocking {
            accounts = viewModel.accountDao.getAllAccountsNormal()
            categories = viewModel.categoryDao.getAllCategoryNormal()
        }

        cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        addButton.setOnClickListener {
            val operationDate = Date()
            val desc = operationDescription.text.toString()
            val amount = operationAmount.text.toString().toDouble()

            if((desc.isNotBlank() && desc.isNotEmpty()) && amount >= 0) {
                when (OperationType.valueOf(operationTypeButton.text.toString())) {
                    OperationType.Expense, OperationType.Income -> {
                        viewModel.addOperation(
                            desc,
                            amount,
                            operationDate,
                            OperationType.valueOf(operationTypeButton.text.toString()),
                            accounts.single { a ->a.accountName.equals(fromAccountButton.text.toString()) }.accountId!!,
                            categories.single { c -> c.categoryName.equals(toAccountButton.text.toString()) }.categoryId!!)
                    }
                    OperationType.Transfer-> {
                        viewModel.addOperation(
                            desc,
                            amount,
                            operationDate,
                            OperationType.valueOf(operationTypeButton.text.toString()),
                            accounts.single { a ->a.accountName.equals(fromAccountButton.text.toString()) }.accountId!!,
                            accounts.single { c -> c.accountName.equals(toAccountButton.text.toString()) }.accountId!!)
                    }
                }
            }
//            if((desc.isNotBlank() && desc.isNotEmpty()) && amount >= 0){
//                viewModel.addOperation(
//                    desc,
//                    amount,
//                    operationDate,
//                    OperationType.valueOf(operationTypeButton.text.toString()),
//                accounts.single { a ->a.accountName.equals(fromAccountButton.text.toString()) }.accountId!!,
//                categories.single { c -> c.categoryName.equals(toAccountButton.text.toString()) }.categoryId!!)
//            }

            findNavController().navigateUp()
        }

        fromAccountButton.setOnClickListener {
            accountsAlertDialogBuilder.show()
        }

        toAccountButton.setOnClickListener {
            categoriesAlertDialogBuilder.show()
        }

        operationTypeButton.setOnClickListener {
            operationTypeAlertDialogBuilder.show()
        }

        val accountsList = accounts!!.map { a -> a.accountName }.toTypedArray()
        val categoriesList = categories!!.map { c -> c.categoryName }.toTypedArray()
        val operationTypesList = OperationType.values().map { opType -> opType.name }.toTypedArray()

        accountsAlertDialogBuilder = AlertDialog.Builder(requireContext())
        accountsAlertDialogBuilder.setTitle("Choose an account")
            .setItems(accountsList, DialogInterface.OnClickListener { dialogInterface, i ->
                fromAccountButton.text = accounts[i].accountName
            }).create()

        categoriesAlertDialogBuilder = AlertDialog.Builder(requireContext())
        categoriesAlertDialogBuilder.setTitle("Choose an category")
            .setItems(categoriesList, DialogInterface.OnClickListener { dialogInterface, i ->
                toAccountButton.text = categories[i].categoryName
            }).create()

        operationTypeAlertDialogBuilder = AlertDialog.Builder(requireContext())
        operationTypeAlertDialogBuilder.setTitle("Choose operation type")
            .setItems(operationTypesList, DialogInterface.OnClickListener { dialogInterface, i ->
                operationTypeButton.text = operationTypes[i].name
//                if(operationTypes[i].name.equals("Transfer")){
//                    accountsAlertDialogBuilder.setTitle("Choose an account")
//                        .setItems(accountsList, DialogInterface.OnClickListener { dialogInterface, i ->
//                            toAccountButton.text = accounts[i].accountName
//                        })
//                }

                when(operationTypes[i].name){
                    "Expense", "Income" -> {
                        accountsAlertDialogBuilder = AlertDialog.Builder(requireContext())
                        accountsAlertDialogBuilder.setTitle("Choose an account")
                            .setItems(accountsList, DialogInterface.OnClickListener { dialogInterface, i ->
                                fromAccountButton.text = accounts[i].accountName
                            }).create()

                        categoriesAlertDialogBuilder = AlertDialog.Builder(requireContext())
                        categoriesAlertDialogBuilder.setTitle("Choose an category")
                            .setItems(categoriesList, DialogInterface.OnClickListener { dialogInterface, i ->
                                toAccountButton.text = categories[i].categoryName
                            }).create()
                    }
                    "Transfer" -> {
                        categoriesAlertDialogBuilder = AlertDialog.Builder(requireContext())
                        categoriesAlertDialogBuilder.setTitle("Choose an category")
                            .setItems(accountsList, DialogInterface.OnClickListener { dialogInterface, i ->
                                toAccountButton.text = accounts[i].accountName
                            }).create()
                    }
                }

            }).create()

        //datePicker = view.findViewById(R.id.operation_date_picker)
//        operationAmount = view.findViewById(R.id.operation_amount)
//        operationDescription = view.findViewById(R.id.operation_desc)

        return view
    }
}