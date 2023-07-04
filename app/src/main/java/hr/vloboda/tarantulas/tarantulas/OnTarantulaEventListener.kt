package hr.vloboda.tarantulas.tarantulas

interface OnTarantulaEventListener {
    fun onTarantulaSelected(position: Int?)
    fun onTarantulaLongPress(position: Int?): Boolean
}