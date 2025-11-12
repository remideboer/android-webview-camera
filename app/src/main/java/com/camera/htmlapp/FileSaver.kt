package com.camera.htmlapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.JavascriptInterface
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileSaver(private val context: Context) {
    private var lastSavedUri: Uri? = null
    private var lastSavedFilePath: String? = null

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
                val uri = savePhotoToMediaStore(bitmap, filename)
                if (uri != null) {
                    lastSavedUri = uri
                    lastSavedFilePath = null
                    return "Photo saved: $filename"
                }
            } else {
                // Android 9 and below: Save to Pictures directory
                val filePath = savePhotoToFile(bitmap, filename)
                if (filePath != null) {
                    lastSavedFilePath = filePath
                    lastSavedUri = null
                    return "Photo saved: $filename"
                }
            }

            "Error: Failed to save photo"
        } catch (e: Exception) {
            "Error saving photo: ${e.message}"
        }
    }

    private fun savePhotoToMediaStore(bitmap: Bitmap, filename: String): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return null

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
        }

        return uri
    }

    private fun savePhotoToFile(bitmap: Bitmap, filename: String): String? {
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        if (!dcimDir.exists()) {
            dcimDir.mkdirs()
        }

        val file = File(dcimDir, filename)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
        }

        // Notify media scanner
        val intent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = android.net.Uri.fromFile(file)
        context.sendBroadcast(intent)

        return file.absolutePath
    }

    @JavascriptInterface
    fun deleteLastPhoto(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+: Delete using MediaStore URI
                lastSavedUri?.let { uri ->
                    val deleted = context.contentResolver.delete(uri, null, null)
                    if (deleted > 0) {
                        lastSavedUri = null
                        "Photo deleted successfully"
                    } else {
                        "Error: Could not delete photo"
                    }
                } ?: "Error: No photo to delete"
            } else {
                // Android 9 and below: Delete using file path
                lastSavedFilePath?.let { filePath ->
                    val file = File(filePath)
                    if (file.exists() && file.delete()) {
                        lastSavedFilePath = null
                        // Notify media scanner
                        val intent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        intent.data = android.net.Uri.fromFile(file)
                        context.sendBroadcast(intent)
                        "Photo deleted successfully"
                    } else {
                        "Error: Could not delete photo file"
                    }
                } ?: "Error: No photo to delete"
            }
        } catch (e: Exception) {
            "Error deleting photo: ${e.message}"
        }
    }

    @JavascriptInterface
    fun sharePhoto(base64Data: String): String {
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

            // Save to temporary file for sharing
            val cacheDir = context.cacheDir
            val file = File(cacheDir, filename)
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
            }

            // Create URI for sharing
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }

            // Create share intent
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Start share activity
            val chooser = Intent.createChooser(shareIntent, "Share photo")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            "Sharing photo..."
        } catch (e: Exception) {
            "Error sharing photo: ${e.message}"
        }
    }

    @JavascriptInterface
    fun shareText(text: String): String {
        return try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }

            val chooser = Intent.createChooser(shareIntent, "Share")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            "Sharing..."
        } catch (e: Exception) {
            "Error sharing: ${e.message}"
        }
    }
}

