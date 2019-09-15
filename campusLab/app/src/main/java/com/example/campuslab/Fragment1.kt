package com.example.campuslab

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.campuslab.databinding.FragmentFragment1Binding
import java.io.StringWriter
import java.lang.reflect.Array


class Fragment1 : Fragment() {

    lateinit var bindingclass: FragmentFragment1Binding

    val campuses = Campus("Leiritie 1 \n hämeentie 135 \n Myllypurontie 1 \n Karaportti 2")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingclass = DataBindingUtil.inflate(inflater, R.layout.fragment_fragment1, container, false)
        bindingclass.campus = campuses
        Log.d("qwerty","hello im testing this $campuses")
        return bindingclass.root
    }
}

class Campus(campus: String){
    val name = campus
}
