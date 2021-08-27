package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager
import com.cito.youoweme.ui.theme.YouOweMeTheme

// NOW WITH JETPACK COMPOSE!!
class ContactsBalancesListFragment : Fragment() {

    private var contacts: MutableState<List<Contact>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateContacts()
        return ComposeView(requireContext()).apply {
            setContent {
                YouOweMeTheme {
                    contacts = remember { mutableStateOf(listOf()) }
                    PerContactBalancesList(contacts!!)
                }
            }
        }
//        return inflater.inflate(R.layout.fragment_contact_balances_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        updateContacts()
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
        contacts?.value = ContactsSQLiteDAO.getAll()?.filter { it.usernameRef == UserLoginManager.loggedUsername }?.apply {
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


    @Composable
    fun PerContactBalancesList(contacts: MutableState<List<Contact>>) {
        LazyColumn {
            items(
                items = contacts.value,
                key = { it.id ?: -1 }
            ) {
                ContactBalance(it)
                Divider(color = Color.LightGray)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ContactBalance(contact: Contact = Contact(null, "Mario", "Rossi", 5f)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                startActivity(Intent(context, ContactBalanceActivity::class.java).apply {
                    putExtra(ContactBalanceActivity.CONTACT_ID_EXTRA, contact.id)
                    putExtra(ContactBalanceActivity.CONTACT_BALANCE_EXTRA, contact.balance ?: 0f)
                })
            }
            .padding(16.dp)) {
            Text(
                text = contact.toString(), fontSize = 24.sp,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                text = stringResource(R.string.format_euros, contact.balance ?: 0),
                fontSize = 24.sp,
                color = MaterialTheme.colors.onBackground,
            )
        }
    }
}

