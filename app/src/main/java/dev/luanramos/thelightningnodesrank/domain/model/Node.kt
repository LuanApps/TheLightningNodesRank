package dev.luanramos.thelightningnodesrank.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Node(
    @SerializedName("publicKey") val publicKey: String,
    @SerializedName("alias") val alias: String,
    @SerializedName("channels") val channels: Int,
    @SerializedName("capacity") val capacity: Long,
    @SerializedName("firstSeen") val firstSeen: Long,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("city") val city: TranslatedField? = null,
    @SerializedName("country") val country: TranslatedField? = null
): Parcelable

@Parcelize
data class TranslatedField(
    @SerializedName("de") val de: String?,
    @SerializedName("en") val en: String?,
    @SerializedName("es") val es: String?,
    @SerializedName("fr") val fr: String?,
    @SerializedName("ja") val ja: String?,
    @SerializedName("pt-BR") val ptBR: String?,
    @SerializedName("ru") val ru: String?,
    @SerializedName("zh-CN") val zhCN: String?
): Parcelable
