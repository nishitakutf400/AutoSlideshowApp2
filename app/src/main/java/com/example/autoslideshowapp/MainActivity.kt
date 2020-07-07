package com.example.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.media.Image
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100
    private var mTimer: Timer? = null
    private var mHandler = Handler()
    private var boolean_flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                getContentsInfo()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor!!.moveToFirst()) {
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                ImageView.setImageURI(imageUri)
        }else if(cursor.moveToLast()){
            cursor.moveToFirst()
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            mHandler.post{ImageView.setImageURI(imageUri)}


        }
        Button1.setOnClickListener{
            if(cursor.isLast()){
                cursor.moveToFirst()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                ImageView.setImageURI(imageUri)
            }else{
                cursor.moveToNext()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                ImageView.setImageURI(imageUri)
            }

        }

        Button2.setOnClickListener{
           if(cursor.isFirst()){
               cursor.moveToLast()
               val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
               val id = cursor.getLong(fieldIndex)
               val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
               ImageView.setImageURI(imageUri)
           }else{
               cursor.moveToPrevious()
               val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
               val id = cursor.getLong(fieldIndex)
               val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
               ImageView.setImageURI(imageUri)
           }
        }
        Button3.setOnClickListener{
            if(boolean_flag == true) {
                Button3.text = "停止"
                boolean_flag = false
                Button1.isEnabled = false
                Button2.isEnabled = false
                if (mTimer == null) {
                    mTimer = Timer()
                    mTimer!!.schedule(object : TimerTask() {
                        override fun run() {
                            if (cursor.isLast) {
                                cursor.moveToFirst()
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri = ContentUris.withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    id
                                )
                                mHandler.post { ImageView.setImageURI(imageUri) }
                            } else {
                                cursor.moveToNext()
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri = ContentUris.withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    id
                                )
                                mHandler.post { ImageView.setImageURI(imageUri) }
                            }
                        }
                    }, 2000, 2000)
                }
            }else if(boolean_flag == false){
                Button3.text="再生"
                boolean_flag = true
                Button1.isEnabled = true
                Button2.isEnabled = true
                mTimer!!.cancel()
                mTimer = null
            }
        }

    }



}