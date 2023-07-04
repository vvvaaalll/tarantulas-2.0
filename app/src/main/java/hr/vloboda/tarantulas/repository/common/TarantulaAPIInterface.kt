package hr.vloboda.tarantulas.repository.common

import hr.vloboda.tarantulas.model.tarantula.CreateTarantulaDao
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao
import hr.vloboda.tarantulas.model.tarantula.UpdateTarantulaDao
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TarantulaAPIInterface{

    @POST("/api/tarantula")
    fun addTarantula(
        @Header("Authorization") token: String,
        @Body createTarantulaDto: CreateTarantulaDao
    ): Call<Any>

    @GET("/api/tarantula")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<List<TarantulaDao>>

    @GET("/api/tarantula/{id}")
    fun getById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<TarantulaDao>

    @DELETE("/api/tarantula/{id}")
    fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<Any>

    @PATCH("/api/tarantula/patch/{id}")
    fun updateById(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body tarantulaUpdateDto: UpdateTarantulaDao
    ): Call<TarantulaDao>
}


