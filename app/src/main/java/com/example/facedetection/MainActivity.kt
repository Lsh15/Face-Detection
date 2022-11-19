package com.example.facedetection

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.facedetection.databinding.ActivityMainBinding
import com.example.facedetection.others.Constants.KEY_COLOR
import com.google.android.gms.vision.Frame
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
//import com.google.mlkit.vision.face.Face
//import com.google.mlkit.vision.face.FaceDetection
//import com.google.mlkit.vision.face.FaceDetector
//import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
    //변수 선언
    lateinit var binding: ActivityMainBinding

    var lensFacing = CameraSelector.LENS_FACING_FRONT

    var camera: Camera? = null
    var cameraProvider: ProcessCameraProvider? = null
    lateinit var cameraExecutor: ExecutorService

    var preview: Preview? = null
    var imageCapture: ImageCapture? = null
    var imageAnalysis: ImageAnalysis? = null

    var width = 320
    var height = 240

    lateinit var sharedPreferences: SharedPreferences

    var colorState : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        cameraExecutor = Executors.newSingleThreadExecutor()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)

        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE)

//        setUpCamera()

        change_btn.setOnClickListener {
            var intent = Intent(this,LensSettings::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        setUpCamera()
    }


    fun setUpCamera(){ // camera thread 생성 부분
        var cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    fun bindCameraUseCases(){ // camera 기본 설정 부분
        var rotation = Surface.ROTATION_270
        var cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        var color =sharedPreferences.getInt(KEY_COLOR,1)

        when (color) {
            1 -> {
                colorState = R.drawable.pink_lens
            }
            2 -> {
                colorState = R.drawable.blue_lens
            }
            3 -> {
                colorState = R.drawable.gray_lens
            }
            4 -> {
                colorState = R.drawable.green_lens
            }
            5 -> {
                colorState = R.drawable.violet_lens
            }
            6 -> {
                colorState = R.drawable.brown_lens
            }
        }

        preview = Preview.Builder() // 미리보기
            .setTargetResolution(Size(width,height))
            .setTargetRotation(rotation)
            .build()

        imageCapture = ImageCapture.Builder() // image 캡처
            .setTargetResolution(Size(width,height))
            .setTargetRotation(rotation)
            .build()
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(width,height))
            .setTargetRotation(rotation)
            .build()
            .also {
                setGraphicOverlay(binding.graphicOverlay)
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer(binding.graphicOverlay,colorState){
                    runOnUiThread {
                        binding.viewFinderImageview.setImageBitmap(it)
                    }
                })
            }

        cameraProvider?.unbindAll()

        try{
            camera = cameraProvider?.bindToLifecycle(this,cameraSelector,preview,imageCapture,imageAnalysis)
            preview?.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
        } catch (e : Exception){
            Log.d("Exception", "Exception $e")
        }
    }
    fun setGraphicOverlay(overlay : GraphicOverlay?){
        var size = Size(width,height)
        val min = Math.min(size.width, size.height)
        val max = Math.max(size.width, size.height)
        //Front Camera : 1 Back Camera : 2
        overlay?.setCameraInfo(min,max,1)
        overlay?.clear()
    }
    inner class LuminosityAnalyzer(var graphicOverlay: GraphicOverlay?,var color:Int? , var listener : (bitmap : Bitmap) -> Unit?) : ImageAnalysis.Analyzer{
        var options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST) // 얼굴을 인식할 때 속도 (또는 정확도를) 우선시합니다.
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS) //눈, 귀, 코, 콧볼, 입과 같은 얼굴의 랜드마크를 파악할 것인지 여부입니다.
            .build()

//        var options1 = FaceDetectorOptions.Builder()
//            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
//            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//            .build()

        override fun analyze(image: ImageProxy) {

            var originBitmap = image.image?.toBitmap(100)
            var bitmapToFloating = originBitmap?.rotateWithReverse(270f)

            var metadata = FirebaseVisionImageMetadata.Builder() //FirebaseVision 감지기에서 사용하는 이미지 메타데이터 입니다.
                .setWidth(image.width)
                .setHeight(image.height)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(FirebaseVisionImageMetadata.ROTATION_270)
                .build()

            var buffer = image.planes[0].buffer
            var bufferImage = FirebaseVisionImage.fromByteBuffer(buffer,metadata)

            FirebaseVision.getInstance() // 얼굴 감지 분석
                .getVisionFaceDetector(options)
                .detectInImage(bufferImage)
                .addOnSuccessListener { faces -> // 이미지 분석 성공 시
                    graphicOverlay?.clear()
                    for(face in faces){
                        var faceGraphic = FaceGraphic(graphicOverlay,face,BitmapFactory.decodeResource(resources, color!!))
                        graphicOverlay?.add(faceGraphic)
                    }
                    graphicOverlay?.postInvalidate() // 화면 갱신
                    image.close() // 이미지 뷰와 싱크를 맞추기 위하여 close
                    bitmapToFloating?.let { listener(it) }

                } // 이미지 분석 실패 시
                .addOnFailureListener {
                    image.close()
                    bitmapToFloating?.let { listener(it) }
                }
        }

    }
}