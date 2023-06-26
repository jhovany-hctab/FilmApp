package org.bedu.filmapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import org.bedu.filmapp.data.room.entities.Session

@Dao
interface SessionDao {
    @Insert
    fun insertSessionData(session: Session)

    @Update
    fun updateSessionData(session: Session)
}