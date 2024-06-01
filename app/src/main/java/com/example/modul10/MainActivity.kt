package com.example.modul10

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.modul10.api.ApiConfig.apiService
import com.example.modul10.model.AddMahasiswaResponse
import com.example.modul10.model.Mahasiswa
import com.example.modul10.model.MahasiswaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var edtNrp: EditText
    private lateinit var edtNama: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtJurusan: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var rv1: RecyclerView
    private lateinit var mhsAdapter: MahasiswaAdapter
    private lateinit var btnAdd: Button
    private lateinit var btnListData: Button
    private var mhsList: MutableList<Mahasiswa> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtNrp = findViewById(R.id.edtNrp)
        edtNama = findViewById(R.id.edtNama)
        edtEmail = findViewById(R.id.edtEmail)
        edtJurusan = findViewById(R.id.edtJurusan)
        progressBar = findViewById(R.id.progressBar)
        rv1 = findViewById(R.id.rv)

        rv1.layoutManager = LinearLayoutManager(this)
        mhsAdapter = MahasiswaAdapter(this, mhsList)
        rv1.adapter = mhsAdapter

        btnAdd = findViewById(R.id.btnAdd)
        btnListData = findViewById(R.id.btnList)

        btnAdd.setOnClickListener {
            addDataMahasiswa()
        }

        btnListData.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchMahasiswaActivity::class.java)
            startActivity(intent)
        }

        getAllMahasiswa()
    }

    private fun addDataMahasiswa() {
        showLoading(true)
        val nrp = edtNrp.text.toString()
        val nama = edtNama.text.toString()
        val email = edtEmail.text.toString()
        val jurusan = edtJurusan.text.toString()

        if (nrp.isEmpty() || nama.isEmpty() || email.isEmpty() || jurusan.isEmpty()) {
            Toast.makeText(this, "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show()
            showLoading(false)
        } else {
            val client = apiService.addMahasiswa(nrp, nama, email, jurusan)
            client.enqueue(object : Callback<AddMahasiswaResponse> {
                override fun onResponse(call: Call<AddMahasiswaResponse>, response: Response<AddMahasiswaResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Berhasil menambahakan silahkan cek data pada halaman list!", Toast.LENGTH_SHORT).show()
                        // Reload the mahasiswa list to include the new item
                        getAllMahasiswa()
                    } else {
                        Log.e("MainActivity", "onResponse: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<AddMahasiswaResponse>, t: Throwable) {
                    showLoading(false)
                    Log.e("MainActivity", "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun getAllMahasiswa() {
        showLoading(true)
        val client = apiService.getAllMahasiswa()
        client.enqueue(object : Callback<MahasiswaResponse> {
            override fun onResponse(call: Call<MahasiswaResponse>, response: Response<MahasiswaResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        mhsList.clear()
                        mhsList.addAll(data)
                        mhsAdapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(this@MainActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MahasiswaResponse>, t: Throwable) {
                showLoading(false)
                Log.e("MainActivity", "onFailure: " + t.message)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val id = it.getStringExtra("id")
                val nrp = it.getStringExtra("nrp")
                val nama = it.getStringExtra("nama")
                val email = it.getStringExtra("email")
                val jurusan = it.getStringExtra("jurusan")

                // Temukan item yang diupdate dan perbarui
                val index = mhsList.indexOfFirst { mahasiswa -> mahasiswa.id == id }
                if (index != -1) {
                    mhsList[index].nrp = nrp
                    mhsList[index].nama = nama
                    mhsList[index].email = email
                    mhsList[index].jurusan = jurusan
                    mhsAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_UPDATE = 1
    }
}
