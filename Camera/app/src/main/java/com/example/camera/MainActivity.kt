package com.example.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { takeImage() }
    }

    fun saveImage(){
        val fileName = "temp_photo"
        val imgPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var imageFile: File? = null
        imageFile = File.createTempFile(fileName, ".jpg", imgPath )

    }

    fun takeImage(){
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val activityInfo = intent.resolveActivityInfo(packageManager, intent.flags)
        if (activityInfo.exported) {
            startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, recIntent: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = recIntent!!.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
}
