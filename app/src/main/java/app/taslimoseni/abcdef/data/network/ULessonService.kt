package app.taslimoseni.abcdef.data.network



import app.taslimoseni.abcdef.data.models.BaseResponse
import app.taslimoseni.abcdef.data.models.Lesson
import retrofit2.Response
import retrofit2.http.*


interface ULessonService {

    @GET("promoted")
    suspend fun getPromotedCards(): Response<BaseResponse<List<Lesson>>>

    @GET("lessons")
    suspend fun getLessonsUnderLive(): Response<BaseResponse<List<Lesson>>>

    @GET("lessons/me")
    suspend fun getMyLessons(): Response<BaseResponse<List<Lesson>>>


}