package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ActivityAddTransactionBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddContact.setOnClickListener {
            startActivity(Intent(this, AddContactActivity::class.java))
        }

        binding.btnSaveNewTransaction.setOnClickListener {
            if (!checkInput()) return@setOnClickListener

            if (TransactionsSQLiteDAO.insert(Transaction(
                amount = binding.edittextAmount.text.toString().toFloat() * if (binding.radioDebt.isChecked) -1 else 1,
                contactId = (binding.spinnerContact.selectedItem as Contact).id,
                timeInMillis = Calendar.getInstance().apply{ with(binding.datePicker){ set(year, month, dayOfMonth) }}.timeInMillis,
                title = binding.edittextTitle.text.toString().run { if (length > 0) this else null },
                desc = binding.edittextDescription.text.toString().run { if (length > 0) this else null },
            ))) {
                setResult(RESULT_CODE_OK)
                finish()
            } else {
                setResult(RESULT_CODE_ABORTED)
                Log.e(AddTransactionActivity::class.simpleName, "Transaction adding failed")
            }

        }
    }

    override fun onResume() {
        super.onResume()
        fillContactSpinner()
    }

    private fun fillContactSpinner() {
        val contactsList = ContactsSQLiteDAO.getAll() ?: listOf()
        if (contactsList.isEmpty())
            binding.spinnerContact.isEnabled = false
        else {
            binding.spinnerContact.isEnabled = true
            binding.spinnerContact.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                contactsList
            )
        }
    }

    private fun checkInput() : Boolean {
        if (!binding.edittextAmount.text.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
            Snackbar.make(binding.root, R.string.message_amount_format_error, Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (binding.spinnerContact.selectedItem == null) {
            Snackbar.make(binding.root, R.string.message_contact_selection_error, Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (!binding.radioDebt.isChecked && !binding.radioCredit.isChecked) {
            Snackbar.make(binding.root, R.string.message_credit_debt_selection_error, Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    companion object {
        const val ADD_TRANSACTION_REQ_CODE = 88
        const val RESULT_CODE_OK = 0
        const val RESULT_CODE_ABORTED = -1

        const val TRANSACTION_EXTRA_KEY = "com.cito.youoweme.AddTransactionActivity.transaction"
    }
}