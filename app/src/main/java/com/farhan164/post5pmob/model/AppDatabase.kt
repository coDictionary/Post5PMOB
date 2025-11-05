package com.farhan164.post5pmob.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farhan164.post5pmob.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    // Definisikan Callback di dalam AppDatabase
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.postDao())
                }
            }
        }

        // Fungsi untuk mengisi database
        suspend fun populateDatabase(postDao: PostDao) {
            val uri1 = "android.resource://com.farhan164.post5pmob/${R.drawable.img_pantai}"
            val uri2 = "android.resource://com.farhan164.post5pmob/${R.drawable.img_happy}"
            val uri3 = "android.resource://com.farhan164.post5pmob/${R.drawable.img_tutoring}"
            val uri4 = "android.resource://com.farhan164.post5pmob/${R.drawable.img_selfie}"

            val profileUri1 = "android.resource://com.farhan164.post5pmob/${R.drawable.avatars_male}"
            val profileUri2 = "android.resource://com.farhan164.post5pmob/${R.drawable.avatars_male_glasses}"
            val profileUri3 = "android.resource://com.farhan164.post5pmob/${R.drawable.avatars_male}"
            val profileUri4 = "android.resource://com.farhan164.post5pmob/${R.drawable.avatars_female}"

            postDao.insertPost(Post(username = "intan_dwi", caption = "Liburan ke pantai 🌊", imageUri = uri1, profileUri = profileUri1))
            postDao.insertPost(Post(username = "minda_04", caption = "Hari yang menyenangkan! ✨", imageUri = uri2, profileUri = profileUri2))
            postDao.insertPost(Post(username = "rubi_community", caption = "Edukasi anak SD", imageUri = uri3, profileUri = profileUri3))
            postDao.insertPost(Post(username = "Yorsyd", caption = "lagi selfie", imageUri = uri4, profileUri = profileUri4))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "post_database"
                )
                    // Tambahkan callback di sini
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
