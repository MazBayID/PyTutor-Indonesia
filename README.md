# PyTutor Baru

Aplikasi belajar Python offline berbahasa Indonesia, ditulis ulang dengan Kotlin + Jetpack Compose.
Terinspirasi dari [whilson03/PyTutor](https://github.com/whilson03/PyTutor) (MIT). Tanpa Firebase, tanpa internet.

Fitur: materi + kuis bertingkat (bintang 1-3, topik terbuka berurutan), latihan dengan solusi,
kumpulan algoritma, dan interpreter Python (Skulpt, mendukung `input()`).

## Cara build
1. Pasang Android Studio versi terbaru (JDK 17 sudah termasuk).
2. File > Open > pilih folder ini, tunggu Gradle sync (butuh internet sekali).
3. Aktifkan Opsi Pengembang + USB debugging di HP, sambungkan, lalu tekan Run.
   Atau: Build > Build APK(s), lalu pasang file APK-nya.

## Menambah materi
Semua konten ada di `app/src/main/java/com/pytutor/baru/Konten.kt`.
Tambahkan `Topik`, `Latihan`, atau `Algoritma` baru ke daftar yang ada.

## Catatan
- minSdk 26 (Android 8.0), targetSdk 35. Cocok untuk Android 14.
- Skulpt adalah Python 3 versi terbatas: tidak semua library dan fitur CPython tersedia.

## Lisensi
MIT, lihat `LICENSE`. Kredit pihak ketiga ada di `NOTICE.md`.
