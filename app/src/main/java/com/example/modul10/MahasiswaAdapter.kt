package com.example.modul10


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.modul10.api.ApiConfig.apiService
import com.example.modul10.model.Mahasiswa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaAdapter(
    private val context: Context,
    private var mahasiswaList: MutableList<Mahasiswa>
) : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    inner class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vNama: TextView = itemView.findViewById(R.id.itm_nama)
        val vNRP: TextView = itemView.findViewById(R.id.itm_nrp)
        val btnDel: ImageButton = itemView.findViewById(R.id.itm_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        val currentMHS = mahasiswaList[position]
        holder.vNama.text = currentMHS.nama
        holder.vNRP.text = currentMHS.nrp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ViewMahasiswa::class.java).apply {
                putExtra("id", currentMHS.id)
                putExtra("nama", currentMHS.nama)
                putExtra("nrp", currentMHS.nrp)
                putExtra("email", currentMHS.email)
                putExtra("jurusan", currentMHS.jurusan)
            }
            if (context is Activity) {
                context.startActivityForResult(intent, MainActivity.REQUEST_CODE_UPDATE)
            }
        }

        holder.btnDel.setOnClickListener {
            Log.d("MahasiswaAdapter", "Delete button clicked for ID: ${currentMHS.id}")
            currentMHS.id?.let { id ->
                deleteMahasiswaById(id, position)
            } ?: run {
                Toast.makeText(context, "ID Mahasiswa tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteMahasiswaById(id: String, position: Int) {
        Log.d("MahasiswaAdapter", "Attempting to delete mahasiswa with ID: $id")
        val client = apiService.deleteMahasiswa(id)
        client.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("MahasiswaAdapter", "Mahasiswa deleted successfully: $id")
                    mahasiswaList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Mahasiswa berhasil dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MahasiswaAdapter", "onResponse failed: $errorBody")
                    Toast.makeText(context, "Gagal menghapus mahasiswa: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MahasiswaAdapter", "onFailure: ${t.message}")
                Toast.makeText(context, "Gagal menghapus mahasiswa: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getItemCount(): Int {
        return mahasiswaList.size
    }
}
