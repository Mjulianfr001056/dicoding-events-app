package org.bangkit.dicodingevent.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import org.bangkit.dicodingevent.R
import org.bangkit.dicodingevent.data.response.DicodingEvent
import org.bangkit.dicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    val event : DicodingEvent by lazy {
        getEventFromIntent()
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.text = event.name
        binding.tvSummary.text = event.summary
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivMediaCover)

        val quota = event.quota ?: 0
        val registrants = event.registrants ?: 0
        binding.cQuota.text = (quota - registrants).toString()

        binding.cDeskripsi.text = event.description?.let {
            HtmlCompat.fromHtml(
                it,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

    }

    private fun getEventFromIntent() : DicodingEvent {
        val defaultEvent = DicodingEvent(
            summary = "No Summary Available",
            mediaCover = "default_image_url",
            registrants = 0,
            imageLogo = "default_logo_url",
            link = "https://example.com",
            description = "No Description Available",
            ownerName = "Unknown Owner",
            cityName = "Unknown City",
            quota = 0,
            name = "Unknown Event",
            id = -1, // Or some invalid ID
            beginTime = "N/A",
            endTime = "N/A",
            category = "Uncategorized"
        )

        return if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_EVENT, DicodingEvent::class.java) ?: defaultEvent
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_EVENT) ?: defaultEvent
        }
    }
}