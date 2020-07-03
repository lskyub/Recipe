package kr.co.sbproject.recipe

import android.app.Application
import android.os.Debug
import kr.co.sbproject.recipe.`interface`.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    companion object {
        val api: ApiInterface by lazy {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder().baseUrl("/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .apply {
                    if (BuildConfig.DEBUG) {
                        client(client)
                    }
                }.build()

            retrofit.create(ApiInterface::class.java)
        }
    }
}