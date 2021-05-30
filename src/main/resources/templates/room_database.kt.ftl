package ${packageName}

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
* Create room database instance. You can use it like this:
* val db = createRoomDb(context)
* val templateDao = db.templateDao()
* templateDao.getById(0)
*/
fun createRoomDb(applicationContext: Context): TemplateDatabase {
    return Room.databaseBuilder(
        applicationContext,
        TemplateDatabase::class.java, "database-name"
    ).build()
}

/**
* TemplateDatabase class holds the database. TemplateDatabase defines the database configuration
* and serves as the app's main access point to the persisted data.
*/
@Database(entities = [TemplateEntity::class], version = 1)
abstract class TemplateDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
}
