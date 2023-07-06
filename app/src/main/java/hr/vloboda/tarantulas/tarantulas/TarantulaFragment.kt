package hr.vloboda.tarantulas.tarantulas

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import hr.vloboda.tarantulas.databinding.FragmentTarantulaBinding
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao
import hr.vloboda.tarantulas.model.tarantula.UpdateTarantulaDao
import hr.vloboda.tarantulas.repository.ImageRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class TarantulaFragment : Fragment() {

    private lateinit var binding: FragmentTarantulaBinding
    private val viewModel: TarantulasViewModel by viewModel()
    private val imageRepository: ImageRepository by inject()

    private val args: TarantulaFragmentArgs by navArgs()
    private var tarantulaId: Long = 0
    private lateinit var tarantula: TarantulaDao
    private var isEditing: Boolean = false

    private var lastFeedingDate: Date? = null
    private var lastMoultDate: Date? = null

    private lateinit var feedingDatePicker: DatePickerDialog
    private lateinit var moultDatePicker: DatePickerDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTarantulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tarantulaId = args.tarantulaId // Assign the value from args

        viewModel.getById(tarantulaId)
        viewModel.tarantula.observe(viewLifecycleOwner) { tarantulaDao ->
            tarantula = tarantulaDao
            if (tarantula != null) {
                displayTarantula(tarantula)
            }
        }

        val today = Calendar.getInstance()
        feedingDatePicker = createDatePickerDialog(today) { date ->
            lastFeedingDate = date
            binding.tvLastFeeding.text = formatDate(date)
        }
        moultDatePicker = createDatePickerDialog(today) { date ->
            lastMoultDate = date
            binding.tvLastMoult.text = formatDate(date)
        }

        binding.btnPickLastFeeding.setOnClickListener {
            feedingDatePicker.show()
        }

        binding.btnPickLastMoult.setOnClickListener {
            moultDatePicker.show()
        }

        binding.btnDelete.setOnClickListener {
            val deleteTarantulaDialog =
                context?.let { androidx.appcompat.app.AlertDialog.Builder(it) }
            deleteTarantulaDialog!!.setTitle("Delete tarantula?")
            deleteTarantulaDialog.setPositiveButton("Yes") { dialog, which ->
                viewModel.delete(tarantulaId = tarantulaId)
                navigateToTarantulaListFragment()
            }
            deleteTarantulaDialog.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            deleteTarantulaDialog.show()
        }

        binding.btnEdit.setOnClickListener {
            isEditing = !isEditing
            if (isEditing) {
                enableEditing()
                binding.btnEdit.text = "Submit"
            } else {
                saveTarantulaChanges()
                disableEditing()
                binding.btnEdit.text = "Edit"
                navigateToTarantulaListFragment()
            }
        }

        binding.tarantulaImage.setOnClickListener {
            if (isEditing) {
                openGalleryForImage()
            }
        }
    }

    private fun displayTarantula(tarantula: TarantulaDao) {

        binding.tvName.setText(tarantula.name)
        binding.urticatingCheckBox.isChecked = tarantula.hairs ?: false
        binding.tvOrigin.setText(tarantula.origin)
        binding.tvSpecies.setText(tarantula.species)
        binding.tvLastFeeding.text = tarantula.lastFeeding
        binding.tvLastMoult.text = tarantula.lastMoult

        when (tarantula.temper) {
            "1" -> binding.rgTemper.check(binding.rbTemper1.id)
            "2" -> binding.rgTemper.check(binding.rbTemper2.id)
            "3" -> binding.rgTemper.check(binding.rbTemper3.id)
            "4" -> binding.rgTemper.check(binding.rbTemper4.id)
            "5" -> binding.rgTemper.check(binding.rbTemper5.id)
            "6" -> binding.rgTemper.check(binding.rbTemper6.id)
        }

        when (tarantula.venom) {
            "1" -> binding.rgVenom.check(binding.rbVenom1.id)
            "2" -> binding.rgVenom.check(binding.rbVenom2.id)
            "3" -> binding.rgVenom.check(binding.rbVenom3.id)
        }

        if (!tarantula.img.isNullOrEmpty() && tarantula.img != "") {
            Glide.with(requireContext())
                .load(tarantula.img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tarantulaImage)
        } else {
            Glide.with(requireContext())
                .load("https://static.wikia.nocookie.net/pocketants/images/8/87/Tara_sprite.png/revision/latest/scale-to-width-down/350?cb=20211209042715")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tarantulaImage)
        }
    }

    private fun enableEditing() {
        binding.tvName.isEnabled = true
        binding.urticatingCheckBox.isEnabled = true
        binding.tvOrigin.isEnabled = true
        binding.tvSpecies.isEnabled = true
        binding.rgTemper.isEnabled = true
        binding.rgVenom.isEnabled = true
        binding.btnPickLastFeeding.isEnabled = true
        binding.btnPickLastMoult.isEnabled = true

        for (i in 0 until binding.rgTemper.childCount) {
            val radioButton = binding.rgTemper.getChildAt(i)
            radioButton.isEnabled = true
        }

        for (i in 0 until binding.rgVenom.childCount) {
            val radioButton = binding.rgVenom.getChildAt(i)
            radioButton.isEnabled = true
        }
    }

    private fun disableEditing() {
        binding.tvName.isEnabled = false
        binding.urticatingCheckBox.isEnabled = false
        binding.tvOrigin.isEnabled = false
        binding.tvSpecies.isEnabled = false
        binding.rgTemper.isEnabled = false
        binding.rgVenom.isEnabled = false
        binding.btnPickLastFeeding.isEnabled = false
        binding.btnPickLastMoult.isEnabled = false

        for (i in 0 until binding.rgTemper.childCount) {
            val radioButton = binding.rgTemper.getChildAt(i)
            radioButton.isEnabled = false
        }

        for (i in 0 until binding.rgVenom.childCount) {
            val radioButton = binding.rgVenom.getChildAt(i)
            radioButton.isEnabled = false
        }
    }

    private fun saveTarantulaChanges() {
        // Retrieve edited values from UI elements
        val name = binding.tvName.text.toString()
        val hairs = binding.urticatingCheckBox.isChecked
        val origin = binding.tvOrigin.text.toString()
        val species = binding.tvSpecies.text.toString()
        val temper = when (binding.rgTemper.checkedRadioButtonId) {
            binding.rbTemper1.id -> "1"
            binding.rbTemper2.id -> "2"
            binding.rbTemper3.id -> "3"
            binding.rbTemper4.id -> "4"
            binding.rbTemper5.id -> "5"
            binding.rbTemper6.id -> "6"
            else -> ""
        }
        val venom = when (binding.rgVenom.checkedRadioButtonId) {
            binding.rbVenom1.id -> "1"
            binding.rbVenom2.id -> "2"
            binding.rbVenom3.id -> "3"
            else -> ""
        }

        val lastFeedingDateString = lastFeedingDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.toString()
        val lastMoultDateString = lastMoultDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.toString()

        val editedTarantula = UpdateTarantulaDao(
            name = name,
            hairs = hairs,
            origin = origin,
            species = species,
            temper = temper,
            venom = venom,
            lastFeeding = lastFeedingDateString,
            lastMoult = lastMoultDateString
        )


        // Call the view model to edit the tarantula
        viewModel.edit(editedTarantula, tarantulaId)
    }

    private fun navigateToTarantulaListFragment() {
        val action =
            TarantulaFragmentDirections.actionTarantulaFragmentToTarantulasListFragment()
        findNavController().navigate(action)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    selectedImageUri
                )
                binding.tarantulaImage.setImageBitmap(bitmap)
                // Upload the selected image
                selectedImageUri?.let { uri ->
                    imageRepository.uploadImageFile(uri).thenAccept { imageUrl ->
                        viewModel.updateTarantulaImage(tarantulaId, imageUrl)
                    }

                }
            }
        }

    private fun createDatePickerDialog(
        calendar: Calendar,
        onDateSelected: (Date) -> Unit
    ): DatePickerDialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), { _, y, m, d ->
            calendar.set(y, m, d)
            onDateSelected(calendar.time)
        }, year, month, day)
    }

    private fun formatDate(date: Date?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = formatter.format(date)
        return LocalDate.parse(formattedDate).toString()
    }

}


