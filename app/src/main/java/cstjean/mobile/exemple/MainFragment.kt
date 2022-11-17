package cstjean.mobile.exemple

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.*
import cstjean.mobile.exemple.databinding.FragmentMainBinding
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val POLL_WORK = "POLL_WORK"

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null

    private val gifGalleryViewModel: GifGalleryViewModel by viewModels()

    private val binding
        get() = checkNotNull(_binding) {
            "Binding est null. La vue est visible ??"
        }

    private  var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(PollWorker::class.java)
            .setConstraints(constraints)
            .build()

        val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            POLL_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest

        )
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                gifGalleryViewModel.uiState.collect { state ->
                    Log.d("GIPHY", "$state")
                    binding.photoGrid.adapter = GifListAdapter(state.gifs)
                    searchView?.setQuery(state.query, false)
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_main, menu)

                val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
                searchView = searchItem.actionView as SearchView

                searchView?.apply {
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(queryText: String): Boolean {
                            gifGalleryViewModel.setQuery(queryText)
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean = false
                    })
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_item_clear -> {
                        gifGalleryViewModel.setQuery("")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView = null
    }
}