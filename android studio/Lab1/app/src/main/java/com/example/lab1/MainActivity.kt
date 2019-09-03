package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var sadFragment: sad
    lateinit var happyFragment: happy
    lateinit var fTrans: FragmentTransaction
    lateinit var fManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        happyFragment = happy()
        sadFragment = sad()
        buttonHappy.setOnClickListener{swapHappy()}
        buttonSad.setOnClickListener{swapSad()}
        fManager = supportFragmentManager
        fTrans = fManager.beginTransaction()
        fTrans.add(R.id.fcontainer, happyFragment)
        fTrans.commit()
    }

    fun swapHappy(){
        fTrans = fManager.beginTransaction()
        fTrans.replace(R.id.fcontainer, happyFragment)
        fTrans.commit()
        //println("Happy")
    }
    fun swapSad(){
        fTrans = fManager.beginTransaction()
        fTrans.replace(R.id.fcontainer, sadFragment)
        fTrans.commit()
        //println("sad")
    }
}

