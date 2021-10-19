package app.taslimoseni.abcdef.di


import app.taslimoseni.abcdef.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @HttpClient
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            client.addInterceptor(loggingInterceptor)
        }
        return client.build()
    }

    @Singleton
    @HttpClient
    @Provides
    fun provideRetrofit(@HttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.END_POINT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CustomClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpClient