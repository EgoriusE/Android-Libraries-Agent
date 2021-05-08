package ${packageName}

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TemplateEntity(
    @PrimaryKey
    val id: Long
)
