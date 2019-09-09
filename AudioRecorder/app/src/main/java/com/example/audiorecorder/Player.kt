package com.example.audiorecorder

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.io.IOException
import java.io.InputStream

class Player(instream: InputStream): Runnable{
    val inputStream = instream
    lateinit var track1:AudioTrack

    override fun run() {
        val minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT)
        val aBuilder = AudioTrack.Builder()
        val aAttr: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val aFormat: AudioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(44100)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .build()
        track1 = aBuilder.setAudioAttributes(aAttr)
            .setAudioFormat(aFormat)
            .setBufferSizeInBytes(minBufferSize)
            .build()
        track1!!.setVolume(0.2f)
        track1!!.play()
        var i = 0
        val buffer = ByteArray(minBufferSize)
        try {
            i = inputStream.read(buffer, 0, minBufferSize)
            while (i != -1) {
                track1!!.write(buffer, 0, i)
                i = inputStream.read(buffer, 0, minBufferSize)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        track1!!.stop()
        track1!!.release()
    }
}