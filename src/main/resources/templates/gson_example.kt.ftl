package ${packageName}

import android.icu.text.SimpleDateFormat
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Since
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*

/**
 * If you want to use gson with retrofit just add Gson Converter factory into your Retrofit Builder
 */
val gsonFactory = GsonConverterFactory.create()

/**
 * Simple examples of using Gson
 */
data class SomeData(
    val name: String,
    @SerializedName("second_name") val secondName: String, // field naming support
    @Since(1.0) val age: Int, // versioning support
    val likeIds: List<Int> // collections support
)

val gson = GsonBuilder() // or you can use Gson()
    .serializeNulls() // null object support
    .setVersion(1.0) // versioning support
    .setPrettyPrinting() // Add whitespaces between field names and its value, object fields, and objects within arrays in the JSON output
    .create()

fun serialize(data: SomeData) {
    val json: String =
        gson.toJson(data) // output: {"name":"someName","second_name":"some second name","age":32,"likeIds":[11,32,344]}
}

fun deserialize(json: String) {
    val someData: SomeData = gson.fromJson(json, SomeData::class.java)
}

/**
 *  Example of custom date time serialization and deserialization
 */

class DateTimeSerializer : JsonSerializer<Date> {

    override fun serialize(src: Date, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val formattedString: String = SimpleDateFormat.getDateTimeInstance().format(src)
        return JsonPrimitive(formattedString)
    }
}

class DateTimeDeserializer : JsonDeserializer<Date?> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        val dateTimeFormatter = SimpleDateFormat.getDateTimeInstance()
        return dateTimeFormatter.parse(json?.asJsonPrimitive?.asString)
    }
}