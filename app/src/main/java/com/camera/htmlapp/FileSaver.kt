package com.camera.htmlapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.JavascriptInterface
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileSaver(private val context: Context) {

    @JavascriptInterface
    fun savePhoto(base64Data: String): String {
        return try {
            // Remove data URL prefix if present
            val base64 = if (base64Data.contains(",")) {
                base64Data.substring(base64Data.indexOf(",") + 1)
            } else {
                base64Data
            }

            // Decode base64 to bitmap
            val imageBytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            if (bitmap == null) {
                return "Error: Could not decode image"
            }

            // Generate filename with timestamp
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filename = "photo_$timestamp.jpg"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (API 29+): Use MediaStore API (no permission needed)
                savePhotoToMediaStore(bitmap, filename)
            } else {
                // Android 9 and below: Save to Pictures directory
                savePhotoToFile(bitmap, filename)
            }

            "Photo saved: $filename"
        } catch (e: Exception) {
            "Error saving photo: ${e.message}"
        }
    }

    private fun savePhotoToMediaStore(bitmap: Bitmap, filename: String): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return false

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
        }

        return true
    }

    private fun savePhotoToFile(bitmap: Bitmap, filename: String): Boolean {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!picturesDir.exists()) {
            picturesDir.mkdirs()
        }

        val file = File(picturesDir, filename)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
        }

        // Notify media scanner
        val intent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = android.net.Uri.fromFile(file)
        context.sendBroadcast(intent)

        return true
    }
}

