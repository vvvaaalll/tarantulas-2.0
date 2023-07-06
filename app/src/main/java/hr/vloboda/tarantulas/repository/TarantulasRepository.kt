package hr.vloboda.tarantulas.repository

import RestClient
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import hr.vloboda.tarantulas.Tarantulas
import hr.vloboda.tarantulas.model.auth.AuthDao
import hr.vloboda.tarantulas.model.tarantula.CreateTarantulaDao
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao
import hr.vloboda.tarantulas.model.tarantula.UpdateTarantulaDao
import hr.vloboda.tarantulas.repository.common.TarantulaAPIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TarantulasRepository {

    private val restClient = RestClient()
    val apiInterface = restClient.getClient().create(TarantulaAPIInterface::class.java)

    val sharedPreferences = Tarantulas.application.getSharedPreferences(
        "TarantulasPrefs",
        Context.MODE_PRIVATE
    )

    fun fetch(): MutableLiveData<List<TarantulaDao>> {
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao? = Gson().fromJson(authDaoJson, AuthDao::class.java)
        if (!(authDao?.token?.token == "")) {
            val token: String = authDao?.token?.token!!
            val tarantulasLiveData = MutableLiveData<List<TarantulaDao>>()

            apiInterface.getAll("Bearer $token").enqueue(object : Callback<List<TarantulaDao>> {
                override fun onResponse(
                    call: Call<List<TarantulaDao>>,
                    response: Response<List<TarantulaDao>>
                ) {

                    if (response.isSuccessful) {
                        tarantulasLiveData.value = response.body()
                    } else {
                        // Redirect to login activity
                    }
                }

                override fun onFailure(call: Call<List<TarantulaDao>>, t: Throwable) {
                    // Handle failure
                }
            })
            return tarantulasLiveData
        } else {
            return MutableLiveData<List<TarantulaDao>>()
        }
    }

    fun getById(tarantulaId: Long): MutableLiveData<TarantulaDao> {
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao? = Gson().fromJson(authDaoJson, AuthDao::class.java)
        val token: String = authDao?.token?.token!!
        val tarantulaLiveData = MutableLiveData<TarantulaDao>()

        apiInterface.getById("Bearer $token", tarantulaId).enqueue(object : Callback<TarantulaDao> {

            override fun onResponse(call: Call<TarantulaDao>, response: Response<TarantulaDao>) {

                if (response.isSuccessful) {
                    tarantulaLiveData.value = response.body()
                } else {
                    // Redirect to login activity
                }
            }

            override fun onFailure(call: Call<TarantulaDao>, t: Throwable) {
                // Handle failure
            }
        })

        return tarantulaLiveData
    }

    fun edit(tarantula: UpdateTarantulaDao, tarantulaId: Long): MutableLiveData<TarantulaDao> {
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao = Gson().fromJson(authDaoJson, AuthDao::class.java)
        val token: String = authDao.token.token
        val updatedTarantulaLiveData = MutableLiveData<TarantulaDao>()
        val updateTarantulaDao = UpdateTarantulaDao(
            tarantula.species, tarantula.name, tarantula.origin, tarantula.img, tarantula.temper,
            tarantula.venom, tarantula.hairs, tarantula.lastFeeding, tarantula.lastMoult
        )
        apiInterface.updateById("Bearer $token", tarantulaId, updateTarantulaDao)
            .enqueue(object : Callback<TarantulaDao> {
                override fun onResponse(
                    call: Call<TarantulaDao>,
                    response: Response<TarantulaDao>
                ) {
                    if (response.isSuccessful) {
                        val updatedTarantula = response.body()
                        updatedTarantulaLiveData.value = updatedTarantula!!
                    } else {
                        // Redirect to login activity
                    }
                }

                override fun onFailure(call: Call<TarantulaDao>, t: Throwable) {
                    // Handle failure
                }
            })

        return updatedTarantulaLiveData
    }

    fun add(tarantula: CreateTarantulaDao): MutableLiveData<Any> {
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao = Gson().fromJson(authDaoJson, AuthDao::class.java)
        val token: String = authDao.token.token
        val additionResultLiveData = MutableLiveData<Any>()

        apiInterface.addTarantula("Bearer $token", tarantula).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    additionResultLiveData.value = response.body()
                } else {
                    // Redirect to login activity
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // Handle failure
            }
        })

        return additionResultLiveData
    }

    fun delete(tarantulaId: Long) {
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao = Gson().fromJson(authDaoJson, AuthDao::class.java)
        val token: String = authDao.token.token
        apiInterface.delete("Bearer $token", tarantulaId).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    return
                } else {
                    // Redirect to login activity
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // Handle failure
            }
        })
    }

}