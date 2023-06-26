package org.bedu.filmapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.bedu.filmapp.data.room.dao.SessionDao
import org.bedu.filmapp.data.room.entities.Session

@Database(entities = [Session::class], version = 1)
abstract class DataBase : RoomDatabase(){
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var dbInstance: DataBase? = null

        private const val DB_NAME = "vehicle_db"

        fun getInstance(context: Context) : DataBase {

            return dbInstance?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    DB_NAME
                ).build()
                dbInstance = instance

                instance
            }
        }
    }
}