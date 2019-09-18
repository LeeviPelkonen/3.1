package com.example.imagetracking

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {
    private lateinit var fragment: ArFragment
    private var iglooRenderable: ModelRenderable? = null
    private var andyRenderable: ModelRenderable? = null
    private lateinit var fitToScanImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = supportFragmentManager.findFragmentById(R.id.arimage_fragment) as ArFragment
        fitToScanImageView = findViewById(R.id.fit_to_scan_img)

        val igloo = ModelRenderable.builder()
            .setSource(this, Uri.parse("broomstick.sfb"))
            .build()
        igloo.thenAccept { iglooRenderable = it }

        val andy = ModelRenderable.builder()
            .setSource(this, Uri.parse("table.sfb"))
            .build()
        andy.thenAccept { andyRenderable = it }

        fragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            onUpdate(frameTime)
        }
    }

    private fun onUpdate(frameTime: FrameTime) {
        fragment.onUpdate(frameTime)
        val arFrame = fragment.arSceneView.arFrame
        if (arFrame == null || arFrame.camera.trackingState != TrackingState.TRACKING) {
            return
        }
        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        updatedAugmentedImages.forEach {
            when (it.trackingState) {
                TrackingState.PAUSED -> {
                    Log.d("QWERTY", "Paused state")
                }
                TrackingState.STOPPED -> {
                    Log.d("QWERTY", "STOPPED")
                }
                TrackingState.TRACKING -> {
                    var anchors = it.anchors
                    if (anchors.isEmpty()) {
                        fitToScanImageView.visibility = View.GONE
                        val pose = it.centerPose
                        val anchor = it.createAnchor(pose)
                        val anchorNode = AnchorNode(anchor)
                        anchorNode.setParent(fragment.arSceneView.scene)
                        val imgNode = TransformableNode(fragment.transformationSystem)
                        imgNode.setParent(anchorNode)
                        if (it.name == "color") {
                            imgNode.renderable = iglooRenderable
                        } else {
                            imgNode.renderable = andyRenderable
                        }
                    }
                }
            }
        }
    }
}
