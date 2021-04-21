package com.jordanro.buttontoaction

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jordanro.buttontoaction.data.entities.Contact
import com.jordanro.buttontoaction.ui.contacts.ContactListAdapter
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactListActivity :  AppCompatActivity(), ContactListAdapter.OnClickListener {

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        val CONTACT_KEY = "CONTACT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        setTitle(R.string.select_contacts)
        loadContacts()

    }

    private fun loadContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        } else {
            val activity = this
            CoroutineScope(Dispatchers.Main).launch {
                val contacts = withContext(Dispatchers.IO) {
                    getContacts()
                }
                progressBar.visibility = View.GONE
                contactList.layoutManager = LinearLayoutManager(activity)
                contactList.adapter = ContactListAdapter(activity,contacts)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun getContacts(): List<Contact> {
        val result = ArrayList<Contact>()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if(cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    )).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        if (cursorPhone!!.count > 0) {
                            while (cursorPhone.moveToNext()) {
                                val phoneNumValue = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                result.add(Contact(name,phoneNumValue))
                            }
                        }
                        cursorPhone.close()
                    }
                }
            } else {
                //   toast("No contacts available!")
            }
            cursor.close()
        }
        return result
    }

    override fun onItemClicked(contact: Contact) {
        val intent = Intent()
        intent.putExtra(CONTACT_KEY,contact)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}