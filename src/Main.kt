sealed class StatusPengiriman {
    object MenungguDiproses : StatusPengiriman()

    data class DalamPerjalanan(
        val namaKurir: String
    ) : StatusPengiriman()
    data class Terkirim(
        val namaPenerima: String,
        val waktuSelesai: String
    ) : StatusPengiriman()
    data class Gagal(
        val alasan: String
    ) : StatusPengiriman()
}

open class Paket (
    val nomorResi: String,
    val pengirim: String,
    val penerima: String,
    val beratKg: Double
){
    var status: StatusPengiriman = StatusPengiriman.MenungguDiproses
        private set

    open fun hitungBiaya(): Double {
        return beratKg * 10000.0
    }
    fun updateStatus(statusBaru: StatusPengiriman) {
        if (this.status != statusBaru) {
            this.status = statusBaru
            println("Status berhasil diupdate")
        } else {
            println("Status sudah yang terbaru")
        }
    }

    open fun tampilkanInfo() {
        println("==== INFO ====")
        println("NomorResi: $nomorResi")
        println("Pengiriman: $pengirim")
        println("Penerima: $penerima")
        println("Status: $status")
        println("Biaya: ${hitungBiaya()}")
        println("===============")
    }
}

class PaketReguler (
    nomorResi: String,
    pengirim: String,
    penerima: String,
    beratKg: Double
) : Paket(nomorResi, pengirim, penerima, beratKg) {
}

class PaketEkspres (
    nomorResi: String,
    pengirim: String,
    penerima: String,
    beratKg: Double,
    val biayaAsuransi: Double
) : Paket(nomorResi, pengirim, penerima, beratKg) {

    override fun hitungBiaya(): Double {
        return beratKg * 15000.0 + biayaAsuransi
    }
}

class PaketFragile (
    nomorResi: String,
    pengirim: String,
    penerima: String,
    beratKg: Double,
    val biayaPackingKayu: Double
) : Paket(nomorResi, pengirim, penerima, beratKg) {

    override fun hitungBiaya(): Double {
        return beratKg * 12000.0 + biayaPackingKayu
    }
}

class EkspedisiManager {
    private val daftarPaket: MutableList<Paket> = mutableListOf()

    fun terimaPaket(paket: Paket) {
        daftarPaket.add(paket)
    }

    fun lacakPaket(resi: String) {
        val paket = daftarPaket.find {it.nomorResi == resi}

        if (paket != null) {
            println("Pelacakan Paket $resi")

            when (paket){
                is PaketReguler -> {
                    println("Paket $resi adalah paket reguler")
                    println("\n")
                }
                is PaketEkspres -> {
                    println("Paket $resi adalah paket ekspres")
                    println("\n")
                }
                is PaketFragile -> {
                    println("Paket $resi adalah paket fragile")
                    println("\n")
                }
            }
        } else {
            println("Paket tidak ditemukan $")
        }
    }

    fun tampilkanSemuaPaket() {
        println("=== Daftar Paket ===")
        if (daftarPaket.isEmpty()) {
            println("Belum ada paket")
        } else {
            for (paket in daftarPaket) {
                paket.tampilkanInfo()
                println("\n")
            }
        }

    }

    fun hitungTotalPendapatan(): Double {
        return daftarPaket.sumOf { it.hitungBiaya() }
    }
}

fun main() {
    val manager = EkspedisiManager()

    val paket1 = PaketReguler(
        nomorResi = "REGULER0001",
        pengirim = "Agus",
        penerima = "Adam",
        beratKg = 2.0
    )
    val paket2 = PaketReguler(
        nomorResi = "REGULER0002",
        pengirim = "Zidane",
        penerima = "Ivan",
        beratKg = 4.0
    )
    val paket3 = PaketEkspres(
        nomorResi = "EKSPRES0001",
        pengirim = "Kurniawan",
        penerima = "Asep",
        beratKg = 1.0,
        biayaAsuransi = 12000.0
    )
    val paket4 = PaketFragile(
        nomorResi = "FRAGILE0002",
        pengirim = "Salam",
        penerima = "Dani",
        beratKg = 2.0,
        biayaPackingKayu = 30000.0
    )

    // Menerima paket
    manager.terimaPaket(paket1)
    manager.terimaPaket(paket2)
    manager.terimaPaket(paket3)
    manager.terimaPaket(paket4)

    // Mengubah status paket
    paket1.updateStatus(StatusPengiriman.DalamPerjalanan("Agus"))
    println("Paket 1 Sedang dalam perjalanan")

    paket2.updateStatus(StatusPengiriman.Gagal("Paket Hilang"))
    println("Paket 2 Gagal")

    paket3.updateStatus(StatusPengiriman.Terkirim("Asep", "12:44"))
    println("Paket 3 Sudah Terkirim")

    // Tampilkan semua paket
    println("\n")
    manager.tampilkanSemuaPaket()

    // Lacak paket
    manager.lacakPaket("FRAGILE0002")
    manager.lacakPaket("EKSPRES0001")
    manager.lacakPaket("REGULER0001")
    manager.lacakPaket("REGULER0002")

    // Hitung total pendapatan
    val totalPendapatan = manager.hitungTotalPendapatan()
    println("Total Pendapatan: Rp. $totalPendapatan")

}

