package com.darklycoder.srouter.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.darklycoder.srouter.annotation.Route

@Route("app/test")
class MainTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test)
    }
}
