package hr.vloboda.tarantulas.tarantulas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import hr.vloboda.tarantulas.databinding.TarantulaCardBinding
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao


class TarantulasAdapter : ListAdapter<TarantulaDao, TarantulasViewHolder>(DiffCallback) {

    var onTarantulaItemSelectedListener: OnTarantulaEventListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarantulasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TarantulaCardBinding.inflate(inflater, parent, false)
        return TarantulasViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: TarantulasViewHolder, position: Int) {
        val tarantula = getItem(position)
        holder.bind(tarantula)

        onTarantulaItemSelectedListener?.let { listener ->
            holder.itemView.setOnClickListener{listener.onTarantulaSelected(position)}
            holder.itemView.setOnLongClickListener{listener.onTarantulaLongPress(position)}
        }

    }



    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TarantulaDao>() {
            override fun areItemsTheSame(oldItem: TarantulaDao, newItem: TarantulaDao): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TarantulaDao, newItem: TarantulaDao): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.species == newItem.species &&
                        oldItem.name == newItem.name &&
                        oldItem.origin == newItem.origin &&
                        oldItem.temper == newItem.temper &&
                        oldItem.venom == newItem.venom &&
                        oldItem.hairs == newItem.hairs &&
                        oldItem.img == newItem.img
            }
        }
    }
}