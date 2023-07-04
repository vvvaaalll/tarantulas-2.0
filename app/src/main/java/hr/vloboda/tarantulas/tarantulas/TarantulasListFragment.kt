package hr.vloboda.tarantulas.tarantulas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.vloboda.tarantulas.AuthenticationActivity
import hr.vloboda.tarantulas.R
import hr.vloboda.tarantulas.authentication.viewModel.AuthenticationViewModel
import hr.vloboda.tarantulas.databinding.FragmentTarantulasBinding
import hr.vloboda.tarantulas.model.tarantula.TarantulaDao
import org.koin.androidx.viewmodel.ext.android.viewModel

class TarantulasListFragment : Fragment(), OnTarantulaEventListener {

    private lateinit var binding: FragmentTarantulasBinding
    private val viewModel: TarantulasViewModel by viewModel()
    private val authViewModel: AuthenticationViewModel by viewModel()

    private lateinit var adapter: TarantulasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTarantulasBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        binding.fabAddTarantula.setOnClickListener {
            val action = TarantulasListFragmentDirections.actionTarantulasListFragmentToAddTarantulaFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeTarantulas()
        viewModel.fetch()
    }

    private fun setupRecyclerView() {
        adapter = TarantulasAdapter()
        binding.tarantulasList.layoutManager = LinearLayoutManager(requireContext())
        binding.tarantulasList.adapter = adapter
        adapter.onTarantulaItemSelectedListener = this
    }

    private fun observeTarantulas() {
        viewModel.tarantulas.observe(viewLifecycleOwner) { tarantulas ->
            adapter.submitList(tarantulas)
        }
    }

    override fun onTarantulaSelected(position: Int?) {
        viewModel.tarantulas.observe(viewLifecycleOwner, { tarantulas ->
            val tarantula = tarantulas?.get(position ?: -1) ?: TarantulaDao()
            val action = TarantulasListFragmentDirections.actionTarantulasListFragmentToTarantulaFragment(tarantula.id!!)
            findNavController().navigate(action)
        })
    }

    override fun onTarantulaLongPress(position: Int?): Boolean {
        // Not implemented yet
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_account -> {
                // Handle delete account action
                true
            }
            R.id.menu_log_out -> {
                // Handle log out action
                authViewModel.handleSignOut()
                val action = TarantulasListFragmentDirections.actionTarantulasListFragmentToAuthenticationActivity()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
