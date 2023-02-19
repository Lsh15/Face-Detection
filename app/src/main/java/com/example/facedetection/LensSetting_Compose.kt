package com.example.facedetection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.facedetection.ui.theme.FaceDetectionTheme

class LensSetting_Compose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceDetectionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    layout()
//                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun layout(){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        ,contentAlignment = Center)
    {
        Column(modifier = Modifier.fillMaxWidth()) {
//            Box() {
//
//            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly) {

                Image(painter = painterResource(id = R.drawable.pink_lens), contentDescription = "pink_lens")
                Image(painter = painterResource(id = R.drawable.blue_lens), contentDescription = "blue_lens")
                Image(painter = painterResource(id = R.drawable.gray_lens), contentDescription = "gray_lens")
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Image(painter = painterResource(id = R.drawable.ic_baseline_check_25), contentDescription = "check")
                Image(painter = painterResource(id = R.drawable.ic_baseline_check_25), contentDescription = "check")
                Image(painter = painterResource(id = R.drawable.ic_baseline_check_25), contentDescription = "check")

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Image(painter = painterResource(id = R.drawable.green_lens), contentDescription = "green_lens")
                Image(painter = painterResource(id = R.drawable.violet_lens), contentDescription = "violet_lens")
                Image(painter = painterResource(id = R.drawable.brown_lens), contentDescription = "brown_lens")
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Image(painter = painterResource(id = R.drawable.ic_baseline_check_25), contentDescription = "check")
                Image(painter = painterResource(id = R.drawable.ic_baseline_check_25), contentDescription = "check")
                Image(painter = painterResource(id = R.drawable.ic_baseline_check_25), contentDescription = "check")
            }
            Divider(thickness = 5.dp)
            Button(onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
            ) {
                Text(text = "색상 변경하기")
            }
        }    
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FaceDetectionTheme {
        layout()
//        Greeting("Android")
    }
}