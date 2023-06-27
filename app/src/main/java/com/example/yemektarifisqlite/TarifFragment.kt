package com.example.yemektarifisqlite

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_tarif.*


@Suppress("DEPRECATION")
class TarifFragment : Fragment() {

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tarif, container, false)
    }
    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated (view, savedInstanceState)
        button.setOnClickListener {
            kaydet (it)
        }
        imageView.setOnClickListener {
            gorselSec (it)
        }
    }
    //SQLite ye kaydetme
    fun kaydet (view: View) {


    }
    //kullanıcı izinleriyle görsel seçme
    fun gorselSec(view: View) {
        val izinler = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, izinler[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(izinler, 1)
            } else {
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            //geriye birşey döndü mü ve izin verildiyse
            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //galeriye gitme
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            secilenGorsel = data.data

            try {
                context?.let {
                    if (secilenGorsel != null){
                        if (Build.VERSION.SDK_INT >= 28){
                             val source = ImageDecoder.createSource(it.contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            imageView.setImageBitmap(secilenBitmap)
                        }else {
                            secilenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver, secilenGorsel)
                            imageView.setImageBitmap(secilenBitmap)
                        }
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}



