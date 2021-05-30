package ${packageName}

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
* DAO provide methods that your app can use to query, update, insert, and delete data in the database.
*/
@Dao
interface TemplateDao {
    fun getAll(): List<TemplateEntity>?

    @Query("SELECT * FROM TemplateEntity WHERE id = :id")
    fun getById(id: Long): TemplateEntity?

    @Insert
    fun insert(employee: TemplateEntity)
    
    @Update
    fun update(employee: TemplateEntity)

    @Delete
    fun delete(employee: TemplateEntity)
}