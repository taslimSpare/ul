package app.taslimoseni.abcdef.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.taslimoseni.abcdef.data.repository.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonRepository: LessonRepository
) : ViewModel() {

    private val refresh = MutableLiveData(true)

    fun getPromotedCards() = lessonRepository.getPromotedCards()

    fun getLessonsUnderLive() = Transformations.switchMap(refresh) {
        lessonRepository.getLessonsUnderLive()
    }

    fun getMyLessons() = Transformations.switchMap(refresh) {
        lessonRepository.getMyLessons()
    }

    fun refresh() {
        refresh.value = true
    }
}