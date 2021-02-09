package ${packageName}

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TemplateEntity::class], version = 1)
abstract class TemplateDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
}
