package app.taslimoseni.abcdef.data.models

import com.google.gson.annotations.SerializedName

data class Lesson(
        @SerializedName("id")
        val id: String,
        @SerializedName("tutor")
        val tutor: Tutor,
        @SerializedName("subject")
        val subject: Subject,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("topic")
        val topic: String,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("start_at")
        val startAt: String,
        @SerializedName("expires_at")
        val expiresAt: String,

)
