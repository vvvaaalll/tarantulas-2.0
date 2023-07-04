package hr.vloboda.tarantulas.repository.common

import hr.vloboda.tarantulas.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AuthRestAdapter {
    companion object Factory {
        fun createAPI(): AuthAPIInterface? {
            val logging = HttpLoggingInterceptor()
            logging.level = Level.BODY
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(5, TimeUnit.SECONDS)
            builder.writeTimeout(10, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(logging)
            }
            builder.cache(null)
            val okHttpClient = builder.build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(AuthAPIInterface::class.java)
        }
    }
}
