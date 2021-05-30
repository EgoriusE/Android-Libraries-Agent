package ${packageName}

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
* Example of simple interceptor for logging.
*/
fun loggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor(
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp", message)
            }
        }
    ).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

/**
* You can add loggingInterceptor() in the retrofit builder via okHttpClient for
* logging retrofit activity:
* .client(createHttpClient())
*/
fun createHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .apply { addInterceptor(loggingInterceptor()) }
    .build()