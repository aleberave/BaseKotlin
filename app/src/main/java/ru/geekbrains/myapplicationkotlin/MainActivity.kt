package ru.geekbrains.myapplicationkotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data1 = DataClass("First", Calendar.getInstance().time)
        val data2 = data1.copy()
        val data3 = data2.copy()
        val data4 = data3.copy()

        data2.title = "Second"
        data3.title = "Third"
        data4.title = "Fourth"

        val list = listOf(data1, data2, data3, data4)


        val str1 = "$data1"
        val str2 = "$data2"

        findViewById<Button>(R.id.myButton).setOnClickListener(View.OnClickListener {
            ("$str1 \n $str2 \n ${list[2]} \n ${list[3]}").also {
                findViewById<TextView>(R.id.myTextview).text = it
            }

            for (some in list) {
                println(some)
            }

            list.forEach {
                println("foreach $it")
            }

            list.forEach { data: DataClass ->
                println("foreach data $data.title")
            }
            repeat(list.size) { index: Int ->
                println("foreach index ${list[index]}")
            }

        })
    }
}

