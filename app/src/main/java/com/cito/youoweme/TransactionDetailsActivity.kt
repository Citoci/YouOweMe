package com.cito.youoweme

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ActivityTransactionDetailsBinding
import com.cito.youoweme.login.UserLoginManager
import com.cito.youoweme.notifications.RememberNotificationBroadcastReceiver
import com.google.android.material.snackbar.Snackbar
import java.util.*


class TransactionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionDetailsBinding

    private var transaction: Transaction? = null
    private var notificationManager: NotificationManager? = null

    // Notification Scheduling Properties
    var notificationScheduleCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting the transaction info
        transaction =
            intent.extras?.getLong(TRANSACTION_ID_EXTRA)?.let { TransactionsSQLiteDAO.getById(it) }
        val contact = transaction?.contactId?.let { ContactsSQLiteDAO.getById(it) }

        if (transaction == null) {
            Log.e(TransactionDetailsActivity::class.simpleName, "transaction not found")
            finish()
            return
        }

        // Displaying the transaction info
        binding.textviewAmount.text = getString(R.string.format_euros, transaction?.amount)
        binding.textviewContact.text = getString(if (transaction!!.amount >= 0) R.string.message_he_owes_you else R.string.message_you_owe_him, contact.toString())
        binding.textviewDate.text = getString(R.string.format_date, Date(transaction!!.timeInMillis))
        binding.textviewTitle.text = "${transaction?.title}"
        binding.textviewDesc.text = "${transaction?.desc}"

        if (UserLoginManager.loggedUser == null) {
            binding.btnNotify.isEnabled = false
        }

        // Instantiating the Pickers
        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker?, h: Int, m: Int ->
                notificationScheduleCalendar.set(Calendar.HOUR, h)
                notificationScheduleCalendar.set(Calendar.MINUTE, m)
                notificationScheduleCalendar.set(Calendar.SECOND, 0)
                scheduleNotification()
            },
            notificationScheduleCalendar.get(Calendar.HOUR),
            notificationScheduleCalendar.get(Calendar.MINUTE),
            true
        )
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, y: Int, m: Int, d: Int ->
                notificationScheduleCalendar.set(Calendar.YEAR, y)
                notificationScheduleCalendar.set(Calendar.MONTH, m)
                notificationScheduleCalendar.set(Calendar.DAY_OF_MONTH, d)
                timePickerDialog.show()
            },
            notificationScheduleCalendar[Calendar.YEAR],
            notificationScheduleCalendar.get(Calendar.MONTH),
            notificationScheduleCalendar.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = notificationScheduleCalendar.timeInMillis }

        // Listeners
        binding.btnNotify.setOnClickListener {
//            Snackbar.make(it, "Not yet implemented", Snackbar.LENGTH_SHORT).show()

            datePickerDialog.show()
        }

        binding.btnDelete.setOnClickListener {
            TransactionsSQLiteDAO.delete(transaction!!)
            finish()
        }

    }

    private fun scheduleNotification() {
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
            AlarmManager.RTC_WAKEUP,
            notificationScheduleCalendar.timeInMillis,
            PendingIntent.getBroadcast(
                this,
                PENDING_INTENT_REQ_CODE,
                Intent(this, RememberNotificationBroadcastReceiver::class.java).apply {
                    putExtra(RememberNotificationBroadcastReceiver.NOTIFICATION_ID_EXTRA, transaction?.id)
                    putExtra(RememberNotificationBroadcastReceiver.NOTIFICATION_TITLE_EXTRA, "title")
                    putExtra(RememberNotificationBroadcastReceiver.NOTIFICATION_TEXT_EXTRA, "text")
                },
                0
            )
        )

        (getString(R.string.message_notification_scheduled) + " ${notificationScheduleCalendar.time}").let {
//            Log.d(TransactionDetailsActivity::class.simpleName, it)
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TRANSACTION_ID_EXTRA =
            "com.cito.youoweme.TransactionDetailsActivity.TRANSACTION_EXTRA_KEY"
        const val PENDING_INTENT_REQ_CODE = 11
    }
}