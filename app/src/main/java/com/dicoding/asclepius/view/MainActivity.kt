package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import java.io.File

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var destinationUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.input_image_warning))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history_page -> {
                val historyIntent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(historyIntent)
            }

            R.id.news_page -> {
                val newsIntent = Intent(this@MainActivity, NewsActivity::class.java)
                startActivity(newsIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = uri
            // Start uCrop activity
            startUCropActivity(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private fun startUCropActivity(uri: Uri) {
        destinationUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}_edited.jpg"))

        val options = UCrop.Options().apply {
            setToolbarColor(getColor(R.color.white))
            setStatusBarColor(getColor(R.color.lightPrimary))
            setToolbarWidgetColor(getColor(R.color.lightAccent))
            setRootViewBackgroundColor(getColor(R.color.lightBackground))
            setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)
            setFreeStyleCropEnabled(true)
            setCircleDimmedLayer(false)
        }

        UCrop.of(uri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            // Handle cropped image result
            handleUCropResult(data)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle uCrop error
            val error = UCrop.getError(data!!)
            showToast(error?.localizedMessage ?: getString(R.string.input_image_warning))
        }
    }

    private fun handleUCropResult(data: Intent?) {
        val resultUri = UCrop.getOutput(data!!)
        resultUri?.let {
            binding.previewImageView.setImageURI(resultUri)
            currentImageUri = resultUri
        } ?: showToast(getString(R.string.input_image_warning))
    }

    private fun analyzeImage(uri: Uri) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}