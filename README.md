# 📱 Post Test Praktikum Pemrograman Mobile — Pertemuan 5

> *"Documentation is a love letter you write to your future self."* 💌

---

## 🧩 Materi
**Recyler View**

Praktikum ini membahas bagaimana data dapat ditampilkan dalam bentuk list, data disimpan di database lokal.

---

## 👨‍💻 Identitas
| Nama | NIM | Slot | PJ |
|------|-----|------|------|
| Farhan Muhammad Iqbal | 2300018164 | Rabu, 07:0–08:30 | Kak Indri |

---

## ⚙️ Teknologi yang Digunakan
- **Android Studio (Kotlin)**
- **View Binding**
- **Kotlin**
- **View Binding** (Menggantikan `findViewById`)
- **Room Database** (Untuk database SQLite lokal)
- **ViewModel** & **LiveData** (Untuk arsitektur MVVM)
- **RecyclerView** (Untuk daftar Story dan Post)
- **Coroutines** (Untuk operasi database di background thread)
- **Material Components** (Untuk `FAB`, `CardView`, `AlertDialog`)
- **Glide** (Untuk memuat gambar dari URI ke `ImageView`)
- Minimum SDK: 21 (Lollipop)

---

## 🧠 Deskripsi Aplikasi

Aplikasi ini terdiri dari satu activity utama yang akan menampilkan:

-**Tampilan Story (Dummy):** Menampilkan daftar 10 story dummy menggunakan `RecyclerView` horizontal.
- **Tampilan Post (Database):** Menampilkan daftar postingan secara vertikal menggunakan `RecyclerView` yang datanya diambil dari `Room Database`.
- **Pre-populate Database:** Saat aplikasi pertama kali diinstal, database akan otomatis diisi dengan 4 postingan default (termasuk gambar dari `drawable`).
- **Tambah Post:** Pengguna dapat menambahkan postingan baru melalui `FloatingActionButton` (FAB) yang akan memunculkan `AlertDialog`.
- **Ambil Gambar:** Pengguna dapat memilih gambar dari galeri device saat menambah atau mengedit post.
- **Edit Post:** Setiap post memiliki menu opsi (`PopupMenu`) untuk "Edit". Ini akan membuka `AlertDialog` yang sama dengan form tambah post, namun sudah terisi data yang ada.
- **Hapus Post:** Menu opsi juga berisi pilihan "Hapus" yang akan memunculkan dialog konfirmasi sebelum menghapus data dari `Room Database`.
  
Form dilengkapi dengan validasi dan informasi lainnya, misalnya:
> ⚠️ Toast jika Kolom masih kosong jika belum diisi.
> Toast berhasil ubah, hapus, tambah.


---

## 🚀 Alur Program
Berikut adalah alur kerja aplikasi dari sisi teknis dan pengguna:

1.  **Inisialisasi & Memuat Data Awal (Read)**
    * Aplikasi diluncurkan, `MyApplication` dimuat.
    * `AppDatabase` diinisialisasi secara *lazy*.
    * `RoomDatabase.Callback` terpicu saat database pertama kali dibuat (`onCreate`).
    * Callback ini menjalankan `populateDatabase()` menggunakan `CoroutineScope` untuk memasukkan 4 data post default ke dalam tabel `posts` di background thread.
    * `MainActivity` dibuat. `PostViewModel` diinisialisasi dan mendapatkan akses ke `PostDao` melalui `MyApplication`.
    * `PostViewModel` mengekspos `allPosts` sebagai `LiveData<List<Post>>` yang diambil dari DAO.
    * `MainActivity` meng-observe (`observe`) `allPosts`. Ketika data default selesai dimasukkan, `LiveData` otomatis memicu update.
    * `PostAdapter.submitList()` dipanggil, `RecyclerView` (`rvPosts`) menampilkan 4 postingan default.
    * `setupStoryRecyclerView()` dipanggil untuk mengisi `rvStories` dengan 10 data dummy `Story`.

2.  **Alur Menambah Postingan Baru (Create)**
    * Pengguna menekan `FloatingActionButton` (FAB) "Tambah".
    * `showAddOrEditPostDialog(null)` dipanggil (argumen `null` menandakan mode "Tambah").
    * Sebuah `AlertDialog` yang berisi layout `dialog_add_post.xml` ditampilkan.
    * Pengguna menekan tombol "Tambah Gambar". Fungsi `openGallery()` dipanggil.
    * `pickImageLauncher` (sebuah `ActivityResultLauncher`) membuka galeri.
    * Setelah pengguna memilih gambar, `launcher` menerima `Uri` gambar, menyimpannya di `selectedImageUri`, dan menampilkannya di `ivPreview`.
    * Pengguna mengisi `etUsername` dan `etCaption`, lalu menekan "Simpan".
    * Aplikasi melakukan validasi input (memastikan semua kolom terisi dan gambar dipilih).
    * Jika valid, sebuah objek `Post` baru dibuat.
    * `postViewModel.insert(newPost)` dipanggil. `ViewModel` menggunakan `viewModelScope` untuk memanggil `postDao.insertPost()` di background thread.
    * `Room` memasukkan data baru ke database. `LiveData` `allPosts` otomatis ter-update, memicu observer di `MainActivity`.
    * `PostAdapter` menerima daftar baru dan `rvPosts` menampilkan postingan baru di bagian atas.

3.  **Alur Mengedit Postingan (Update)**
    * Pengguna menekan ikon titik tiga (menu) pada salah satu item di `rvPosts`.
    * Callback `onMenuClick` dari `PostAdapter` dieksekusi di `MainActivity`.
    * `showPostMenu()` menampilkan `PopupMenu` dengan opsi "Edit" dan "Hapus".
    * Pengguna memilih "Edit Postingan".
    * `showAddOrEditPostDialog(post)` dipanggil, kali ini dengan data `post` yang dipilih.
    * `AlertDialog` yang sama muncul, namun form diisi dengan data dari `post` (username, caption, dan gambar preview).
    * Pengguna mengubah data dan menekan "Update".
    * Validasi input dijalankan.
    * `postViewModel.update(updatedPost)` dipanggil, yang menjalankan `postDao.updatePost()` di background thread.
    * `LiveData` ter-update, dan `rvPosts` memperbarui tampilan item yang di-edit.

4.  **Alur Menghapus Postingan (Delete)**
    * Pengguna menekan ikon titik tiga dan memilih "Hapus Postingan".
    * `showDeleteConfirmDialog(post)` dipanggil.
    * Sebuah `AlertDialog` konfirmasi muncul ("Anda yakin?").
    * Pengguna menekan "Ya".
    * `postViewModel.delete(post)` dipanggil, yang menjalankan `postDao.deletePost()` di background thread.
    * `LiveData` ter-update, `PostAdapter` menerima list baru (tanpa post yang dihapus), dan `rvPosts` menghapus item tersebut dari layar.

---
## ✨ Fitur Utama

* **Tampilan Story (Dummy):** Menampilkan daftar 10 story dummy menggunakan `RecyclerView` horizontal.
* **Tampilan Post (Database):** Menampilkan daftar postingan secara vertikal menggunakan `RecyclerView` yang datanya diambil dari `Room Database`.
* **Pre-populate Database:** Saat aplikasi pertama kali diinstal, database akan otomatis diisi dengan 4 postingan default (termasuk gambar dari `drawable`).
* **Tambah Post:** Pengguna dapat menambahkan postingan baru melalui `FloatingActionButton` (FAB) yang akan memunculkan `AlertDialog`.
* **Ambil Gambar:** Pengguna dapat memilih gambar dari galeri device saat menambah atau mengedit post.
* **Edit Post:** Setiap post memiliki menu opsi (`PopupMenu`) untuk "Edit". Ini akan membuka `AlertDialog` yang sama dengan form tambah post, namun sudah terisi data yang ada.
* **Hapus Post:** Menu opsi juga berisi pilihan "Hapus" yang akan memunculkan dialog konfirmasi sebelum menghapus data dari `Room Database`.

---

## 🏁 Status
✅ *Selesai — Post Test Pertemuan 5 (Recycler View)*

---

### 💬 Catatan
> Proyek ini memperdalam konsep room database dan pengaplikasian list data menggunakan Kotlin dan View Binding.  
> Simple, clean, and functional ✨
