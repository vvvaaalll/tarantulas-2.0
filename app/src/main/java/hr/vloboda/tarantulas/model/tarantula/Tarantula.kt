package hr.vloboda.tarantulas.model.tarantula

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName


data class Tarantula(
    @SerializedName("species")
    var species: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("origin")
    var origin: String = "",

    @SerializedName("img-url")
    var img: String? = null,

    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("temper")
    var temper: String = "",

    @SerializedName("venom")
    var venom: String = "",

    @SerializedName("hairs")
    var hairs: Boolean = false,
)