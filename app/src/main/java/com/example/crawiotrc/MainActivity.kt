package com.example.crawiotrc

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    var goalDegree: Int = 85
    var moveSpeed: Int = 90
    var uri = "http://crawiot.lan"

    override fun onStart() {
        super.onStart()
        //Проверяет подключение
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        Настройки скорости в процентах
        numberPicker.maxValue = 10
        numberPicker.minValue = 1
        val formatter = NumberPicker.Formatter { value ->
            val temp = value * 10
            "" + temp
        }
        numberPicker.setFormatter(formatter)

//        Настройка угла поворота
        rotateDegree.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Write code to perform some action when progress is changed.
                when (progress) {

                    0 -> {goalDegree = 60; showDegree.setText("-30")
                    }
                    1 -> {
                        goalDegree = 70; showDegree.setText("-20")
                    }
                    2 -> {
                        goalDegree = 80; showDegree.setText("-10")
                    }
                    3 -> {
                        goalDegree = 90; showDegree.setText("0")
                    }
                    4 -> {
                        goalDegree = 100; showDegree.setText("10")
                    }
                    5 -> {
                        goalDegree = 110; showDegree.setText("20")
                    }
                    6 -> {
                        goalDegree = 120; showDegree.setText("30")
                    }
                }
//                Log.d("tap", "ROTATE"+goalDegree.toString())
                Thread {
                    sendPostRequest(goalDegree, MotionType.Rotate)
                }.start()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                goToLeft.isEnabled = false
                goToRight.isEnabled = false
                moveButton.isEnabled = false
                backButton.isEnabled = false

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is stopped.
                goToLeft.isEnabled = true
                goToRight.isEnabled = true
                moveButton.isEnabled = true
                backButton.isEnabled = true

            }
        })

//        Старт движения
        moveButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {

                        when (numberPicker.value.toString()) {
                            "1" -> moveSpeed = 110
                            "2" -> moveSpeed = 116
                            "3" -> moveSpeed = 122
                            "4" -> moveSpeed = 128
                            "5" -> moveSpeed = 134
                            "6" -> moveSpeed = 140
                            "7" -> moveSpeed = 146
                            "8" -> moveSpeed = 154
                            "9" -> moveSpeed = 160
                            "10" -> moveSpeed = 170
                        }
//                        Log.d("tap", moveSpeed.toString())
                        Thread {
                            sendPostRequest(moveSpeed, MotionType.Move)
                        }.start()

                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        moveButton.setOnClickListener {
            moveSpeed = 90
            Log.d("tap", "STOP")
//            Thread {
//                sendPostRequest(moveSpeed, MotionType.Move)
//            }.start()
        }
//Кнопка назад
        backButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
//                        Log.d("tap", "MOVE BACK")
                        Thread {
                            sendPostRequest(moveSpeed, MotionType.Move)
                        }.start()

                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
        backButton.setOnClickListener {
            Thread {
                sendPostRequest(70, MotionType.Move)
            }.start()
//            Log.d("tap", "STOP")
        }


    }

    enum class MotionType {
        Move,
        Rotate
    }

    fun sendPostRequest(units: Int, type: MotionType) : Boolean {
        var suffix = if(type == MotionType.Move) "move" else "rotate";
        val mURL = URL("http://crawiot.lan/api/motion/$suffix?units=$units")

        with(mURL.openConnection() as HttpURLConnection) {
            try {
                requestMethod = "POST"
                return responseCode == 200;
            }catch (e: Exception) {
                Log.d("Inet", e.toString())
                return false;
            }
        }
    }

}