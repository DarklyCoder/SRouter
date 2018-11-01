package com.darklycoder.srouter.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.darklycoder.srouter.SRouter
import com.darklycoder.srouter.annotation.Route
import com.darklycoder.srouter.data.ActionInfo
import kotlinx.android.synthetic.main.activity_main.*

@Route("app/main")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_inner.setOnClickListener {
            SRouter.instance.run(ActionInfo.Builder().newActivity(this, "app/test"))
        }

        btn_outer.setOnClickListener {
            SRouter.instance.run(ActionInfo.Builder().newActivity(this, "test/main"))
        }
    }

}
