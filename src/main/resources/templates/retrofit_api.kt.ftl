package ${packageName}

import retrofit2.http.*
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.Call

const val BASE_URL = "http://api.example.com/"

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
// TODO: Add converter factory here. For example you can add GsonConverterFactory via Android Library Agent
//  .addConverterFactory()
    .build()

interface Api {

    @GET("sample/{id}/users")
    fun sampleGet(
        @Path("id") pathId: String,
        @Query("sort") sort: String
    ): Call<Triple<Unit, Unit, Unit>>

    @POST("post/query_map")
    fun sampleQueryMap(
        @QueryMap options: Map<String, String>
    )

    @FormUrlEncoded
    @POST("post/sample")
    fun sampleUrlEncoded(
        @Field("first") firstField: String,
        @Field("second") secondField: String
    )

    @Multipart
    @PUT("put/sample")
    fun sampleMultipart(
        @Part("photo") photo: RequestBody,
        @Part("description") description: RequestBody
    ): Call<Pair<Unit, Unit>>?

    @Headers("Cache-Control: max-age=640000")
    @GET("get/sample")
    fun sampleHeader()

}