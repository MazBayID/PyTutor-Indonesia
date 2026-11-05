# PyTutor Indonesia

**Aplikasi belajar Python berbahasa Indonesia untuk Android.**

<em>Versi 0.02 · Tahap pengembangan aktif</em>

PyTutor Indonesia adalah aplikasi pembelajaran Python yang dirancang untuk membantu pengguna Indonesia mengenal dasar-dasar pemrograman secara bertahap melalui materi, latihan, kuis, dan eksperimen kode langsung dari perangkat Android.

Proyek ini masih dalam tahap pengembangan awal (**v0.02**). Fitur, tampilan, materi, dan pengalaman pengguna masih dapat berubah seiring proses pengembangan. Versi ini merupakan fondasi awal aplikasi, bukan rilis final.

## Tentang aplikasi

PyTutor Indonesia dikembangkan menggunakan **Kotlin** dan **Jetpack Compose**. Aplikasi dirancang agar materi pembelajaran dan fitur utamanya dapat digunakan secara offline setelah aplikasi terpasang. Komponen interpreter dan konten pembelajaran disiapkan agar pengguna dapat belajar serta mencoba kode Python langsung di perangkat.

Proyek ini merupakan penulisan ulang dengan Kotlin + Jetpack Compose dan terinspirasi dari [whilson03/PyTutor](https://github.com/whilson03/PyTutor) yang menggunakan lisensi MIT.

## Fitur

Fitur yang sudah menjadi bagian dari fondasi proyek saat ini meliputi:

- **Materi pembelajaran:** 10 sektor Python dari dasar, operator, string, koleksi, modul, hingga penanganan error.
- **Kuis bertingkat:** soal dengan penilaian bintang 1–3 dan pembukaan topik secara berurutan.
- **Latihan coding:** latihan pemrograman yang dilengkapi solusi.
- **Kumpulan algoritma:** 7 contoh algoritma, termasuk visualisasi langkah Bubble Sort.
- **Interpreter Python:** menjalankan dan mencoba kode Python menggunakan Skulpt, termasuk dukungan `input()`.
- **Antarmuka sci-fi:** tema gelap bernuansa futuristik, kartu materi berwarna, dan dashboard progres.
- **Syntax highlighting:** pewarnaan keyword, string, komentar, angka, fungsi bawaan, dan nama fungsi Python.
- **Tipografi kode:** Anonymous Pro Regular untuk contoh, editor, output, dan tantangan susun kode.
- **Learning Galaxy:** ringkasan sektor selesai, level, XP, streak, dan milestone.

> **Catatan versi 0.01:** Implementasi dan cakupan fitur masih terus dikembangkan. Beberapa bagian dapat berubah, belum lengkap, atau memiliki keterbatasan.

## Tampilan, editor, dan Learning Galaxy

Antarmuka sedang dikembangkan dengan identitas visual sci-fi gelap: latar navy, aksen cyan dan violet, kartu materi, serta indikator progres. Tampilan ini akan terus disempurnakan agar tetap nyaman dibaca pada layar ponsel. Dashboard menampilkan ringkasan progres Learning Galaxy, sementara visualisasi Bubble Sort memungkinkan pengguna menelusuri proses pengurutan langkah demi langkah.

Seluruh contoh dan editor kode menggunakan font monospace **Anonymous Pro Regular** yang dibundel secara lokal agar dapat digunakan secara offline. Font didistribusikan sesuai SIL Open Font License 1.1; salinan lisensinya tersedia di [licenses/AnonymousPro-OFL.txt](licenses/AnonymousPro-OFL.txt).

## Teknologi

- **Kotlin** — bahasa pemrograman utama aplikasi.
- **Jetpack Compose** — toolkit UI Android.
- **Skulpt** — interpreter Python berbasis JavaScript untuk menjalankan kode Python di dalam aplikasi.

Skulpt menyediakan implementasi Python dengan cakupan yang terbatas. Tidak semua library, modul, dan fitur yang tersedia pada CPython dapat digunakan.

## Persyaratan

- Android **8.0 (API 26)** atau lebih baru.
- Android Studio versi terbaru yang mendukung proyek ini.
- JDK 17.
- Koneksi internet diperlukan saat pertama kali menyiapkan proyek dan mengunduh dependensi Gradle. Setelah aplikasi terpasang, fitur pembelajaran yang tersedia secara lokal dirancang untuk dapat digunakan tanpa koneksi internet.

## Build dari source

1. Pasang Android Studio versi terbaru (JDK 17 sudah termasuk).
2. Buka Android Studio, pilih **File > Open**, lalu pilih folder repository ini.
3. Tunggu proses Gradle Sync selesai. Proses awal memerlukan koneksi internet untuk mengunduh dependensi.
4. Untuk menjalankan aplikasi, aktifkan **Developer options** dan **USB debugging** pada perangkat Android, sambungkan perangkat, lalu tekan **Run**.
5. Untuk membuat APK, pilih **Build > Build APK(s)**. Setelah proses selesai, pasang APK pada perangkat Android.

## Struktur konten

Konten pembelajaran saat ini dikelola di:

`app/src/main/java/com/pytutor/baru/Konten.kt`

Di dalam file tersebut, pengembangan konten dapat dilakukan dengan menambahkan atau memperbarui data `Topik`, `Latihan`, dan `Algoritma` pada daftar yang tersedia.

## Status pengembangan

| Informasi | Status |
|---|---|
| Nama proyek | PyTutor Indonesia |
| Versi saat ini | **v0.02** |
| Tahap | Pengembangan awal |
| Platform | Android |
| Bahasa aplikasi | Indonesia |
| Status rilis | Belum final |

## Roadmap

Pengembangan berikutnya akan berfokus pada penyempurnaan fondasi aplikasi, kualitas materi pembelajaran, pengalaman latihan dan kuis, serta stabilitas interpreter. Rincian roadmap dapat berubah mengikuti hasil pengujian dan prioritas pengembangan.

## Lisensi dan kredit

Proyek ini menggunakan lisensi MIT. Silakan lihat file [LICENSE](LICENSE) untuk ketentuan lengkap. Kredit dan atribusi komponen pihak ketiga tersedia di [NOTICE.md](NOTICE.md).
