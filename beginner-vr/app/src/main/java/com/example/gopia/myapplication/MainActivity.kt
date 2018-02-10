package com.example.gopia.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.vr.sdk.base.*
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig

class MainActivity : GvrActivity(), GvrView.StereoRenderer {
    private val TAG: String = "myapplication"

    private lateinit var modelCube: FloatArray
    private lateinit var modelPosition: FloatArray

    // used to characterize the view
    private val Z_NEAR = 0.1f          // near plane clipping
    private val Z_FAR = 100.0f         // far plane clipping
    private val CAMERA_Z = 0.01f       // TODO: Find out what this z value is used for

    private val TIME_DELTA = 0.3f      // probably the unit update time for the simulation
    private val YAW_LIMIT = 0.12f      // yaw-epsilon used for registering clicks
    private val PITCH_LIMIT = 0.12f    // pitch-epsilon used for registering clicks

    private val COORDS_PER_VERTEX = 3  // dimensionality ??? TODO: find out what this is

    private val LIGHT_POS_IN_WORLD_SPACE = floatArrayOf(0.0f, 2.0f, 0.0f, 1.0f)
    private val POS_MATRIX_MULTIPLY_VEC = floatArrayOf(0F, 0F, 0F, 1.0f) // used to extract the position from a transform matrix
    private val MIN_MODEL_DISTANCE = 3.0f                                // TODO: Find out why these values exist
    private val MAX_MODEL_DISTANCE = 7.0f

    private val lightPosInEyeSpace = FloatArray(4) // intermediate array used for gluniforms?

    private lateinit var floorVertices: FloatBuffer
    private lateinit var floorColours: FloatBuffer
    private lateinit var floorNormals: FloatBuffer

    private lateinit var cubeVertices: FloatBuffer
    private lateinit var cubeColours: FloatBuffer
    private lateinit var cubeFoundColours: FloatBuffer
    private lateinit var cubeNormals: FloatBuffer

    private var cubeProgram = 0
    private var floorProgram = 0

    // used to keep track of glLocations
    private var cubePositionParam = 0
    private var cubeNormalParam = 0
    private var cubeColorParam = 0
    private var cubeModelParam = 0
    private var cubeModelViewParam = 0
    private var cubeModelProjectionParam = 0
    private var cubeLightPosParam = 0

    private var floorPositionParam = 0
    private var floorNormalParam = 0
    private var floorColorParam = 0
    private var floorModelParam = 0
    private var floorModelViewParam = 0
    private var floorModelProjectionParam = 0
    private var floorLightPosParam = 0

    private lateinit var camera : FloatArray
    private lateinit var view : FloatArray
    private lateinit var headView : FloatArray
    private lateinit var modelViewProjection : FloatArray
    private lateinit var modelView : FloatArray
    private lateinit var modelFloor : FloatArray

    private lateinit var tempPosition : FloatArray
    private lateinit var headRotation : FloatArray

    private var objectDistance = MAX_MODEL_DISTANCE / 2.0f
    private var floorDepth = 20f;


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
        var gvrView: GvrView = findViewById(R.id.gvr_view)

        // and configure it's params
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8)


        // define a renderer for the view to display
        gvrView.setRenderer(this)

        // TODO: Check out what this line does:
        gvrView.setTransitionViewEnabled(true)


        if (gvrView.setAsyncReprojectionEnabled(true)) {
            // Asyn reprojection - decouples framerate from display framerate,
            // prevents throttling from performance mode effecting logic
            AndroidCompat.setSustainedPerformanceMode(this, true)
        }

        // then just set the gvrView for the class
        setGvrView(gvrView)
    }


    private fun loadGLShader(type: Int, resId: Int): Int {
        TODO("Not implemented")
    }
}
