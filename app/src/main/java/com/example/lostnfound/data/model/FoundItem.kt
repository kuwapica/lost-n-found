// In your entity/FoundItemEntity.kt file
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "found_items")
data class FoundItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaBarang: String,
    val lokasiFound: String,
    val waktu: String,
    val deskripsi: String,
    val imagePath: String?,
    val status: String,
    val timestamp: Long = System.currentTimeMillis() // <-- TAMBAHKAN BARIS INI
)