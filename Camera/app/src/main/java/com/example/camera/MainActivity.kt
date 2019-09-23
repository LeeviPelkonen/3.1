package com.example.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    var mCurrentPhotoPath: String = ""
    var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { saveImage() }
    }

    fun saveImage(){
        val fileName = "temp_photo"
        val imgPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        imageFile = File.createTempFile(fileName, ".jpg", imgPath )
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.camera.fileprovider",
            imageFile!!
        )
        mCurrentPhotoPath = imageFile!!.absolutePath
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (myIntent.resolveActivity(packageManager) != null) {
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
        }
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
            val imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            imageView.setImageBitmap(imageBitmap)
        }
    }
}
