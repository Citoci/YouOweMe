package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.TransactionEntryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransactionsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions_list, container, false)

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
                TransactionsSQLiteDAO.getAll() ?: listOf()
            )
        }
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

    inner class TransactionsRecyclerViewAdapter(private val values: List<Transaction>) :
        RecyclerView.Adapter<TransactionsRecyclerViewAdapter.TransactionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            return TransactionViewHolder(
                TransactionEntryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            val item = values[position]

            holder.contentView.text = item.toString()

            holder.itemView.setOnClickListener { view ->
//            Toast.makeText(view.context, item.toString(), Toast.LENGTH_SHORT).show()
                view.context.startActivity(
                    Intent(view.context, TransactionDetailsActivity::class.java).also {
                        it.putExtra(TransactionDetailsActivity.TRANSACTION_ID_EXTRA, item.id)
                    }
                )

            }
        }

        override fun getItemCount(): Int = values.size

        inner class TransactionViewHolder(binding: TransactionEntryBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val contentView: TextView = binding.textviewTransactionEntry
        }

    }

}