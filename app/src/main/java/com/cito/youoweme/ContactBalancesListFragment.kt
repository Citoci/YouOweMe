package com.cito.youoweme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ContactBalanceEntryBinding
import com.cito.youoweme.ui.theme.YouOweMeTheme

// NOW WITH JETPACK COMPOSE!!
class ContactBalancesListFragment : Fragment() {

    private var contacts = listOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateContacts()
        return ComposeView(requireContext()).apply {
            setContent {
                YouOweMeTheme {
                    PerContactBalancesList(contacts)
                }
            }
        }
//        return inflater.inflate(R.layout.fragment_contact_balances_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        updateContacts()
    }

    @Composable
    fun PerContactBalancesList(contacts: List<Contact>) {
        LazyColumn {
            items(
                items = contacts,
                key = { it.id ?: -1 }
            ) {
                ContactBalance(it)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ContactBalance(contact: Contact = Contact(null, "Mario", "Rossi", 5f)) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(8.dp)) {
            Text(text = contact.name ?: "")
            Text(text = contact.surname ?: "",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp))
            Text(text = "" + (contact.balance ?: 0))
        }
    }

//    override fun onResume() {
//        super.onResume()
//        with(activity?.findViewById<RecyclerView>(R.id.contact_balances_list)) {
//            this?.layoutManager = LinearLayoutManager(context)
//            this?.adapter = ContactBalancesRecycleViewAdapter(
//                ContactsSQLiteDAO.getAll()?.apply {
//                    TransactionsSQLiteDAO.getAll()?.let { transactions ->
//                        this.forEach { contact ->
//                            contact.calculateBalance(transactions)
//                        }
//                    }
//                } ?: listOf()
//            )
//        }
//    }

    private fun updateContacts() {
        contacts = ContactsSQLiteDAO.getAll()?.apply {
            TransactionsSQLiteDAO.getAll()?.let { transactions ->
                this.forEach { contact ->
                    contact.calculateBalance(transactions)
                }
            }
        } ?: listOf()
    }

    private fun Contact.calculateBalance(transactions: List<Transaction>): Float {
        var sum = 0f
        transactions.filter { it.contactId == id }
            .forEach { sum += it.amount }
        return sum.also { balance = it }
    }

//    inner class ContactBalancesRecycleViewAdapter(private val values: List<Contact>) :
//        RecyclerView.Adapter<ContactBalancesRecycleViewAdapter.ContactViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
//            return ContactViewHolder(
//                ContactBalanceEntryBinding.inflate(
//                    LayoutInflater.from(parent.context), parent, false
//                )
//            )
//        }
//
//        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
//            val item = values[position]
//
//            holder.contentView.text = "$item : ${item.balance}"
//        }
//
//        override fun getItemCount(): Int = values.size
//
//        inner class ContactViewHolder(binding: ContactBalanceEntryBinding) :
//            RecyclerView.ViewHolder(binding.root) {
//            val contentView: TextView = binding.txtfghty
//        }
//
//    }

}