package com.example.sub2dicodingeventfun4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sub2dicodingeventfun4.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
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
            showDetailEvent(eventId)
        }
    }
    private fun showDetailEvent(eventId: Int) {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailEvent(eventId)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val event = response.body()?.event
                    if (event != null) {
                        updateUI(event)
                    }
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                // Handle failure
                showLoading(false)
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
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }
}