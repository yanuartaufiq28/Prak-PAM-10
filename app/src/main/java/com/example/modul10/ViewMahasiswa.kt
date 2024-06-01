package com.example.modul10

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modul10.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewMahasiswa : AppCompatActivity() {
    private var mhsId: String? = null
    private lateinit var viewNRP: TextView
    private lateinit var viewNama: TextView
    private lateinit var viewEmail: TextView
    private lateinit var viewJurusan: TextView
    private lateinit var btn_update: Button
    private lateinit var btn_back: Button
    private lateinit var uNRP: EditText
    private lateinit var uNama: EditText
    private lateinit var uEmail: EditText
    private lateinit var uJurusan: EditText
    private lateinit var btUpdate: Button
    private lateinit var popup: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_mahasiswa)
        val bundle: Bundle? = intent.extras

        viewNRP = findViewById(R.id.view_NRP)
        viewNama = findViewById(R.id.viewNama)
        viewEmail = findViewById(R.id.viewEmail)
        viewJurusan = findViewById(R.id.viewJurusan)
        btn_update = findViewById(R.id.btn_update)
        btn_back = findViewById(R.id.btn_back)
        uNRP = findViewById(R.id.uNRP)
        uNama = findViewById(R.id.uNama)
        uEmail = findViewById(R.id.uEmail)
        uJurusan = findViewById(R.id.uJurusan)
        btUpdate = findViewById(R.id.update)
        popup = findViewById(R.id.popup)

        val vNRP = bundle?.getString("nrp")
        val vNama = bundle?.getString("nama")
        val vEmail = bundle?.getString("email")
        val vJurusan = bundle?.getString("jurusan")
        mhsId = bundle?.getString("id")

        viewNRP.text = vNRP
        viewNama.text = vNama
        viewEmail.text = vEmail
        viewJurusan.text = vJurusan

        btn_back.setOnClickListener {
            finish()
        }

        btn_update.setOnClickListener {
            popup.visibility = View.VISIBLE

            // Set nilai EditText untuk mengedit data
            uNRP.setText(viewNRP.text)
            uNama.setText(viewNama.text)
            uEmail.setText(viewEmail.text)
            uJurusan.setText(viewJurusan.text)
        }

        btUpdate.setOnClickListener {
            val nrp = uNRP.text.toString()
            val nama = uNama.text.toString()
            val email = uEmail.text.toString()
            val jurusan = uJurusan.text.toString()

            if (mhsId != null) {
                updateMahasiswa(mhsId!!, nrp, nama, email, jurusan)
            }
        }
    }

    private fun updateMahasiswa(id: String, nrp: String, nama: String, email: String, jurusan: String) {
        val client = ApiConfig.apiService.updateMahasiswa(id, nrp, nama, email, jurusan)
        client.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    popup.visibility = View.GONE
                    viewNRP.text = nrp
                    viewNama.text = nama
                    viewEmail.text = email
                    viewJurusan.text = jurusan

//                    intent.putExtra("id", id)
//                    intent.putExtra("nrp", nrp)
//                    intent.putExtra("nama", nama)
//                    intent.putExtra("email", email)
//                    intent.putExtra("jurusan", jurusan)
//                    setResult(Activity.RESULT_OK, intent)
                    val resultIntent = Intent().apply {
                        putExtra("id", id)
                        putExtra("nrp", nrp)
                        putExtra("nama", nama)
                        putExtra("email", email)
                        putExtra("jurusan", jurusan)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                } else {
                    Toast.makeText(this@ViewMahasiswa, "Gagal mengupdate mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ViewMahasiswa", "onFailure: ${t.message}")
                Toast.makeText(this@ViewMahasiswa, "Gagal mengupdate mahasiswa: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
