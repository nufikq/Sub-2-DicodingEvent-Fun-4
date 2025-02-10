package com.example.sub2dicodingeventfun4.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sub2dicodingeventfun4.Event
import com.example.sub2dicodingeventfun4.EventAdapter
import com.example.sub2dicodingeventfun4.R
import com.example.sub2dicodingeventfun4.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val viewModel: UpcomingViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    // Begin
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()
        viewModel.fetchEvents()
    }

    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner, Observer { events ->
            if (events.isEmpty()) {
                showNoEvent(true)
            } else {
                showNoEvent(false)
                setEventData(events)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setEventData(upcomingEvent: List<Event>) {
        val adapter = EventAdapter()
        binding.rvEvent.adapter = adapter
        adapter.submitList(upcomingEvent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoEvent(isNoEvent: Boolean) {
        binding.imageNoData.visibility = if (isNoEvent) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}