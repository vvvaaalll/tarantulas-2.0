package hr.vloboda.tarantulas.tarantulas

import hr.vloboda.tarantulas.repository.TarantulasRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.vloboda.tarantulas.model.tarantula.CreateTarantulaDao
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao
import hr.vloboda.tarantulas.model.tarantula.UpdateTarantulaDao

class TarantulasViewModel(
    private val tarantulasRepository: TarantulasRepository
    ) : ViewModel() {
    val tarantulas: MutableLiveData<List<TarantulaDao>> = MutableLiveData()
    val tarantula: MutableLiveData<TarantulaDao> = MutableLiveData()

    fun updateTarantulaImage(tarantulaId: Long, imgUrl: String){
        val updateImage = UpdateTarantulaDao(img = imgUrl)
        this.edit(updateImage, tarantulaId)
    }


    fun fetch() {
        tarantulasRepository.fetch().observeForever { fetchedTarantulas ->
            tarantulas.value = fetchedTarantulas
        }
    }

    fun getById(tarantulaId: Long) {
        tarantulasRepository.getById(tarantulaId).observeForever { fetchedTarantula ->
            tarantula.value = fetchedTarantula
        }
    }


    fun add(tarantula: CreateTarantulaDao) {
        tarantulasRepository.add(tarantula).observeForever { result ->
            this.fetch()
        }
    }

    fun edit(tarantula: UpdateTarantulaDao, tarantulaId: Long) {
        tarantulasRepository.edit(tarantula, tarantulaId).observeForever { updatedTarantula ->
            val updatedList = tarantulas.value?.toMutableList()
            updatedList?.let {
                val index = it.indexOfFirst { it.id == updatedTarantula.id }
                if (index != -1) {
                    it[index] = updatedTarantula
                    tarantulas.value = it
                }
            }
        }
    }

    fun delete(tarantulaId: Long) {
        tarantulasRepository.delete(tarantulaId)

        val updatedList = tarantulas.value?.toMutableList()
        updatedList?.let {
            val index = it.indexOfFirst { it.id == tarantulaId }
            if (index != -1) {
                it.removeAt(index)
                tarantulas.value = it
            }
        }
    }
}