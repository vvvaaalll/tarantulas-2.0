package hr.vloboda.tarantulas.model.tarantula

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTarantulaDao(
    @SerializedName("species")
    var species: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("origin")
    var origin: String? = null,

    @SerializedName("img-url")
    var img: String? = null,

    @SerializedName("temper")
    var temper: String? = null,

    @SerializedName("venom")
    var venom: String? = null,

    @SerializedName("hairs")
    var hairs: Boolean? = null,

    @SerializedName("last-fed")
    var lastFeeding: String? = null,

    @SerializedName("last-moult")
    var lastMoult: String? = null
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}