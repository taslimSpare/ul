package app.taslimoseni.abcdef.data.repository

import app.taslimoseni.abcdef.data.models.responseLiveData
import app.taslimoseni.abcdef.data.network.ULessonService
import app.taslimoseni.abcdef.di.CustomClient
import javax.inject.Inject

class LessonRepository @Inject constructor(
    @CustomClient private val client: ULessonService
) {

    fun getPromotedCards() = responseLiveData(
        { client.getPromotedCards() }
    )

    fun getLessonsUnderLive() = responseLiveData(
        { client.getLessonsUnderLive() }
    )

    fun getMyLessons() = responseLiveData(
        { client.getMyLessons() }
    )



}