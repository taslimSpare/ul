package app.taslimoseni.abcdef.data.models


import androidx.lifecycle.liveData
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
)

class Success<T>(data: T?, message: String? = null) : Resource<T>(data, message)
class Failed<T> : Resource<T>()
class Loading<T>(data: T? = null) : Resource<T>(data)
class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

fun <T> responseLiveData(
    networkCall: suspend () -> Response<T>,
    databaseCall: (suspend (T) -> Unit)? = null
) = liveData<Resource<T>> {
    emit(Loading())
    try {
        val response = networkCall.invoke()
        if (response.isSuccessful) {
            val data = response.body()!!
            databaseCall?.invoke(data)
            emit(Success(data))
        } else {
            emit(Error("An error has occurred", response.body()))
        }
    } catch (c: IOException) {
        emit(Failed())
    } catch (e: Exception) {
        Timber.e(e)
        emit(Error(e.message, null))
    }
}