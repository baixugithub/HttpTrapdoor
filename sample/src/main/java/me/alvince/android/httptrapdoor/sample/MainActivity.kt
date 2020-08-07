package me.alvince.android.httptrapdoor.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.alvince.android.httptrapdoor.Trapdoor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit

    private lateinit var trapdoor: Trapdoor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trapdoor = Trapdoor.with(OkHttpClient.Builder().build())
            .apply {
                enableHttpLog()
            }
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.github.com")
            .callFactory(trapdoor.factory())
            .build()
    }

    override fun onResume() {
        super.onResume()
        listElements()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_pop_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reload -> {
                listElements()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @UiThread
    private fun listElements() {
        tv_showPanel.apply {
            StringBuilder().apply {
                trapdoor.elements().forEach {
                    append(it.toString())
                    appendln()
                }
            }.also {
                text = it
            }
        }
    }

}
