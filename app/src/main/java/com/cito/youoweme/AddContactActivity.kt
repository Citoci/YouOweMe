package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager
import com.google.android.material.snackbar.Snackbar


class AddContactActivity :
    AppCompatActivity() /*, ImportContactDialog.ContactSelectorListener, LoaderManager.LoaderCallbacks<Cursor>*/ {

//    private val providedContacts = arrayListOf<Contact>()

    // UI Elements
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var importBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

//        LoaderManager.getInstance(this).initLoader(0, null, this)

        nameEditText = findViewById(R.id.edittext_contact_name)
        surnameEditText = findViewById(R.id.edittext_contact_surname)
        saveBtn = findViewById(R.id.btn_save_new_contact)
        importBtn = findViewById(R.id.btn_import_contact)

        saveBtn.setOnClickListener {
            addContact()
        }

        importBtn.apply {
            isEnabled = UserLoginManager.isLogged
            setOnClickListener {
//                ImportContactDialog(providedContacts.map { "$it" }.toTypedArray()).show(supportFragmentManager, "")
                startActivityForResult(
                    Intent(
                        Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI
                    ),
                    CONTACT_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            CONTACT_REQUEST_CODE -> {
                if (resultCode != RESULT_OK) {
                    Log.d(this::class.simpleName, "contact not selected")
                    return
                }
                intent?.data?.let { uri ->
//                    Log.d(this::class.simpleName, uri.toString() )

                    var lookup = ""
                    managedQuery(
                        uri,
                        arrayOf(ContactsContract.Contacts.LOOKUP_KEY),
                        null, null, null
                    )?.apply {
                        moveToFirst()
                        lookup = getString(0)
//                        close() // do not call with "managedQuery()"
                    }

                    managedQuery(
                        ContactsContract.Data.CONTENT_URI,
                        arrayOf(
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        ),
                        "${ContactsContract.Data.LOOKUP_KEY} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                        arrayOf(lookup, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
                        null
                    )?.apply {
                        moveToFirst()
//                        Log.d(this::class.simpleName, "surname: ${getString(0)}, name: ${getString(1)}")
                        nameEditText.setText(getString(1))
                        surnameEditText.setText(getString(0))
//                        close()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, intent)
        }
    }

    private fun checkInput(): Boolean {
        val regex = Regex("[A-Za-z]+")
        if (!nameEditText.text.toString().matches(regex)) {
            Snackbar.make(saveBtn, "Name not valid", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (!surnameEditText.text.toString().matches(regex)) {
            Snackbar.make(saveBtn, "Surname not valid", Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addContact() {
        if (!checkInput()) return

        ContactsSQLiteDAO.insert(
            Contact(
                name = nameEditText.text.toString(),
                surname = surnameEditText.text.toString(),
                usernameRef = UserLoginManager.loggedUsername
            )
        )
        finish()
    }

/*
    override fun onContactSelected(index: Int) {
        nameEditText.setText(providedContacts[index].name)
        surnameEditText.setText(providedContacts[index].surname)
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
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) { }
*/

    companion object {
        private const val CONTACT_REQUEST_CODE = 3103
    }
}

/*
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
*/