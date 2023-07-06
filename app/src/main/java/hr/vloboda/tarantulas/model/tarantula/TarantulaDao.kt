package hr.vloboda.tarantulas.model.tarantula

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

class TarantulaDao (
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("species")
    var species: String? = "",

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("origin")
    var origin: String? = "",

    @SerializedName("img-url")
    var img: String? = null,



    @SerializedName("temper")
    var temper: String? = "",

    @SerializedName("venom")
    var venom: String? = "",

    @SerializedName("urticating-hairs")
    var hairs: Boolean? = false,

    @SerializedName("last-fed")
    var lastFeeding: String? = null,

    @SerializedName("last-moult")
    var lastMoult: String? = null
){}