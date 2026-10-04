package com.glazdan.shopperTrackerAndroidApp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [OfflineProductDbModel::class],
    version = 1,
    exportSchema = false
)

abstract class OfflineProductsDatabase: RoomDatabase(){
    abstract fun productsDao(): OfflineProductDao


    //DoubleCheck
    companion object{
        private var instance: OfflineProductsDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): OfflineProductsDatabase{
            instance?.let { return it }
            synchronized(LOCK){
                instance?.let { return it }
                return Room.databaseBuilder(
                    context = context,
                    klass = OfflineProductsDatabase::class.java,
                    name = "products.db"
                ).build().also{
                    instance = it
                }

            }
        }

    }
}