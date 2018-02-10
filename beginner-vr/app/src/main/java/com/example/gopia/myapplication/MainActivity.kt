package com.example.gopia.myapplication

import android.opengl.GLES20
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.vr.sdk.base.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
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

    private lateinit var camera: FloatArray
    private lateinit var view: FloatArray
    private lateinit var headView: FloatArray
    private lateinit var modelViewProjection: FloatArray
    private lateinit var modelView: FloatArray
    private lateinit var modelFloor: FloatArray

    private lateinit var tempPosition: FloatArray
    private lateinit var headRotation: FloatArray

    private var objectDistance = MAX_MODEL_DISTANCE / 2.0f
    private var floorDepth = 20f;


    override fun onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown")
    }

    override fun onFinishFrame(p0: Viewport?) {
        TODO("not implemented")
        // nothing needed
    }

    override fun onDrawEye(p0: Eye?) {
        TODO("not implemented")

        // set flags

        // move view transform to camera

        // set position of light

        // draw cube
        // draw floor


    }

    override fun onSurfaceCreated(p0: EGLConfig?) {
        // TODO: Add surface created is where all the buffers are added
    }

    override fun onSurfaceChanged(p0: Int, p1: Int) {
        Log.i(TAG, "onSurfaceChanged")
    }

    override fun onNewFrame(p0: HeadTransform?) {
        TODO("not implemented")
        // used to modify the camera matrix
    }

    private fun initializeGVRView() {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeGVRView()
        modelCube = FloatArray(16)
        camera = FloatArray(16)
        view = FloatArray(16)
        modelViewProjection = FloatArray(16)
        modelView = FloatArray(16)
        modelFloor = FloatArray(16)
        tempPosition = FloatArray(4)

        modelPosition = floatArrayOf(0.0f, 0.0f, -MAX_MODEL_DISTANCE / 2.0f)
        headRotation = FloatArray(4)
        headView = FloatArray(16)

    }

    /**
     * Reads a text file to string
     * @param resId the resource ID of the text file
     * @return The text file as a string
     */
    private fun readRawTextFile(resId: Int): String {
        // basic file reading function - pretty sure there now exists sdk func to do this
        val inputStream = super.getResources().openRawResource(resId)
        try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                sb.append(line).append("\n")
                line = reader.readLine()
            }
            reader.close();
            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }


    override fun onCardboardTrigger() {
        // when cardboard button called
        super.onCardboardTrigger()
    }

    private fun loadGLShader(type: Int, resId: Int): Int {
        val code = readRawTextFile(resId)
        // create and compile the shader
        var shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)

        // check shader compiled successfully
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == 0) {
            Log.d(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader))
            GLES20.glDeleteShader(shader)
            shader = 0
        }

        if (shader == 0) {
            throw RuntimeException("Error creating shader")
        }

        return shader
    }


}
