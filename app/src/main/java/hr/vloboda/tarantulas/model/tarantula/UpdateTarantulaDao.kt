package hr.vloboda.tarantulas.model.tarantula

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

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
    var lastFeeding: LocalDate? = null,

    @SerializedName("last-moult")
    var lastMoult: LocalDate? = null
)