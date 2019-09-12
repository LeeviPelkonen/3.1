package com.example.externalstorageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import kotlinx.android.synthetic.main.fragment_read.*
import kotlinx.android.synthetic.main.fragment_write.*
import java.io.File
import java.lang.Exception

const val SHAREFILE = "shared.txt"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        writeText.setOnClickListener { write() }
        read()
    }

    private fun write(){
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED) {
            val file = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), SHAREFILE)
            file.appendText("${editText.text}\n")
            editText.text.clear()
        }
        read()
    }

    private fun read(){
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED) {
            try {
                val file = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), SHAREFILE)
                textView.text = file.readText()
            }catch (e:Exception){
                Log.d("TEST",e.toString())
            }
        }
    }
}
