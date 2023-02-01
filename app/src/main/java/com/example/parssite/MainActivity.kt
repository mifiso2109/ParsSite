package com.example.parssite

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    lateinit var Get: Button
    lateinit var LV: ListView
    lateinit var Res: TextView
    var titleList = ArrayList<String>()
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Res = findViewById(R.id.textView)
       // LV = findViewById(R.id.lv)
        Get = findViewById(R.id.btnParseHTML)
        val save = findViewById<Button>(R.id.onSave)
        val clear = findViewById<Button>(R.id.onClear)

        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()
        val res1 = pref.getString("res", "Result")
        Res.text = res1
        //titleList.add(res1.toString())
        title = "KotlinApp"

GlobalScope.launch {
    run("https://api.github.com/users/Evin1-/repos")
}

        Get.setOnClickListener {
            GlobalScope.launch {
                getHtmlFromWeb()
            }
        }

        save.setOnClickListener {
            editor.putString("res", Res.text.toString())
            editor.apply()
        }

        clear.setOnClickListener {
            editor.clear()
            editor.apply()
            Res.text = ""
        }

    }

    private fun getHtmlFromWeb(){
        Thread(Runnable {
            val stringBuilder = StringBuilder()
            try{
                val doc: Document = Jsoup.connect("https://en.wikipedia.org/wiki/The_Cabinet_of_Dr._Caligari").get()
                val title: String = doc.title()
                val links: Elements = doc.select(".infobox tr")
               // titleList.clear()
                stringBuilder.append(title).append("\n")
                //val s = links.select()
                for (link in links) {
                    //titleList.add(link.text())
                    stringBuilder
                              .append("\n").append(link.getElementsByTag("th").text()).append(": ")
                              .append(link.getElementsByTag("td").text())
                              //.append("\n").append("Хэш2 :").append(link.getElementsByTag("th").text())
                              //.append("\n").append("Символы : ").append(link.text())
            }
        }
            catch (e: IOException) {
                stringBuilder.append("Error : ").append(e.message).append("\n")
            }
            runOnUiThread { Res.text = stringBuilder.toString() }
        }).start()

    }

    fun run(url:String) {
        val req = Request.Builder().url(url).build()
        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) =
                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + response.body()?.string())
        })
    }
}
