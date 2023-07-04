package hr.vloboda.tarantulas.tarantulas

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.vloboda.tarantulas.databinding.AddTarantulaCardBinding
import hr.vloboda.tarantulas.model.tarantula.CreateTarantulaDao
import hr.vloboda.tarantulas.repository.ImageRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class AddTarantulaFragment : Fragment() {

    private lateinit var binding: AddTarantulaCardBinding
    private val viewModel: TarantulasViewModel by viewModel()
    private val imageRepository: ImageRepository by inject()
    private var selectedImageUri: Uri? = null
    var imgUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTarantulaCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tarantulaImage.setOnClickListener {
            openGalleryForImage()
        }


        binding.btnSubmit.setOnClickListener {
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
            val createTarantulaDao = CreateTarantulaDao(
                name = name,
                hairs = hairs,
                origin = origin,
                species = species,
                temper = temper,
                venom = venom,
                imgUrl = imgUrl
            )

            viewModel.add(createTarantulaDao)

            val action =
                AddTarantulaFragmentDirections.actionAddTarantulaFragmentToTarantulasListFragment()
            findNavController().navigate(action)
        }

    }


    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImageUri
                    )
                    binding.tarantulaImage.setImageBitmap(bitmap)

                    // Upload the selected image and set the imgUrl
                    selectedImageUri?.let { uri ->
                        imageRepository.uploadImageFile(uri).thenAccept { imageUrl ->
                            this.imgUrl = imageUrl
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
}