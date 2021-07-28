package com.cito.youoweme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ContactBalanceEntryBinding

class ContactBalancesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_balances_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        with(activity?.findViewById<RecyclerView>(R.id.contact_balances_list)) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = ContactBalancesRecicleViewAdapter(
                ContactsSQLiteDAO.getAll()?.apply {
                    TransactionsSQLiteDAO.getAll()?.let { transactions ->
                        this.forEach { contact ->
                            contact.calculateBalance(transactions)
                        }
                    }
                } ?: listOf()
            )
        }
    }

    private fun Contact.calculateBalance(transactions: List<Transaction>): Float {
        var sum = 0f
        transactions.filter { it.contactId == id }
            .forEach { sum += it.amount }
        return sum.also { balance = it }
    }

    inner class ContactBalancesRecicleViewAdapter(private val values: List<Contact>) :
        RecyclerView.Adapter<ContactBalancesRecicleViewAdapter.ContactViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            return ContactViewHolder(
                ContactBalanceEntryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val item = values[position]

            holder.contentView.text = "$item : ${item.balance}"
        }

        override fun getItemCount(): Int = values.size

        inner class ContactViewHolder(binding: ContactBalanceEntryBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val contentView: TextView = binding.txtfghty
        }

    }

}