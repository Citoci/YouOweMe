package com.cito.youoweme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.model.euros
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.EntryTransactionBinding
import com.cito.youoweme.login.UserLoginManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransactionsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions_list, container, false)

        view.findViewById<RecyclerView>(R.id.transactions_list).apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        // Set the listener for the floating button
        view.findViewById<FloatingActionButton>(R.id.btn_add_transaction).setOnClickListener {
            startActivityForResult(
                Intent(context, AddTransactionActivity::class.java),
                AddTransactionActivity.ADD_TRANSACTION_REQ_CODE
            )
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<RecyclerView>(R.id.transactions_list)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TransactionsRecyclerViewAdapter(
                TransactionsSQLiteDAO.getAll()?.filter { it.isOfLoggedUser() } ?: listOf()
            )
        }
    }

    private fun Transaction.isOfLoggedUser(): Boolean {
        return (ContactsSQLiteDAO.getById(contactId?:return false)?.usernameRef == UserLoginManager.loggedUser?.username)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            AddTransactionActivity.RESULT_CODE_OK -> {
                data?.extras?.getParcelable<Transaction>(AddTransactionActivity.TRANSACTION_EXTRA_KEY)
            }
            AddTransactionActivity.RESULT_CODE_ABORTED -> {
                Toast.makeText(context, "Operation Aborted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class TransactionsRecyclerViewAdapter(private val transactions: List<Transaction>) :
        RecyclerView.Adapter<TransactionsRecyclerViewAdapter.TransactionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            return TransactionViewHolder(
                EntryTransactionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            // creating an hashmap od the contacts, to get their names efficiently later
            val contactNames = ContactsSQLiteDAO.getAll()?.associate { Pair(it.id, it) }

            with(holder) {
                transactions[position].let { t ->
                    amountView.text = itemView.resources.getString(R.string.format_euros, t.amount.euros())
                    titleView.text = t.title
                    contactView.text = contactNames?.get(t.contactId)?.toString() ?: "error"// t.contactId?.let { ContactsSQLiteDAO.getById(it)?.toString() } ?: "error"
                    dateView.text = t.formattedDate

                    amountView.setTextColor(if (t.amount >= 0) itemView.resources.getColor(R.color.credit) else Color.RED)

                    itemView.setOnClickListener { view ->
                        view.context.startActivity(
                            Intent(view.context, TransactionDetailsActivity::class.java).apply {
                                putExtra(TransactionDetailsActivity.TRANSACTION_ID_EXTRA, t.id)
                            }
                        )
                    }
                }
            }
        }

        override fun getItemCount(): Int = transactions.size

        inner class TransactionViewHolder(binding: EntryTransactionBinding) :
            RecyclerView.ViewHolder(binding.root) {

//            val contentView: TextView = binding.textviewTransactionEntry
            val amountView: TextView = binding.txtAmount
            val titleView: TextView = binding.txtTitle
            val dateView: TextView = binding.txtDate
            val contactView: TextView = binding.txtContact

        }

    }

}