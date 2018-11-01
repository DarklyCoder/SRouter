package com.darklycoder.modeltest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.darklycoder.srouter.annotation.Pack
import com.darklycoder.srouter.annotation.Route

@Pack("com.darklycoder.modeltest", table = "TestRouterTable")
@Route("test/main")
class ModelTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_test)
    }
}
