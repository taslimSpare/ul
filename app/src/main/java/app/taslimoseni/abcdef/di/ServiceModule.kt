package app.taslimoseni.abcdef.di

import app.taslimoseni.abcdef.data.network.ULessonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Singleton
    @CustomClient
    @Provides
    fun provideULessonService(@HttpClient retrofit: Retrofit): ULessonService {
        return retrofit.create(ULessonService::class.java)
    }

}
