package com.ibrahimgok.stt


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_urunler.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class Urunler : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id_uyari_01"
    private val notificationId = 101
    private val notificationId2 = 102
    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urunler)

        val intent = intent
        val veri = (intent.getStringExtra("qrcodeverisi"))
        val liste = veri?.split(" ")
        val urun = liste?.get(0)
        var year = liste?.get(3)
        if (year == null) { year = "0000"}
        var month = liste?.get(2)
        if (month == null) { month = "01"}
        var day = liste?.get(1)
        if (day == null) { day = "01"} 


        val current_date = LocalDate.now()
        val STT = "${year}-${month}-${day}"
        val stt = LocalDate.parse(STT, DateTimeFormatter.ISO_DATE)
        val stturun = current_date.until(stt, ChronoUnit.DAYS).toInt()

        val veritabani = this.openOrCreateDatabase("Urunler", MODE_PRIVATE,null)
        veritabani.execSQL("CREATE TABLE IF NOT EXISTS urunler (id INTEGER PRIMARY KEY, urun VARCHAR, stturun VARCHAR)")

        if (year != "0000") { veritabani.execSQL("INSERT INTO urunler (urun, stturun) VALUES ('${urun}','${stturun}')")}

        val cursor = veritabani.rawQuery("SELECT * FROM urunler",null)

        val idcolumnIndex = cursor.getColumnIndex("id")
        val uruncolumnIndex = cursor.getColumnIndex("urun")
        val stturuncolumnIndex = cursor.getColumnIndex("stturun")


        val urunler = ArrayList<String>()
        while (cursor.moveToNext()) {
            urunler.add("${cursor.getInt(idcolumnIndex)}. ${cursor.getString(uruncolumnIndex)} ->  ${cursor.getInt(stturuncolumnIndex)} gün kaldı")
        }
        cursor.close()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = RecyclerAdapter(urunler)
        recyclerView.adapter = adapter

        button4.setOnClickListener (  View.OnClickListener { View ->
            val listeyisiluyarimesaji = AlertDialog.Builder(this)
            listeyisiluyarimesaji.setTitle("Emin misiniz?")
            listeyisiluyarimesaji.setMessage("Listenizi silmek üzeresiniz emin misiniz?")
            listeyisiluyarimesaji.setPositiveButton("Evet", DialogInterface.OnClickListener { dialogInterface, i ->
                veritabani.execSQL("DELETE FROM urunler")
                urunler.clear()
                while (cursor.moveToNext()) {
                    urunler.add("${cursor.getInt(idcolumnIndex)}. ${cursor.getString(uruncolumnIndex)} ->  ${cursor.getInt(stturuncolumnIndex)} gün kaldı")
                }
                cursor.close()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                val adapter = RecyclerAdapter(urunler)
                recyclerView.adapter = adapter

            })
            listeyisiluyarimesaji.setNegativeButton("Hayır", DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(this, "İşlem iptal edildi!", Toast.LENGTH_SHORT).show()
            })
            listeyisiluyarimesaji.show()
        })


        createNotificationChannel()
        if (stturun == 3) {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.applogo)
                    .setContentTitle("Hemen tüketin!")
                    .setContentText("${urun} adlı ürünün bozulmasına 3 gün kaldı!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }
        if (stturun == 7) {
            val builder = NotificationCompat.Builder(this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.applogo)
                    .setContentTitle("Tüketin!")
                    .setContentText("${urun} adlı ürünün bozulmasına bir hafta kaldı!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId2,builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description= descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun button3(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}












