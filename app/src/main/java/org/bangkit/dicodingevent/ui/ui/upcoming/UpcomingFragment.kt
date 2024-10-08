package org.bangkit.dicodingevent.ui.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.bangkit.dicodingevent.R
import org.bangkit.dicodingevent.data.response.DicodingEvent
import org.bangkit.dicodingevent.databinding.FragmentUpcomingBinding
import org.bangkit.dicodingevent.ui.DetailActivity
import org.bangkit.dicodingevent.ui.DicodingEventAdapter

class UpcomingFragment : Fragment() {

    private val viewModel: UpcomingViewModel by viewModels()

    companion object {
        private const val TAG = "UpcomingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUpcomingBinding.bind(view)

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = DicodingEventAdapter { dicodingEvent ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT, dicodingEvent)
            startActivity(intent)
        }
        binding.rvEvent.adapter = adapter

        viewModel.eventlist.observe(viewLifecycleOwner) { events ->
            setEventList(events, adapter)
        }

        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireActivity())
        val searchAdapter = DicodingEventAdapter { dicodingEvent ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT, dicodingEvent)
            startActivity(intent)
        }
        binding.rvSearchResults.adapter = searchAdapter

        viewModel.searchResults.observe(viewLifecycleOwner) { filteredEvents ->
//            Log.d(TAG, "onViewCreated: $filteredEvents")
            if (!filteredEvents.isNullOrEmpty()) {
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.rvEvent.visibility = View.GONE
                searchAdapter.submitList(filteredEvents)
            } else {
                binding.rvSearchResults.visibility = View.GONE
                binding.rvEvent.visibility = View.VISIBLE
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.searchEvent(textView.text.toString())
                    false
                }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            binding.searchBar.clearText()

            if (binding.rvSearchResults.visibility == View.VISIBLE) {
                binding.rvSearchResults.visibility = View.GONE
                binding.rvEvent.visibility = View.VISIBLE
            } else {
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setEventList(eventList : List<DicodingEvent>, adapter: DicodingEventAdapter) {
        adapter.submitList(eventList)
    }

    private fun showLoading(isLoading: Boolean, binding: FragmentUpcomingBinding) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}