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
        setContentView(R.layout.activity_main)
    }


    private fun loadGLShader(type : Int, resId : Int) : Int {
        TODO("Not implemented")
    }
}
