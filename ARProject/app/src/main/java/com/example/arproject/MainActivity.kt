package com.example.arproject

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_layout.*
import kotlinx.android.synthetic.main.recycler_layour.*

data class Furniture(val img:Int,val text:String)

class ArMaker{

    lateinit var fragment: ArFragment
    private var testRenderable: ModelRenderable? = null

    fun doAr(currentItem: String){
        val modelUri = Uri.parse(currentItem)
        val renderableFuture = ModelRenderable.builder()
            .setSource(fragment.context, modelUri)
            .build()
        renderableFuture.thenAccept { testRenderable = it }

        fragment.setOnTapArPlaneListener { hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent? ->
            if (testRenderable == null) {
                Log.d("QWERTY", "NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL")
                return@setOnTapArPlaneListener
            }
            Log.d("QWERTY", "inside set on tap plane listener")
            val anchor = hitResult!!.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(fragment.arSceneView.scene)
            val viewNode = TransformableNode(fragment.transformationSystem)
            viewNode.setParent(anchorNode)
            viewNode.renderable = testRenderable
            viewNode.select()

        }
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var fragment: ArFragment
    private var testRenderable: ModelRenderable? = null
    val maker = ArMaker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            popUp()
        }
        maker.fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment
        maker.doAr("drawer.sfb")
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

    fun popUp(){
        // Initialize a new layout inflater instance
        val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.popup_layout,null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        popupWindow.elevation = 10.0F
        // Create a new slide animation for popup window enter transition
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.END
        popupWindow.exitTransition = slideOut

        // Get the widgets reference from custom view
        val buttonPopup = view.findViewById<Button>(R.id.button_popup)
        val recyclerM = view.findViewById<RecyclerView>(R.id.recyclerMain)

        recyclerM.layoutManager = LinearLayoutManager(this)

        val itemList = mutableListOf<Furniture>()
        itemList.add(Furniture(R.drawable.drawer,"Drawer"))
        itemList.add(Furniture(R.drawable.lamp,"Lamp"))
        itemList.add(Furniture(R.drawable.oben,"Oben"))
        itemList.add(Furniture(R.drawable.stool,"Stool"))
        recyclerM.adapter = MainAdapter(itemList,maker)


        // Set a click listener for popup's button widget
        buttonPopup.setOnClickListener{
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        // Set a dismiss listener for popup window
        popupWindow.setOnDismissListener {
            Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
        }


        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(root_layout)
        popupWindow.showAtLocation(
            root_layout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )
    }
}