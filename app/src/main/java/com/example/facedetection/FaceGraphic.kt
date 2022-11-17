package com.example.facedetection

import android.graphics.*
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark

class FaceGraphic  (overlay: GraphicOverlay?, var firebaseVisionFace : FirebaseVisionFace?, var lensBitmap : Bitmap) : GraphicOverlay.Graphic(overlay){
    val idPaint = Paint().apply {
        color = Color.WHITE
    }
    override fun draw(canvas: Canvas?) {
        var face = firebaseVisionFace ?: return

        drawBitmapOverLandmarkPosition(canvas, face,lensBitmap, FirebaseVisionFaceLandmark.RIGHT_EYE) // 오른쪽 눈
        drawBitmapOverLandmarkPosition(canvas ,face,lensBitmap, FirebaseVisionFaceLandmark.LEFT_EYE) // 왼쪽 눈
    }

    // 얼굴에 포인트를 찍어주는 함수
    fun drawBitmapOverLandmarkPosition(canvas: Canvas?, face: FirebaseVisionFace, overlayBitmap: Bitmap, landmarkID : Int){
        val landmark = face.getLandmark(landmarkID)
        val point = landmark?.position
        if (point != null){
            val imageEdgeSizeBasedOnFaceSize = face.boundingBox.height() / 9f
            val left = translateX(point.x) - imageEdgeSizeBasedOnFaceSize
            val top = translateY(point.y) - imageEdgeSizeBasedOnFaceSize
            val right = translateX(point.x) + imageEdgeSizeBasedOnFaceSize
            val bottom = translateY(point.y) + imageEdgeSizeBasedOnFaceSize
            canvas?.drawBitmap(overlayBitmap,null, Rect(left.toInt(),top.toInt(),right.toInt(),bottom.toInt()),null)
        }
    }
}