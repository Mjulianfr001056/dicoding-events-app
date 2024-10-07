package org.bangkit.dicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import org.bangkit.dicodingevent.data.response.DicodingEvent
import org.bangkit.dicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val event : DicodingEvent by lazy {
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
        binding.tvOwnerName.text = event.ownerName
        binding.tvBeginTime.text = event.beginTime

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

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
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