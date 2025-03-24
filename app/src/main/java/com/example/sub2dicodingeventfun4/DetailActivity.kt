package com.example.sub2dicodingeventfun4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.sub2dicodingeventfun4.data.remote.response.Event
import com.example.sub2dicodingeventfun4.data.remote.response.EventDetailResponse
import com.example.sub2dicodingeventfun4.data.remote.retrofit.ApiConfig
import com.example.sub2dicodingeventfun4.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val eventId = intent.getIntExtra(EXTRA_EVENT, -1)
        if (eventId != -1) {
            viewModel.fetchEventDetail(eventId)
        }
        observeViewModel()
        setupFavoriteButton()
    }
    private fun observeViewModel() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        viewModel.event.observe(this, Observer { event ->
            if (event != null) {
                updateUI(event)
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(event: Event) {
        supportActionBar?.title = getString(R.string.dicoding_detail_event)
        binding.eventName.text = event.name
        val remainingQuota = event.quota - event.registrants
        binding.eventOwnerName.text = getString(R.string.event_owner_name, event.ownerName)
        binding.eventBeginTime.text = getString(R.string.event_begin_time, event.beginTime)
        binding.eventRegistrant.text = getString(R.string.event_registrant, event.registrants)
        binding.eventQuota.text = getString(R.string.event_quota, remainingQuota)
        binding.eventDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.eventPoster)
        binding.eventLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupFavoriteButton() {
        viewModel.isFavorite.observe(this) { isFavorite ->
            val iconResource = if (isFavorite) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
            binding.favoriteButton.setIconResource(iconResource)
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }
}