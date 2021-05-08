package ${packageName}

import android.util.Log
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

// Simple interceptor for logging
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