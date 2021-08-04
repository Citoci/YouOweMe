package com.cito.youoweme

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.google.android.material.appbar.AppBarLayout

class ContactBalanceActivity : AppCompatActivity() {

    private var contact: Contact? = null
    private var transactions: List<Transaction>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_balance)

        setSupportActionBar(findViewById(R.id.toolbar))

//        actionBar?.setDisplayShowTitleEnabled(false)

//        var isShow = false
//        findViewById<AppBarLayout>(R.id.app_bar).addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//
//            val scrollRange = appBarLayout.totalScrollRange
//
//            if (verticalOffset == 0) {
//                isShow = true
//                Toast.makeText(this, "unho", Toast.LENGTH_SHORT).show()
//            } else if (isShow) {
//                isShow = false
//                Toast.makeText(this, "duhy", Toast.LENGTH_SHORT).show()
//            }
////            Log.d("", "$scrollRange + $verticalOffset")
//        })

        contact = ContactsSQLiteDAO.getById(intent.getLongExtra(CONTACT_ID_EXTRA, 0))
        contact?.balance = intent.getFloatExtra(CONTACT_BALANCE_EXTRA, 0f)

        if (contact == null) {
            Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        transactions = TransactionsSQLiteDAO.getAll()?.filter { it.contactId == contact?.id }

//        if (supportActionBar == null)
//            Log.d(this::class.simpleName, "nope")
        supportActionBar?.apply {
            Log.d(this::class.simpleName, "onCreate")
            setDisplayShowTitleEnabled(true)
            title = "$contact: €${contact?.balance}".also { Log.d(this::class.simpleName, it) }
        }

        findViewById<RecyclerView>(R.id.recycler_transactions_list).apply {
            layoutManager = LinearLayoutManager(this@ContactBalanceActivity)
            adapter =
                TransactionsListFragment.TransactionsRecyclerViewAdapter(
                    transactions ?: listOf()
//                    mutableListOf<Transaction>().apply { transactions?.forEach { t -> repeat(7) { add(t) } }}
                )
            addItemDecoration(
                DividerItemDecoration(
                    this@ContactBalanceActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

    }


    companion object {
        const val CONTACT_ID_EXTRA = "com.cito.youoweme.ContactBalanceActivity.CONTACT_ID_EXTRA"
        const val CONTACT_BALANCE_EXTRA =
            "com.cito.youoweme.ContactBalanceActivity.CONTACT_BALANCE_EXTRA"
    }
}