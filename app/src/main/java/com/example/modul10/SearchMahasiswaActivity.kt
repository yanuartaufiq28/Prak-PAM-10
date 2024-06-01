package com.example.modul10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.modul10.api.ApiConfig.apiService
import com.example.modul10.model.Mahasiswa
import com.example.modul10.model.MahasiswaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMahasiswaActivity : AppCompatActivity() {
    private lateinit var edtChecNrp: EditText
    private lateinit var btnSearch: Button
    private var progressBar: ProgressBar? = null
    private var tvNrp: TextView? = null
    private var tvNama: TextView? = null
    private var tvEmail: TextView? = null
    private var tvJurusan: TextView? = null
    private var mahasiswaList: List<Mahasiswa>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_mahasiswa)
        edtChecNrp = findViewById(R.id.edtChckNrp)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        tvNrp = findViewById(R.id.tvValNrp)
        tvNama = findViewById(R.id.tvValNama)
        tvEmail = findViewById(R.id.tvValEmail)
        tvJurusan = findViewById(R.id.tvValJurusan)
        mahasiswaList = ArrayList()

        btnSearch.setOnClickListener {
            showLoading(true)
            val nrp = edtChecNrp.text.toString()
            if (nrp.isEmpty()) {
                edtChecNrp.error = "Silahakan Isi nrp terlebih dahulu"
                showLoading(false)
            } else {
                val client = apiService.getMahasiswa(nrp)
                client.enqueue(object : Callback<MahasiswaResponse> {
                    override fun onResponse(call: Call<MahasiswaResponse>, response: Response<MahasiswaResponse>) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                mahasiswaList = it.data
                                setData(mahasiswaList)
                            }
                        } else {
                            Log.e("Error", "onResponse: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<MahasiswaResponse>, t: Throwable) {
                        showLoading(false)
                        Log.e("Error Retrofit", "onFailure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun setData(mahasiswaList: List<Mahasiswa>?) {
        mahasiswaList?.get(0)?.let {
            tvNrp?.text = it.nrp
            tvNama?.text = it.nama
            tvEmail?.text = it.email
            tvJurusan?.text = it.jurusan
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}