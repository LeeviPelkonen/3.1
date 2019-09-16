package com.example.kotlinadapter

import android.app.Activity
import android.content.Context
import android.graphics.ColorSpace
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the adapter to convert the array to views
        val adapter = PresidentListAdapter(this, GlobalModel.presidents)

        // use a custom layout (instead of the ListActivity default layout)
        setContentView(R.layout.activity_main)

        // attach the adapter to a ListView
        mainlistview.adapter = adapter

        mainlistview.setOnItemClickListener { _, _, position, _ ->
            Log.d("USR", "Selected $position")
            selname.text = GlobalModel.presidents[position].toString()
            seldescription.text = GlobalModel.presidents[position].description
            getData(GlobalModel.presidents[position].name)
        }

        mainlistview.setOnItemLongClickListener { _, _, position, _ ->
            val selectedPresident = GlobalModel.presidents[position]
            val detailIntent = PresidentDetailActivity.newIntent(this, selectedPresident)

            startActivity(detailIntent)
            true
        }
    }

    object DemoApi {
        const val URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch="

        object Model {
            data class DataResponse(val query: DataQuery)
            data class DataQuery(val search: SearchInfo)
            data class SearchInfo(val totalHits: Int)
        }

        interface Service {
            @GET("api.php")
            fun name(@Query("name") action: String): Call<Model.DataResponse>
        }

        private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(
            GsonConverterFactory.create())
            .build()

        val service = retrofit.create(Service::class.java)!!
    }
/*
    fun callWebService() {
        val call = DemoApi.service.name("Trump")

        val value = object : Callback<DemoApi.Model.DataResponse> {
            override fun onResponse(call: Call<DemoApi.Model.DataResponse>, response:
                        Response<DemoApi.Model.DataResponse>?) {
                if (response != null) {
                    var res: DemoApi.Model.DataResponse = response.body()!!

                    Log.d("DBG", "$res")// just for the demo
                }
            }

        override fun onFailure(call: Call<DemoApi.Model.SearchInfo>, t: Throwable) {
            Log.e("DBG", t.toString())
        }
    }
        call.enqueue(value) // asynchronous request
}
*/



    fun getData(name: String){
        val myURLparams = URLparams(name)
        GetConn().execute(myURLparams)
    }

    data class URLparams(val name: String)

    inner class GetConn : AsyncTask<URLparams, Unit, String>() {

        override fun doInBackground(vararg urlp: URLparams): String {
            var str = ""
            try {
                //Log.d("TEST","doing in background")
                val url = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch="
                val myUrl = URL(url + urlp[0].name)
                val myCon = myUrl.openConnection()
                val instream = myCon.getInputStream()
                val allText = instream.bufferedReader().use{it.readText()}
                val result = StringBuilder()
                result.append(allText)
                str = result.toString()
            } catch (e: Exception) {
                Log.d("TEST",e.toString())
            }
            return str
        }
        override fun onPostExecute(result: String?) {
            //Log.d("TEST","on post execute")
            val a = result
            hits.text = "$result hits"
            Log.d("QWERTY",result.toString())
        }
    }

    private inner class PresidentListAdapter(context: Context, private val presidents: MutableList<President>) : BaseAdapter() {
        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return presidents.size
        }

        override fun getItem(position: Int): Any {
            return presidents[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rowView = inflater.inflate(R.layout.item_president, parent, false)

            val thisPresident = presidents[position]
            var tv = rowView.findViewById(R.id.tvName) as TextView
            tv.text = thisPresident.name

            tv = rowView.findViewById(R.id.tvStartDuty) as TextView
            tv.text = Integer.toString(thisPresident.startDuty)

            tv = rowView.findViewById(R.id.tvEndDuty) as TextView
            tv.text = Integer.toString(thisPresident.endDuty)

            return rowView
        }
    }
}
