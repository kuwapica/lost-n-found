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
import com.example.lostnfound.entity.LostItemEntity
import java.util.concurrent.TimeUnit

class LostItemAdapter(
    private var itemList: List<LostItemEntity>,
    private val onItemClick: (LostItemEntity) -> Unit
    ) : RecyclerView.Adapter<LostItemAdapter.LostViewHolder>() {

    inner class LostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_card, parent, false)
        return LostViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: LostViewHolder, position: Int) {
        val item = itemList[position]

        holder.tvNamaBarang.text = item.namaBarang
        // Ganti teks sesuai konteks "Barang Hilang"
        holder.tvLokasiInfo.text = "Lokasi Terakhir: ${item.lokasiLost}"
        holder.tvWaktuLalu.text = "Hilang ${formatTimeAgo(item.timestamp)}"

        item.imagePath?.let {
            val imageUri = Uri.parse(it)
            Glide.with(holder.itemView.context)
                .load(imageUri)
                .into(holder.ivFotoBarang)
        }
    }

    fun updateData(newList: List<LostItemEntity>) { // <-- Ganti tipe data
        this.itemList = newList
        notifyDataSetChanged()
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