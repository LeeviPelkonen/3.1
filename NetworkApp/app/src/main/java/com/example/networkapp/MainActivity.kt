package com.example.networkapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.net.URL

const val CHANNEL_ID = "..."

    class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            setSupportActionBar(toolbar)
            Button.setOnClickListener {getData()}

            fab.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

        fun getData(){
            val name = editText.text.toString()

            val myURLparams = URLparams(name)
            GetConn().execute(myURLparams)

            toaster("getting data")
        }

        fun toaster(notification:String){
            Toast.makeText(
                this,
                notification,
                Toast.LENGTH_SHORT
            ).show()
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = getString(R.string.channel_description)
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as
                            NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun notifyUser(notification: String){
            createNotificationChannel()
            val notif = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notify_title))
                .setContentText(notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            NotificationManagerCompat.from(this).notify(1, notif)
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            return when (item.itemId) {
                R.id.action_settings -> true
                else -> super.onOptionsItemSelected(item)
            }
        }
        data class URLparams(val userName: String)

        inner class GetConn : AsyncTask<URLparams, Unit, String>() {

            override fun doInBackground(vararg urlp: URLparams): String {
                var str = ""
                try {
                    //Log.d("TEST","doing in background")
                    val url = "https://secure.runescape.com/m=hiscore_oldschool/index_lite.ws?player="
                    val myUrl = URL(url + urlp[0].userName)
                    val myCon = myUrl.openConnection()
                    val instream = myCon.getInputStream()
                    val allText = instream.bufferedReader().use{it.readText()}
                    val result = StringBuilder()
                    result.append(allText)
                    str = result.toString()
                } catch (e: Exception) {
                    Log.d("TEST",e.toString())
                }
                return str.substringAfter(",").substringBefore(",")
            }
            override fun onPostExecute(result: String?) {
                //Log.d("TEST","on post execute")
                totalLevel.text = result
                notifyUser("Total level of ${editText.text} is $result")
            }
        }
    }