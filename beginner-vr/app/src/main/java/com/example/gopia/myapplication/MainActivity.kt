package com.example.gopia.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.vr.sdk.base.*
import javax.microedition.khronos.egl.EGLConfig

class MainActivity : GvrActivity(), GvrView.StereoRenderer {
    override fun onRendererShutdown() {
        TODO("not implemented")
    }

    override fun onFinishFrame(p0: Viewport?) {
        TODO("not implemented")
    }

    override fun onDrawEye(p0: Eye?) {
        TODO("not implemented")
    }

    override fun onSurfaceCreated(p0: EGLConfig?) {
        TODO("not implemented")
    }

    override fun onSurfaceChanged(p0: Int, p1: Int) {
        TODO("not implemented")
    }

    override fun onNewFrame(p0: HeadTransform?) {
        TODO("not implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // standard android load layout
        setContentView(R.layout.activity_main)

        // we then retrieve the vr view
        var gvrView : GvrView = findViewById(R.id.gvr_view)
        // and configure it's params
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8)


        // define a renderer for the view to display
        gvrView.setRenderer(this)
        // TODO: Check out what this line does:
        gvrView.setTransitionViewEnabled(true)


        if(gvrView.setAsyncReprojectionEnabled(true)) {
            // Asyn reprojection - decouples framerate from display framerate,
            // prevents throttling from performance mode effecting logic
            AndroidCompat.setSustainedPerformanceMode(this, true);
        }

        // then just set the gvrView for the class
        setGvrView(gvrView);
    }


    private fun loadGLShader(type : Int, resId : Int) : Int {
        TODO("Not implemented")
    }
}
