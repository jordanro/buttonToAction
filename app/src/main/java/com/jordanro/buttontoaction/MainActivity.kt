package com.jordanro.buttontoaction

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.jordanro.buttontoaction.data.entities.Action
import com.jordanro.buttontoaction.data.entities.Contact
import com.jordanro.buttontoaction.ui.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val CONTACT_REQUEST_CALL_ACTION = 777
    val CONTACT_REQUEST_NOTIFICATION_ACTION = 888

    val NOTIFICATION_INTENT_KEY = "NOTIFICATION_INTENT_KEY"

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var currentAction : Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionButton.setOnClickListener { handleActionClicked()}
        viewModel.currentAction.observe(this, Observer { action -> onActionUpdated(action) })

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if(intent!=null && intent.getBooleanExtra(NOTIFICATION_INTENT_KEY,false)){
            launchContactList(CONTACT_REQUEST_NOTIFICATION_ACTION)
        }
    }

    private fun onActionUpdated(action: Action){
        currentAction =  action
        actionButton.isEnabled = !action.type.equals(Action.Type.disabled)
    }

    private fun handleActionClicked(){
        val type = currentAction.type
        when(currentAction.type){
            Action.Type.animation -> handleAnimation()
            Action.Type.toast -> handleToast()
            Action.Type.call -> handleCall()
            Action.Type.notification -> handleNotification()
        }
    }

    private fun handleAnimation(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        actionButton.startAnimation(animation)
        viewModel.onActionDone(currentAction.type)
    }

    private fun handleToast(){
        Toast.makeText(this,getString(R.string.toast_action), Toast.LENGTH_LONG).show()
        viewModel.onActionDone(currentAction.type)
    }

    private fun handleCall(){
        launchContactList(CONTACT_REQUEST_CALL_ACTION)
    }

    private fun launchContactList(requestCode:Int) {
        startActivityForResult(
            Intent(this, ContactListActivity::class.java),
            requestCode
        )
    }

    private fun handleCall(contact : Contact){
        val title = if(TextUtils.isEmpty(contact.name)) contact.phoneNumber else contact.name
        Toast.makeText(this,getString(R.string.calling_contact,title), Toast.LENGTH_LONG).show()
        viewModel.onActionDone(currentAction.type)
    }

    private fun handleNotification(){

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(NOTIFICATION_INTENT_KEY,true)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, getString(R.string.channel_name))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.notification_title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(123, builder.build())
        }

    }

    private fun handleNotification(contact : Contact) {
        val title = if (TextUtils.isEmpty(contact.name)) contact.phoneNumber else contact.name + " " + contact.phoneNumber
        Toast.makeText(this,getString(R.string.calling_contact,title), Toast.LENGTH_LONG).show()
        viewModel.onActionDone(currentAction.type)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val contact :Contact = data?.getSerializableExtra(ContactListActivity.CONTACT_KEY) as Contact
            if(requestCode == CONTACT_REQUEST_CALL_ACTION){
                handleCall(contact)
            }
            else if(requestCode == CONTACT_REQUEST_NOTIFICATION_ACTION){
                handleNotification(contact)
            }
        }
    }
}