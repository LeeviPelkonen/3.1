package com.example.audiorecorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import java.io.*
import kotlin.math.log

class Recorder:Runnable{

    public var recRunning = false
    private lateinit var recFile:File

    override fun run(){
        Log.d("TESTING", "run")
        val recFileName = "testkjs.raw"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        try {
            recFile = File(storageDir.toString() + "/"+ recFileName)
        } catch (ex: IOException) {
            Log.d("TESTING", ex.toString())
// Error occurred while creating the File
        }
        try {
            val outputStream = FileOutputStream(recFile)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val dataOutputStream = DataOutputStream(bufferedOutputStream)
            val minBufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT)
            val aFormat = AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build()
            val recorder = AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(aFormat)
                .setBufferSizeInBytes(minBufferSize)
                .build()
            val audioData = ByteArray(minBufferSize)
            recorder.startRecording()
            while (recRunning) {
                val numofBytes = recorder.read(audioData, 0, minBufferSize)
                if(numofBytes>0) {
                    dataOutputStream.write(audioData)
                    //Log.d("TESTING", "writing")
                }
            }
            recorder.stop()
            Log.d("TESTING", "stopping")
            dataOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("TESTING", e.toString())
        }
    }
}