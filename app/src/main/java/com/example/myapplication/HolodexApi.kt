package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class HolodexVideo(
    val id: String,
    val title: String,
    val status: String,
    val channel: HolodexChannel,
    val start_scheduled: String? = null,
    val available_at: String? = null,
    val type: String? = null,
    val topic_id: String? = null
)

data class HolodexChannel(
    val id: String,
    val name: String,
    val photo: String? = null,
    val org: String? = null
)

interface HolodexApi {
    @GET("live")
    suspend fun getLiveStreams(
        @Query("org") org: String = "Hololive",
        @Query("status") status: String = "live"
    ): List<HolodexVideo>

    @GET("videos")
    suspend fun getVideos(
        @Query("org") org: String = "Hololive",
        @Query("status") status: String, // live, upcoming, past
        @Query("type") type: String? = null,
        @Query("limit") limit: Int = 50
    ): List<HolodexVideo>

    companion object {
        private const val BASE_URL = "https://holodex.net/api/v2/"

        fun create(apiKey: String? = null): HolodexApi {
            val client = okhttp3.OkHttpClient.Builder().apply {
                if (!apiKey.isNullOrBlank()) {
                    addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("X-APIKEY", apiKey)
                            .build()
                        chain.proceed(request)
                    }
                }
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HolodexApi::class.java)
        }
    }
}
