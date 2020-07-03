package kr.co.sbproject.recipe.ui.fragment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.vp_main_pager
import kr.co.sbproject.recipe.App
import kr.co.sbproject.recipe.R
import kr.co.sbproject.recipe.`interface`.ApiInterface
import kr.co.sbproject.recipe.ui.activity.MainActivity
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val KEY_EVENT_ACTION = "key_event_action"

const val KEY_EVENT_EXTRA = "key_event_extra"
/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class MainFragment : BaseFragment() {
    override fun getResourceId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also {
            dl.setScrimColor(Color.TRANSPARENT)
            val actionBarDrawerToggle: ActionBarDrawerToggle =
                object : ActionBarDrawerToggle(it, dl, R.string.app_name, R.string.app_name) {
                    override fun onDrawerSlide(
                        drawerView: View,
                        slideOffset: Float
                    ) {
                        super.onDrawerSlide(drawerView, slideOffset)
                        val slideX = drawerView.width * slideOffset
                        content.translationX = slideX
                    }

                    override fun onDrawerOpened(drawerView: View) {
                        super.onDrawerOpened(drawerView)
                        Log.i(TAG, "start")
                        if (!hasPermissions(PERMISSIONS)) {
                            //퍼미션 허가 안되어있다면 사용자에게 요청
                            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)
                        } else {
                            settingCamera()
                        }
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        super.onDrawerClosed(drawerView)
                        Log.i(TAG, "stop")
                        // Shut down our background executor
                        cameraExecutor.shutdown()

                        // Unregister the broadcast receivers and listeners
                        broadcastManager.unregisterReceiver(volumeDownReceiver)
                        displayManager.unregisterDisplayListener(displayListener)
                    }

                }
            dl.addDrawerListener(actionBarDrawerToggle)

            vp_main_pager.apply {
                adapter = MainViewPagerAdapter(it.supportFragmentManager, lifecycle)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
//                        bnv_pager.menu.getItem(position).isChecked = true
                    }
                })
                (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                isUserInputEnabled = false
            }

            bnv_pager.setOnNavigationItemSelectedListener { item ->
                val position = when (item.itemId) {
                    R.id.menu_kitchen -> {
                        0
                    }
                    R.id.menu_calendar -> {
                        1
                    }
                    R.id.menu_edit -> {
                        2
                    }
                    R.id.menu_list -> {
                        3
                    }
                    else -> {
                        0
                    }
                }
                vp_main_pager.setCurrentItem(position, false)
                true
            }
        }
    }

    override fun onUiStart() {
    }

    override fun onUiResume() {
    }

    override fun onUiPause() {
    }

    override fun onUiStop() {
    }

    /** viewpager adapter **/
    inner class MainViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RefrigeratorFragment()
                1 -> CalendarFragment()
                2 -> WriteFragment()
                3 -> RecipeFragment()
                else -> ErrorFragment()
            }
        }

        override fun getItemCount() = 4
    }

    /** camera **/
    private val PERMISSIONS_REQUEST_CODE = 1000

    private var PERMISSIONS = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    /** Volume down button receiver used to trigger shutter */
    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                // When the volume down button is pressed, simulate a shutter button click
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                    val shutter = container
//                        .findViewById<ImageButton>(R.id.camera_capture_button)
//                    shutter.simulateClick()
                }
            }
        }
    }

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@MainFragment.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        var result: Int

        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (perms in permissions) {
            result = ContextCompat.checkSelfPermission(mContext, perms)
            if (result == PackageManager.PERMISSION_DENIED) {
                //허가 안된 퍼미션 발견
                return false
            }
        }
        //모든 퍼미션이 허가되었음
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                //TODO Camera Start
                settingCamera()
            } else {
                Toast.makeText(
                    mContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(mContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun settingCamera() {
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        broadcastManager = LocalBroadcastManager.getInstance(mContext)

        // Set up the intent filter that will receive events from our main activity
        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)

        // Determine the output directory
        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        // Wait for the views to be properly laid out
        pv_finder.post {

            // Keep track of the display in which this view is attached
            displayId = pv_finder.display.displayId

            // Build UI controls
            updateCameraUi()

            // Set up the camera and its use cases
            setUpCamera()
        }
    }

    /**
     * Inflate camera controls and update the UI manually upon config changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     *
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Redraw the camera UI controls
        updateCameraUi()

        // Enable or disable switching between cameras
        updateCameraSwitchButton()
    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // Enable or disable switching between cameras
            updateCameraSwitchButton()

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { pv_finder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = pv_finder.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation
            .setTargetRotation(rotation)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            // We request aspect ratio but no resolution to match preview config, but letting
            // CameraX optimize for whatever specific resolution best fits our use cases
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .build()

        // ImageAnalysis
        imageAnalyzer = ImageAnalysis.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .build()
            // The analyzer can then be assigned to the instance
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                    // Values returned from our analyzer are passed to the attached listener
                    // We log image analysis results here - you should do something useful
                    // instead!
                    Log.d(TAG, "Average luminosity: $luma")
                })
            }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )

            // Attach the pv_finder's surface provider to preview use case
            preview?.setSurfaceProvider(pv_finder.createSurfaceProvider())
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }


    /**
     *  [androidx.camera.core.ImageAnalysisConfig] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** Method used to re-draw the camera UI controls, called every time configuration changes. */
    private fun updateCameraUi() {

    }

    /** Enabled or disabled a button to switch cameras depending on the available cameras */
    private fun updateCameraSwitchButton() {

    }

    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    /**
     * Our custom image analysis class.
     *
     * <p>All we need to do is override the function `analyze` with our desired operations. Here,
     * we compute the average luminosity of the image by looking at the Y plane of the YUV frame.
     */
    @OptIn(ExperimentalStdlibApi::class)
    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * Used to add listeners that will be called with each luma computed
         */
        fun onFrameAnalyzed(listener: LumaListener) = listeners.add(listener)

        /**
         * Helper extension function used to extract a byte array from an image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        /**
         * Analyzes an image to produce a result.
         *
         * <p>The caller is responsible for ensuring this analysis method can be executed quickly
         * enough to prevent stalls in the image acquisition pipeline. Otherwise, newly available
         * images will not be acquired and analyzed.
         *
         * <p>The image passed to this method becomes invalid after this method returns. The caller
         * should not store external references to this image, as these references will become
         * invalid.
         *
         * @param image image being analyzed VERY IMPORTANT: Analyzer method implementation must
         * call image.close() on received images when finished using them. Otherwise, new images
         * may not be received or the camera may stall, depending on back pressure setting.
         *
         */
        override fun analyze(image: ImageProxy) {
            // If there are no listeners attached, we don't need to perform analysis
            if (listeners.isEmpty()) {
                image.close()
                return
            }

            // Keep track of frames analyzed
            val currentTime = System.currentTimeMillis()
            frameTimestamps.add(currentTime)

            // Compute the FPS using a moving average
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.first() ?: currentTime
            val timestampLast = frameTimestamps.last() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            // Analysis could take an arbitrarily long amount of time
            // Since we are running in a different thread, it won't stall other use cases

            lastAnalyzedTimestamp = frameTimestamps.first()

            // Since format in ImageAnalysis is YUV, image.planes[0] contains the luminance plane
            val buffer = image.planes[0].buffer

            // Extract image data from callback object
            val data = buffer.toByteArray()

            // Convert the data into an array of pixel values ranging 0-255
            val pixels = data.map { it.toInt() and 0xFF }

            // Compute average luminance for the image
            val luma = pixels.average()

            // Call all listeners with new value
            listeners.forEach { it(luma) }

            image.close()
        }
    }

    companion object {
        private const val TAG = "sgim"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }
}
