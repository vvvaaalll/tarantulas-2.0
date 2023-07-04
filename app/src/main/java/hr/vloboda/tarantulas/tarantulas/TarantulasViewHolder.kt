package hr.vloboda.tarantulas.tarantulas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import hr.vloboda.tarantulas.databinding.TarantulaCardBinding
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao

class TarantulasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(tarantula: TarantulaDao) {
        val binding = TarantulaCardBinding.bind(itemView)

        binding.tvSpecies.text = tarantula.species ?: ""
        binding.tvName.text = tarantula.name ?: ""
        binding.tvOrigin.text = tarantula.origin ?: ""

        when (tarantula.temper) {
            "1" -> binding.temperGroup.check(binding.temper1.id)
            "2" -> binding.temperGroup.check(binding.temper2.id)
            "3" -> binding.temperGroup.check(binding.temper3.id)
            "4" -> binding.temperGroup.check(binding.temper4.id)
            "5" -> binding.temperGroup.check(binding.temper5.id)
            "6" -> binding.temperGroup.check(binding.temper6.id)
            else -> ""
        }

        // Check radio button for venom
        when (tarantula.venom) {
            "1" -> binding.venomGroup.check(binding.venom1.id)
            "2" -> binding.venomGroup.check(binding.venom2.id)
            "3" -> binding.venomGroup.check(binding.venom3.id)
            else -> ""
        }

        binding.urticatingCheckBox.isChecked = tarantula.hairs ?: false

        if (tarantula.img != null && tarantula.img != "") {
            Glide.with(itemView)
                .load(tarantula.img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tarantulaImage)
        } else{
            Glide.with(itemView)
                .load("https://static.wikia.nocookie.net/pocketants/images/8/87/Tara_sprite.png/revision/latest/scale-to-width-down/350?cb=20211209042715")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tarantulaImage)
        }
    }
}