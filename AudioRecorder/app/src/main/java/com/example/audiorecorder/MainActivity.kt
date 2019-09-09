package com.example.audiorecorder

import android.media.AudioRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    val recorderRunnable = Recorder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start.setOnClickListener {startRecording()}
        stop.setOnClickListener {stopRecording()}
        play.setOnClickListener {playRecording()}
    }

    fun startRecording(){
        Log.d("TESTING", "starting")
        recorderRunnable.recRunning = true
        val recorderThread = Thread(recorderRunnable)
        recorderThread!!.start()
    }

    fun stopRecording(){
        Log.d("TESTING", "stopping")
        recorderRunnable.recRunning = false
    }

    fun playRecording(){
        Log.d("TESTING", "playing")
        val recFileName = "testkjs.raw"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val playerRunnable = Player(File(storageDir.toString() + "/"+ recFileName).inputStream())
        val playerThread = Thread(playerRunnable)
        playerThread!!.start()
    }
}
