package ${packageName}

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
* Entities represent tables in your app's database.
*/
@Entity
data class TemplateEntity(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "full_name") // You can change the name of table field with name parameter
    val fullName: String,

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) // You can change the type of table field with typeAffinity parameter
    val salary: String
)