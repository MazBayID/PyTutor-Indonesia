package com.pytutor.baru

sealed interface Blok
data class Teks(val isi: String) : Blok
data class Kode(val kode: String) : Blok

data class Soal(val tanya: String, val pilihan: List<String>, val jawaban: Int, val kode: String? = null)
data class Susun(val tanya: String, val urut: List<String>)
data class Pelajaran(val judul: String, val blok: List<Blok>)
data class Topik(
    val id: String,
    val judul: String,
    val deskripsi: String,
    val pelajaran: List<Pelajaran>,
    val kuis: List<Soal>,
    val susun: List<Susun> = emptyList(),
)
data class Latihan(val judul: String, val tingkat: String, val soal: String, val solusi: String)
data class Algoritma(val judul: String, val deskripsi: String, val kode: String)

object Konten {

    private val isiKosong = mapOf(
        "dasar" to listOf(Soal("Lengkapi kode agar tampil Halo!", listOf("print", "echo", "show", "say"), 0, "___(\"Halo!\")")),
        "variabel" to listOf(Soal("Isi bagian kosong agar hasilnya 8", listOf("+", "-", "*", "=="), 0, "a = 5\nb = 3\nprint(a ___ b)")),
        "kondisi" to listOf(Soal("Agar tampil \"Belum boleh\", operator yang tepat adalah…", listOf("<", ">", "==", "!="), 0, "umur = 15\nif umur ___ 17:\n    print(\"Belum boleh\")")),
        "loop" to listOf(Soal("Isi bagian kosong agar mencetak 0, 1, 2", listOf("range", "list", "loop", "count"), 0, "for i in ___(3):\n    print(i)")),
        "fungsi" to listOf(Soal("Kata kunci apa yang mengembalikan nilai?", listOf("return", "give", "yield", "output"), 0, "def kuadrat(x):\n    ___ x * x\n\nprint(kuadrat(4))")),
    )

    private val susun = mapOf(
        "dasar" to listOf(Susun("Susun kode agar menyapa pengguna", listOf("nama = input(\"Namamu? \")", "print(\"Halo,\", nama)"))),
        "variabel" to listOf(Susun("Susun kode untuk luas persegi panjang", listOf("panjang = 5", "lebar = 3", "print(panjang * lebar)"))),
        "kondisi" to listOf(Susun("Susun percabangan berikut", listOf("nilai = 90", "if nilai >= 80:", "    print(\"Lulus\")", "else:", "    print(\"Coba lagi\")"))),
        "loop" to listOf(Susun("Susun perulangan hitung mundur", listOf("n = 3", "while n > 0:", "    print(n)", "    n = n - 1"))),
        "fungsi" to listOf(Susun("Susun fungsi penjumlahan", listOf("def tambah(a, b):", "    return a + b", "print(tambah(2, 3))"))),
    )

    private val topikAsli = listOf(
        Topik(
            "dasar", "Halo, Python!", "print(), komentar, dan input()",
            listOf(
                Pelajaran("Menampilkan teks", listOf(
                    Teks("Python adalah bahasa pemrograman yang mudah dibaca. Untuk menampilkan sesuatu ke layar, gunakan print()."),
                    Kode("""
                        print("Halo, dunia!")
                        print("Saya belajar Python")
                    """.trimIndent()),
                    Teks("Setiap print() mencetak satu baris baru. Teks harus diapit tanda kutip ganda atau tunggal."),
                )),
                Pelajaran("Komentar dan input", listOf(
                    Teks("Komentar diawali tanda # dan diabaikan oleh Python. Gunakan untuk menjelaskan kode."),
                    Kode("""
                        # ini komentar
                        nama = input("Namamu? ")
                        print("Halo,", nama)
                    """.trimIndent()),
                    Teks("input() meminta pengguna mengetik sesuatu. Hasilnya selalu berupa teks."),
                )),
            ),
            listOf(
                Soal("Fungsi untuk menampilkan teks ke layar adalah…", listOf("echo()", "print()", "show()", "write()"), 1),
                Soal("Tanda apa yang memulai komentar di Python?", listOf("//", "#", "--", "/*"), 1),
                Soal("Apa tipe hasil dari input()?", listOf("Angka", "Teks (string)", "Boolean", "List"), 1),
            ),
        ),
        Topik(
            "variabel", "Variabel dan Tipe Data", "Menyimpan nilai dan mengubah tipenya",
            listOf(
                Pelajaran("Variabel", listOf(
                    Teks("Variabel adalah wadah bernama untuk menyimpan nilai. Di Python kamu tidak perlu menulis tipenya."),
                    Kode("""
                        umur = 17
                        nama = "Sari"
                        tinggi = 1.62
                        print(nama, umur, tinggi)
                    """.trimIndent()),
                    Teks("Nama variabel boleh berisi huruf, angka, dan _, tetapi tidak boleh diawali angka. Huruf besar dan kecil dibedakan."),
                )),
                Pelajaran("Tipe data dan konversi", listOf(
                    Teks("Tipe dasar: int (bilangan bulat), float (desimal), str (teks), dan bool (True/False). type() menunjukkan tipe, sedangkan int(), float(), str() mengubahnya."),
                    Kode("""
                        a = int("5")
                        b = 2.5
                        print(a + b)
                        print(type(a), type(b))
                        print("Umur: " + str(17))
                    """.trimIndent()),
                )),
            ),
            listOf(
                Soal("Mana nama variabel yang valid?", listOf("2nama", "nama_2", "nama-2", "nama 2"), 1),
                Soal("3.14 termasuk tipe data…", listOf("int", "float", "str", "bool"), 1),
                Soal("Hasil dari int(\"7\") + 3 adalah…", listOf("73", "10", "Error", "\"10\""), 1),
            ),
        ),
        Topik(
            "kondisi", "Percabangan", "if, elif, else, dan operator",
            listOf(
                Pelajaran("if, elif, else", listOf(
                    Teks("Percabangan menjalankan kode hanya jika syaratnya terpenuhi. Perhatikan tanda titik dua (:) dan indentasi (menjorok ke dalam)."),
                    Kode("""
                        nilai = 78
                        if nilai >= 80:
                            print("A")
                        elif nilai >= 70:
                            print("B")
                        else:
                            print("C")
                    """.trimIndent()),
                )),
                Pelajaran("Operator perbandingan dan logika", listOf(
                    Teks("Perbandingan: ==, !=, <, >, <=, >=. Logika: and, or, not."),
                    Kode("""
                        umur = 20
                        punya_ktp = True
                        if umur >= 17 and punya_ktp:
                            print("Boleh memilih")
                    """.trimIndent()),
                )),
            ),
            listOf(
                Soal("Operator untuk memeriksa dua nilai sama adalah…", listOf("=", "==", "!=", "=>"), 1),
                Soal("Kode di dalam if harus…", listOf("diberi kurung kurawal", "menjorok (indentasi)", "diakhiri titik koma", "ditulis huruf besar"), 1),
                Soal("Hasil dari True and False adalah…", listOf("True", "False", "Error", "None"), 1),
            ),
        ),
        Topik(
            "loop", "Perulangan", "for dan while",
            listOf(
                Pelajaran("for", listOf(
                    Teks("for mengulang kode untuk setiap item. range(n) menghasilkan angka 0 sampai n-1."),
                    Kode("""
                        for i in range(5):
                            print("Putaran ke-", i)
                    """.trimIndent()),
                )),
                Pelajaran("while", listOf(
                    Teks("while mengulang selama syaratnya benar. Pastikan syaratnya suatu saat menjadi salah, kalau tidak perulangan tak akan berhenti."),
                    Kode("""
                        n = 3
                        while n > 0:
                            print(n)
                            n = n - 1
                        print("Selesai!")
                    """.trimIndent()),
                )),
            ),
            listOf(
                Soal("range(3) menghasilkan…", listOf("1, 2, 3", "0, 1, 2", "0, 1, 2, 3", "3"), 1),
                Soal("Perulangan yang tidak pernah berhenti disebut…", listOf("loop tak hingga", "loop terbalik", "loop bersarang", "loop kosong"), 0),
                Soal("Kata kunci untuk keluar dari loop lebih awal adalah…", listOf("exit", "stop", "break", "end"), 2),
            ),
        ),
        Topik(
            "fungsi", "Fungsi dan List", "Membuat fungsi dan memakai list",
            listOf(
                Pelajaran("Fungsi", listOf(
                    Teks("Fungsi adalah blok kode bernama yang bisa dipakai berulang kali. Dibuat dengan def, dan return mengembalikan nilai."),
                    Kode("""
                        def luas_persegi(sisi):
                            return sisi * sisi

                        print(luas_persegi(4))
                    """.trimIndent()),
                )),
                Pelajaran("List", listOf(
                    Teks("List menyimpan banyak nilai berurutan. Indeks dimulai dari 0."),
                    Kode("""
                        buah = ["apel", "jeruk", "mangga"]
                        buah.append("pisang")
                        print(buah[0])
                        print(len(buah))
                        for b in buah:
                            print(b)
                    """.trimIndent()),
                )),
            ),
            listOf(
                Soal("Kata kunci untuk membuat fungsi adalah…", listOf("func", "def", "function", "lambda"), 1),
                Soal("Indeks pertama pada list adalah…", listOf("0", "1", "-1", "tergantung"), 0),
                Soal("Cara menambah item di akhir list adalah…", listOf(".add()", ".append()", ".push()", ".insert_end()"), 1),
            ),
        ),
    )

    val topik = topikAsli.map {
        it.copy(kuis = it.kuis + isiKosong[it.id].orEmpty(), susun = susun[it.id].orEmpty())
    }

    val latihan = listOf(
        Latihan("Halo Nama", "Mudah",
            "Minta pengguna mengetik namanya, lalu tampilkan \"Halo, <nama>!\".",
            """
            nama = input("Siapa namamu? ")
            print("Halo, " + nama + "!")
            """.trimIndent()),
        Latihan("Luas Lingkaran", "Mudah",
            "Hitung dan tampilkan luas lingkaran dengan jari-jari 7 (gunakan π = 3.14).",
            """
            r = 7
            luas = 3.14 * r * r
            print("Luas lingkaran:", luas)
            """.trimIndent()),
        Latihan("Ganjil atau Genap", "Sedang",
            "Minta sebuah bilangan bulat, lalu tampilkan apakah bilangan itu ganjil atau genap.",
            """
            n = int(input("Masukkan bilangan: "))
            if n % 2 == 0:
                print("Genap")
            else:
                print("Ganjil")
            """.trimIndent()),
        Latihan("Tabel Perkalian", "Sedang",
            "Minta sebuah bilangan, lalu tampilkan tabel perkaliannya dari 1 sampai 10.",
            """
            n = int(input("Tabel perkalian berapa? "))
            for i in range(1, 11):
                print(n, "x", i, "=", n * i)
            """.trimIndent()),
        Latihan("Palindrom", "Sulit",
            "Minta sebuah kata, lalu tampilkan apakah kata itu palindrom (dibaca sama dari depan dan belakang, contoh: katak).",
            """
            kata = input("Masukkan kata: ").lower()
            if kata == kata[::-1]:
                print("Palindrom")
            else:
                print("Bukan palindrom")
            """.trimIndent()),
    )

    val algoritma = listOf(
        Algoritma("Faktorial", "Hasil kali semua bilangan bulat dari 1 sampai n. Contoh: 5! = 120.",
            """
            def faktorial(n):
                hasil = 1
                for i in range(2, n + 1):
                    hasil *= i
                return hasil

            print(faktorial(5))
            """.trimIndent()),
        Algoritma("Deret Fibonacci", "Setiap angka adalah jumlah dua angka sebelumnya: 0, 1, 1, 2, 3, 5, 8, …",
            """
            def fibonacci(n):
                a, b = 0, 1
                for _ in range(n):
                    print(a, end=" ")
                    a, b = b, a + b

            fibonacci(10)
            """.trimIndent()),
        Algoritma("Bubble Sort", "Mengurutkan data dengan membandingkan dua elemen bersebelahan lalu menukarnya bila urutannya salah.",
            """
            def bubble_sort(data):
                n = len(data)
                for i in range(n - 1):
                    for j in range(n - 1 - i):
                        if data[j] > data[j + 1]:
                            data[j], data[j + 1] = data[j + 1], data[j]
                return data

            print(bubble_sort([5, 2, 9, 1, 7]))
            """.trimIndent()),
        Algoritma("Pencarian Biner", "Mencari nilai pada data yang sudah terurut dengan membagi dua rentang pencarian di setiap langkah.",
            """
            def cari_biner(data, target):
                kiri, kanan = 0, len(data) - 1
                while kiri <= kanan:
                    tengah = (kiri + kanan) // 2
                    if data[tengah] == target:
                        return tengah
                    elif data[tengah] < target:
                        kiri = tengah + 1
                    else:
                        kanan = tengah - 1
                return -1

            print(cari_biner([1, 3, 5, 7, 9, 11], 7))
            """.trimIndent()),
    )
}
