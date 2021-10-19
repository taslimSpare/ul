package app.taslimoseni.abcdef.data.models

import com.google.gson.annotations.SerializedName

data class Tutor(
        @SerializedName("firstname")
        val firstName: String,
        @SerializedName("lastname")
        val lastName: String
)
