package com.example.lostnfound.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostnfound.R
import com.example.lostnfound.entity.FoundItemEntity
import com.example.lostnfound.entity.LostItemEntity
import java.util.concurrent.TimeUnit

class FoundItemAdapter(
    private var itemList: List<FoundItemEntity>,
    private val onItemClick: (FoundItemEntity) -> Unit
    ) : RecyclerView.Adapter<FoundItemAdapter.FoundViewHolder>() {

    inner class FoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFotoBarang: ImageView = itemView.findViewById(R.id.iv_foto_barang)
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tv_nama_barang)
        val tvLokasiInfo: TextView = itemView.findViewById(R.id.tv_lokasi_info)
        val tvWaktuLalu: TextView = itemView.findViewById(R.id.tv_waktu_lalu)

        init {
            itemView.setOnClickListener {
                onItemClick(itemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_card, parent, false) // Menggunakan layout card kamu
        return FoundViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FoundViewHolder, position: Int) {
        val item = itemList[position]

        // Mengisi data ke dalam view
        holder.tvNamaBarang.text = item.namaBarang
        holder.tvLokasiInfo.text = "Ditemukan di: ${item.lokasiFound}"
        holder.tvWaktuLalu.text = "Ditemukan ${formatTimeAgo(item.timestamp)}"

        // Menggunakan Glide untuk memuat gambar dari Uri
        item.imagePath?.let {
            val imageUri = Uri.parse(it)
            Glide.with(holder.itemView.context)
                .load(imageUri)
//                .placeholder(R.drawable.ic_placeholder) // Opsional: gambar placeholder
//                .error(R.drawable.ic_error) // Opsional: gambar jika error
                .into(holder.ivFotoBarang)
        }
    }

    // Fungsi untuk memperbarui data di adapter
    fun updateData(newList: List<FoundItemEntity>) {
        this.itemList = newList
        notifyDataSetChanged() // Memberi tahu RecyclerView untuk refresh
    }

    private fun formatTimeAgo(timeInMillis: Long): String {
        val diff = System.currentTimeMillis() - timeInMillis
        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "baru saja"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} menit yang lalu"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} jam yang lalu"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff)} hari yang lalu"
        }
    }
}