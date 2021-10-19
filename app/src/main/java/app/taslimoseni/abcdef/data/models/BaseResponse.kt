package app.taslimoseni.abcdef.data.models

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
        @SerializedName("data")
        val data: T?,
        @SerializedName("success")
        val success: Boolean
)
