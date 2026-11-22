package com.yidao.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yidao.app.ui.AppRoot
import com.yidao.app.ui.theme.YidaoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YidaoTheme {
                AppRoot()
            }
        }
    }
}
