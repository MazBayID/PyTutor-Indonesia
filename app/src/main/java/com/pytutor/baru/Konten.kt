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

        Topik(
            "operator", "Operator Python", "Aritmetika, perbandingan, logika, dan keanggotaan",
            listOf(
                Pelajaran("Operator aritmetika", listOf(
                    Teks("Python mendukung +, -, *, /, // (pembagian bulat), % (sisa bagi), dan ** (pangkat)."),
                    Kode("""a = 17
b = 5
print(a // b)
print(a % b)
print(2 ** 3)""".trimIndent()),
                )),
                Pelajaran("Operator logika dan keanggotaan", listOf(
                    Teks("and membutuhkan semua kondisi benar, or cukup salah satu benar, dan not membalik nilai boolean. Operator in memeriksa keanggotaan."),
                    Kode("""angka = [2, 4, 6, 8]
print(4 in angka)
print(3 not in angka)
print(10 > 5 and 2 < 4)""".trimIndent()),
                )),
            ),
            listOf(
                Soal("Operator untuk menghitung pangkat adalah…", listOf("*", "**", "//", "%"), 1),
                Soal("Hasil dari 17 // 5 adalah…", listOf("3", "3.4", "2", "4"), 0),
                Soal("Operator untuk memeriksa keanggotaan adalah…", listOf("is", "in", "==", "and"), 1),
            ),
        ),
        Topik(
            "string", "Mengolah String", "Indeks, slicing, format, dan metode teks",
            listOf(
                Pelajaran("Indeks dan slicing", listOf(
                    Teks("String adalah urutan karakter. Indeks dimulai dari 0 dan indeks negatif menghitung dari belakang. Slicing mengambil sebagian teks dengan pola teks[awal:akhir]."),
                    Kode("""kata = "Python"
print(kata[0])
print(kata[-1])
print(kata[1:4])""".trimIndent()),
                )),
                Pelajaran("Metode string dan format", listOf(
                    Teks("Gunakan lower(), upper(), strip(), replace(), dan split() untuk mengolah teks. f-string memudahkan penyisipan nilai."),
                    Kode("""nama = "  Budi  "
rapi = nama.strip().title()
umur = 18
print(f"Halo {rapi}, umur {umur}")""".trimIndent()),
                )),
            ),
            listOf(
                Soal("Indeks karakter pertama string adalah…", listOf("0", "1", "-1", "Panjang string"), 0),
                Soal("Metode untuk menghapus spasi di tepi adalah…", listOf("trim()", "strip()", "clean()", "remove()"), 1),
                Soal("f-string ditandai dengan awalan…", listOf("s", "f", "format", "$"), 1),
            ),
        ),
        Topik(
            "koleksi", "Dictionary dan Koleksi", "Menyimpan data dengan pasangan kunci dan nilai",
            listOf(
                Pelajaran("Dictionary", listOf(
                    Teks("Dictionary menyimpan pasangan kunci dan nilai. Ambil nilai menggunakan kunci, lalu tambahkan atau ubah nilai dengan penugasan."),
                    Kode("""siswa = {"nama": "Ayu", "nilai": 90}
print(siswa["nama"])
siswa["kelas"] = "10A"
print(siswa.get("nilai"))""".trimIndent()),
                )),
                Pelajaran("Tuple dan set", listOf(
                    Teks("Tuple mirip list tetapi tidak dapat diubah. Set menyimpan anggota unik dan cocok untuk operasi himpunan."),
                    Kode("""koordinat = (10, 20)
warna = {"merah", "biru", "merah"}
print(koordinat[0])
print(warna)
print({1, 2, 3} | {3, 4})""".trimIndent()),
                )),
            ),
            listOf(
                Soal("Dictionary menyimpan data dalam bentuk…", listOf("Indeks saja", "Kunci dan nilai", "Karakter", "Baris kode"), 1),
                Soal("Struktur data yang tidak dapat diubah adalah…", listOf("List", "Dictionary", "Tuple", "Set"), 2),
                Soal("Set secara otomatis menghilangkan…", listOf("Angka", "Duplikasi", "String", "Kunci"), 1),
            ),
        ),
        Topik(
            "modul", "Modul dan Fungsi Lanjutan", "import, parameter, dan ruang lingkup",
            listOf(
                Pelajaran("Menggunakan modul", listOf(
                    Teks("Modul berisi fungsi dan alat yang dapat digunakan kembali. import memuat modul, lalu nama_modul.fungsi() memanggil fiturnya."),
                    Kode("""import math
print(math.sqrt(81))
print(math.pi)""".trimIndent()),
                    Teks("Ketersediaan modul bergantung pada interpreter Skulpt yang digunakan aplikasi."),
                )),
                Pelajaran("Parameter dan nilai bawaan", listOf(
                    Teks("Fungsi dapat memiliki parameter default. Argumen bernama membuat pemanggilan fungsi lebih jelas."),
                    Kode("""def sapa(nama, pesan="Halo"):
    print(pesan, nama)

sapa("Dina")
sapa(nama="Raka", pesan="Selamat pagi")""".trimIndent()),
                )),
            ),
            listOf(
                Soal("Kata kunci untuk memuat modul adalah…", listOf("include", "import", "using", "load"), 1),
                Soal("Nilai bawaan parameter ditulis menggunakan…", listOf("==", "=", ":", "->"), 1),
                Soal("Pemanggilan math.sqrt(81) menghasilkan…", listOf("8", "9", "18", "81"), 1),
            ),
        ),
        Topik(
            "error", "Menangani Kesalahan", "try, except, dan debugging dasar",
            listOf(
                Pelajaran("Memahami error", listOf(
                    Teks("SyntaxError muncul saat penulisan kode tidak sesuai aturan. NameError biasanya muncul ketika nama belum didefinisikan. TypeError terjadi saat operasi tidak cocok dengan tipe data."),
                    Kode("""angka = 10
print(angka)
# Periksa nama variabel dan tipe data
# jika program menghasilkan error.""".trimIndent()),
                )),
                Pelajaran("try dan except", listOf(
                    Teks("Gunakan try untuk kode yang berpotensi gagal dan except untuk menangani jenis kesalahan tertentu."),
                    Kode("""try:
    umur = int(input("Umur: "))
    print("Tahun depan:", umur + 1)
except ValueError:
    print("Masukkan angka yang valid")""".trimIndent()),
                )),
            ),
            listOf(
                Soal("Blok untuk menangani kesalahan adalah…", listOf("catch", "except", "error", "handle"), 1),
                Soal("Nama variabel yang belum ada memicu…", listOf("NameError", "ValueError", "SyntaxError", "IndexError"), 0),
                Soal("Blok kode yang dicoba terlebih dahulu adalah…", listOf("except", "finally", "try", "raise"), 2),
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
        Latihan("Rata-rata Nilai", "Sedang",
            "Buat list nilai, hitung rata-ratanya, lalu tampilkan hasilnya.",
            """
            nilai = [80, 90, 75, 85]
            rata = sum(nilai) / len(nilai)
            print("Rata-rata:", rata)
            """.trimIndent()),
        Latihan("Kalkulator Sederhana", "Sedang",
            "Buat fungsi yang menerima dua angka dan operator (+, -, *, /), lalu mengembalikan hasilnya.",
            """
            def hitung(a, operator, b):
                if operator == "+":
                    return a + b
                if operator == "-":
                    return a - b
                if operator == "*":
                    return a * b
                if operator == "/" and b != 0:
                    return a / b
                return "Operasi tidak valid"

            print(hitung(8, "*", 3))
            """.trimIndent()),
        Latihan("Frekuensi Huruf", "Sulit",
            "Hitung berapa kali setiap huruf muncul dalam sebuah kata menggunakan dictionary.",
            """
            kata = "banana"
            frekuensi = {}
            for huruf in kata:
                frekuensi[huruf] = frekuensi.get(huruf, 0) + 1
            print(frekuensi)
            """.trimIndent()),
        Latihan("Bilangan Prima", "Sulit",
            "Periksa apakah sebuah bilangan lebih besar dari 1 adalah bilangan prima.",
            """
            n = 29
            prima = n > 1
            for i in range(2, n):
                if n % i == 0:
                    prima = False
                    break
            print("Prima" if prima else "Bukan prima")
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
        Algoritma("Linear Search", "Memeriksa elemen satu per satu sampai target ditemukan atau data habis.",
            """
            def cari_linear(data, target):
                for indeks, nilai in enumerate(data):
                    if nilai == target:
                        return indeks
                return -1

            print(cari_linear([4, 8, 2, 9], 2))
            """.trimIndent()),
        Algoritma("Selection Sort", "Memilih nilai terkecil dari bagian yang belum terurut lalu menukarnya ke posisi berikutnya.",
            """
            def selection_sort(data):
                for i in range(len(data)):
                    terkecil = i
                    for j in range(i + 1, len(data)):
                        if data[j] < data[terkecil]:
                            terkecil = j
                    data[i], data[terkecil] = data[terkecil], data[i]
                return data

            print(selection_sort([5, 2, 9, 1, 7]))
            """.trimIndent()),
        Algoritma("Stack dan Queue", "Stack memakai prinsip LIFO, sedangkan queue memakai FIFO.",
            """
            stack = []
            stack.append("A")
            stack.append("B")
            print(stack.pop())

            antrean = ["A", "B"]
            antrean.append("C")
            print(antrean.pop(0))
            """.trimIndent()),
    )
}
