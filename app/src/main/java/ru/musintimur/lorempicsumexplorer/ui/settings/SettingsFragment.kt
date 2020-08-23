package ru.musintimur.lorempicsumexplorer.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.musintimur.lorempicsumexplorer.R
import ru.musintimur.lorempicsumexplorer.app.preferences.IntProperties

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        setupObservers()
        setupListeners()
        settingsViewModel.loadSettings()
    }

    private fun setupObservers() {
        val owner = viewLifecycleOwner
        settingsViewModel.run {
            getSettings().observe(owner, Observer {
                restoreSettings(it)
            })
            isSaved().observe(owner, Observer {
                if (it) goBack()
            })
        }
    }

    private fun setupListeners() {
        fabSaveSettings.setOnClickListener { saveSettings() }
    }

    private fun saveSettings() {
        if (!validateInput()) return
        val settings = mapOf(
            IntProperties.PHOTO_WIDTH.propertyName to editTextPhotoWidth.text.toString().toInt(),
            IntProperties.PHOTO_HEIGHT.propertyName to editTextPhotoHeight.text.toString().toInt(),
            IntProperties.PAGE_SIZE.propertyName to editTextPerPage.text.toString().toInt(),
            IntProperties.INITIAL_PAGE_SIZE.propertyName to editTextInitialLoad.text.toString().toInt()
        )
        settingsViewModel.saveSettings(settings)
    }

    private fun restoreSettings(settings: Map<String, Int>) {
        editTextPhotoWidth.setText(settings[IntProperties.PHOTO_WIDTH.propertyName].toString())
        editTextPhotoHeight.setText(settings[IntProperties.PHOTO_HEIGHT.propertyName].toString())
        editTextPerPage.setText(settings[IntProperties.PAGE_SIZE.propertyName].toString())
        editTextInitialLoad.setText(settings[IntProperties.INITIAL_PAGE_SIZE.propertyName].toString())
    }

    fun validateInput(): Boolean {
        val minWidth = 100
        val maxWidth = 1280
        val minHeight = 100
        val maxHeight = 1280
        val minPageSize = 5
        val maxPageSize = 100
        val minInitialPageSize = 10
        val maxInitialPageSize = 100

        if (editTextPhotoWidth.text.isBlank() ||
            editTextPhotoWidth.text.toString().toIntOrNull() == null ||
            editTextPhotoWidth.text.toString().toInt() !in (minWidth..maxWidth)) {
            showToast(getString(R.string.invalid_width, minWidth, maxWidth))
            return false
        }

        if (editTextPhotoHeight.text.isBlank() ||
            editTextPhotoHeight.text.toString().toIntOrNull() == null ||
            editTextPhotoHeight.text.toString().toInt() !in (minHeight..maxHeight)) {
            showToast(getString(R.string.invalid_height, minHeight, maxHeight))
            return false
        }

        if (editTextPerPage.text.isBlank() ||
            editTextPerPage.text.toString().toIntOrNull() == null ||
            editTextPerPage.text.toString().toInt() !in (minPageSize..maxPageSize)) {
            showToast(getString(R.string.invalid_page_size, minPageSize, maxPageSize))
            return false
        }

        if (editTextInitialLoad.text.isBlank() ||
            editTextInitialLoad.text.toString().toIntOrNull() == null ||
            editTextInitialLoad.text.toString().toInt() !in (minInitialPageSize..maxInitialPageSize)) {
            showToast(getString(R.string.invalid_initial_page_size, minInitialPageSize, maxInitialPageSize))
            return false
        }

        return true
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}