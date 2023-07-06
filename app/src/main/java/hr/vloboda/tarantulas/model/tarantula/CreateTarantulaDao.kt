package hr.vloboda.tarantulas.model.tarantula

import com.google.gson.annotations.SerializedName

class CreateTarantulaDao(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("urticating-hairs")
    val hairs: Boolean? = null,

    @SerializedName("origin")
    val origin: String? = null,

    @SerializedName("species")
    val species: String? = null,

    @SerializedName("temper")
    val temper: String? = null,

    @SerializedName("venom")
    val venom: String? = null,

    @SerializedName("img-url")
    val imgUrl: String? = null,

) {

}

