package com.cito.youoweme

import android.app.Dialog
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager

class AddContactActivity : AppCompatActivity(), ImportContactDialog.ContactSelectorListener, LoaderManager.LoaderCallbacks<Cursor>  {

    private val providedContacts = arrayListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        LoaderManager.getInstance(this).initLoader(0, null, this)

        findViewById<Button>(R.id.btn_save_new_contact).setOnClickListener {
            addContact()
        }

        findViewById<Button>(R.id.btn_import_contact).setOnClickListener {
            ImportContactDialog(providedContacts.map { "$it" }.toTypedArray()).show(supportFragmentManager, "")
        }
    }

    private fun checkInput(): Boolean {
        return true
        // TODO check add contact inputs
    }

    private fun addContact() {
        if (!checkInput()) return

        ContactsSQLiteDAO.insert(Contact(
            name = findViewById<EditText>(R.id.edittext_contact_name).text.toString(),
            surname = findViewById<EditText>(R.id.edittext_contact_surname).text.toString(),
            usernameRef = UserLoginManager.loggedUsername
        ))
        finish()
    }

    override fun onContactSelected(index: Int) {
        findViewById<EditText>(R.id.edittext_contact_name).setText(providedContacts[index].name)
        findViewById<EditText>(R.id.edittext_contact_surname).setText(providedContacts[index].surname)
//        TODO("Not yet implemented")
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.LOOKUP_KEY,
//                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
            ),
            null,
            null,
            null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data?.apply {
            moveToFirst()
            while (!isAfterLast) {
                providedContacts.add(Contact(surname = getString(0), name = getString(1)))
                moveToNext()
            }
            close()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) { }


}

class ImportContactDialog(private val contactNames: Array<String>): DialogFragment() {
    private lateinit var listener: ContactSelectorListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener = activity as? ContactSelectorListener ?: throw IllegalStateException("Activity must listen!")
        return activity?.let {

            AlertDialog.Builder(it)
                .setTitle(R.string.title_contact_import_dialog)
                .setItems(contactNames) { _, index -> listener.onContactSelected(index) }
                .create()
        } ?: throw IllegalStateException("Activity cant be null")
    }

    interface ContactSelectorListener {
        fun onContactSelected(index: Int)
    }
}