package com.example.facedetection

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.facedetection.others.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_lens_settings.*

class LensSettings : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
//    var sharedPreferences: SharedPreferences = this.getSharedPreferences("pref", MODE_PRIVATE)

    private var colorState : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lens_settings)

        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE)
        redCircle.setOnClickListener {
            colorState = 1
            selectColorCircle()
        }
        blueCircle.setOnClickListener {
            colorState = 2
            selectColorCircle()
        }
        grayCircle.setOnClickListener {
            colorState = 3
            selectColorCircle()
        }
        greenCircle.setOnClickListener {
            colorState = 4
            selectColorCircle()
        }
        violetCircle.setOnClickListener {
            colorState = 5
            selectColorCircle()
        }
        brownCircle.setOnClickListener {
            colorState = 6
            selectColorCircle()
        }

        btnColorChanges.setOnClickListener {
            val layout = findViewById<View>(R.id.Settings)
            val success = changingColor()
            if(success){
                Snackbar.make(layout,"Lens color changed.", Snackbar.LENGTH_SHORT).show()
            }
            else{
                Snackbar.make(layout,"Please choose again.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectColorCircle(){
        when (colorState) {
            1 -> {
                redCheck.visibility = View.VISIBLE
                blueCheck.visibility = View.GONE
                grayCheck.visibility = View.GONE
                greenCheck.visibility = View.GONE
                violetCheck.visibility = View.GONE
                brownCheck.visibility = View.GONE
            }
            2 -> {
                redCheck.visibility = View.GONE
                blueCheck.visibility = View.VISIBLE
                grayCheck.visibility = View.GONE
                greenCheck.visibility = View.GONE
                violetCheck.visibility = View.GONE
                brownCheck.visibility = View.GONE
            }
            3 -> {
                redCheck.visibility = View.GONE
                blueCheck.visibility = View.GONE
                grayCheck.visibility = View.VISIBLE
                greenCheck.visibility = View.GONE
                violetCheck.visibility = View.GONE
                brownCheck.visibility = View.GONE
            }
            4 -> {
                redCheck.visibility = View.GONE
                blueCheck.visibility = View.GONE
                grayCheck.visibility = View.GONE
                greenCheck.visibility = View.VISIBLE
                violetCheck.visibility = View.GONE
                brownCheck.visibility = View.GONE
            }
            5 -> {
                redCheck.visibility = View.GONE
                blueCheck.visibility = View.GONE
                grayCheck.visibility = View.GONE
                greenCheck.visibility = View.GONE
                violetCheck.visibility = View.VISIBLE
                brownCheck.visibility = View.GONE
            }
            6 -> {
                redCheck.visibility = View.GONE
                blueCheck.visibility = View.GONE
                grayCheck.visibility = View.GONE
                greenCheck.visibility = View.GONE
                violetCheck.visibility = View.GONE
                brownCheck.visibility = View.VISIBLE
            }
        }
    }

    private fun changingColor() : Boolean {
        sharedPreferences.edit()
            .putInt(Constants.KEY_COLOR, colorState)
            .apply()
        return true
    }

}