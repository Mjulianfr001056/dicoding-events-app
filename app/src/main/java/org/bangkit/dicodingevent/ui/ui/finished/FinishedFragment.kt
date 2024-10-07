package org.bangkit.dicodingevent.ui.ui.finished

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.bangkit.dicodingevent.R
import org.bangkit.dicodingevent.data.response.DicodingEvent
import org.bangkit.dicodingevent.databinding.FragmentFinishedBinding
import org.bangkit.dicodingevent.databinding.FragmentUpcomingBinding
import org.bangkit.dicodingevent.ui.DicodingEventAdapter

class FinishedFragment : Fragment() {

    private val viewModel: FinishedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFinishedBinding.bind(view)

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = DicodingEventAdapter()
        binding.rvEvent.adapter = adapter

        viewModel.eventlist.observe(viewLifecycleOwner) { events ->
            setEventList(events, adapter)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding)
        }
    }

    private fun setEventList(eventList : List<DicodingEvent>, adapter: DicodingEventAdapter) {
        adapter.submitList(eventList)
    }

    private fun showLoading(isLoading: Boolean, binding: FragmentFinishedBinding) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}