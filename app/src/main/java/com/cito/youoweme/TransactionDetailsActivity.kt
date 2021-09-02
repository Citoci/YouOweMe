package com.cito.youoweme

import android.app.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ActivityTransactionDetailsBinding
import com.cito.youoweme.login.UserLoginManager
import com.cito.youoweme.login.model.User
import com.cito.youoweme.notifications.RememberNotificationBroadcastReceiver
import com.cito.youoweme.utils.quickToast
import com.google.android.material.snackbar.Snackbar
import java.util.*


class TransactionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionDetailsBinding

    private var transaction: Transaction? = null
    private var contact: Contact? = null
    private var notificationManager: NotificationManager? = null

    // Notification Scheduling Properties
    var notifScheduleCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TransactionsSQLiteDAO.open(this)
        ContactsSQLiteDAO.open(this)

        // Getting the transaction info
        transaction =
            intent.extras?.getLong(TRANSACTION_ID_EXTRA)?.let { TransactionsSQLiteDAO.getById(it) }
        contact = transaction?.contactId?.let { ContactsSQLiteDAO.getById(it) }

        if (transaction == null) {
            Log.e(TransactionDetailsActivity::class.simpleName, "transaction not found")
            finish()
            return
        }

        // Displaying the transaction info
        binding.textviewAmount.text = getString(R.string.format_euros, transaction?.amount)
        binding.textviewContact.text = getString(
            if (transaction!!.amount >= 0) R.string.message_he_owes_you else R.string.message_you_owe_him,
            contact.toString()
        )
        binding.textviewDate.text =
            getString(R.string.format_date, Date(transaction!!.timeInMillis))
        binding.textviewTitle.text = "${transaction?.title}"
        binding.textviewDesc.text = "${transaction?.desc}"

        binding.btnNotify.isEnabled = UserLoginManager.isLogged

        // Instantiating the Pickers
        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker?, h: Int, m: Int ->
                notifScheduleCalendar.set(Calendar.HOUR, h)
                notifScheduleCalendar.set(Calendar.MINUTE, m)
                notifScheduleCalendar.set(Calendar.SECOND, 0)
                scheduleNotification()
            },
            notifScheduleCalendar.get(Calendar.HOUR),
            notifScheduleCalendar.get(Calendar.MINUTE),
            true
        )
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, y: Int, m: Int, d: Int ->
                notifScheduleCalendar.set(Calendar.YEAR, y)
                notifScheduleCalendar.set(Calendar.MONTH, m)
                notifScheduleCalendar.set(Calendar.DAY_OF_MONTH, d)
                timePickerDialog.show()
            },
            notifScheduleCalendar[Calendar.YEAR],
            notifScheduleCalendar.get(Calendar.MONTH),
            notifScheduleCalendar.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = notifScheduleCalendar.timeInMillis }

        // Listeners
        binding.btnNotify.setOnClickListener {
//            Snackbar.make(it, "Not yet implemented", Snackbar.LENGTH_SHORT).show()
            datePickerDialog.show()
        }

        binding.btnCopyToClipboard.setOnClickListener {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                ClipData.newPlainText("",
                    transaction?.amount?.let {
                        getString(R.string.text_transaction_exported,
                            if (it>0) contact else UserLoginManager.loggedUsername,
                            if (it>0) UserLoginManager.loggedUsername else contact,
                            getString(R.string.format_euros, transaction?.amount),
                            transaction?.title
                        )
                    }
                )
            )
            quickToast(this, getString(R.string.message_transaction_copied_to_clipboard))
        }

        binding.btnDelete.setOnClickListener {
            TransactionsSQLiteDAO.delete(transaction!!)
            finish()
        }

    }

    private fun scheduleNotification() {
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
            AlarmManager.RTC_WAKEUP,
            notifScheduleCalendar.timeInMillis + 5 * 1000,
//            Calendar.getInstance().timeInMillis + 5 * 1000,
            PendingIntent.getBroadcast(
                this,
                "${UserLoginManager.loggedUsername}:${transaction!!.id}".hashCode(),
                Intent(this, RememberNotificationBroadcastReceiver::class.java).apply {
                    putExtra(
                        RememberNotificationBroadcastReceiver.NOTIFICATION_ID_EXTRA,
                        transaction?.id
                    )
                    putExtra(
                        RememberNotificationBroadcastReceiver.NOTIFICATION_TITLE_EXTRA,
                        "${transaction?.title}: ${
                            getString(
                                R.string.format_euros,
                                transaction?.amount
                            )
                        }"
                    )
                    putExtra(
                        RememberNotificationBroadcastReceiver.NOTIFICATION_TEXT_EXTRA,
                        getString(
                            if (transaction!!.amount >= 0) R.string.message_he_owes_you else R.string.message_you_owe_him,
                            contact.toString()
                        )
                    )
                },
                PendingIntent.FLAG_ONE_SHOT or
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )
        )

        (getString(R.string.message_notification_scheduled) + " ${notifScheduleCalendar.time}").let {
//            Log.d(TransactionDetailsActivity::class.simpleName, it)
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TRANSACTION_ID_EXTRA =
            "com.cito.youoweme.TransactionDetailsActivity.TRANSACTION_ID_EXTRA"
    }
}