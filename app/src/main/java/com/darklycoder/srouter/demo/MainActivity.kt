package com.darklycoder.srouter.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.darklycoder.srouter.SRouter
import com.darklycoder.srouter.annotation.Route
import com.darklycoder.srouter.data.ActionInfo
import com.darklycoder.srouter.listener.ActionCallback
import kotlinx.android.synthetic.main.activity_main.*

@Route("app/main")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_inner.setOnClickListener {
            SRouter.instance.run(
                ActionInfo()
                    .newActivity(this, "app/test")
                    .newCallback(object : ActionCallback {
                        override fun onResult() {
                            finish()
                        }

                        override fun onFail(err: String?, e: Throwable?) {
                        }
                    })
            )
        }

        btn_outer.setOnClickListener {
            SRouter.instance.run(ActionInfo().newActivity(this, "test/main"))
        }
    }

}
