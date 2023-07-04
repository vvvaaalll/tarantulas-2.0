package hr.vloboda.tarantulas.repository

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

class ImageRepository {

    private val fStorage = FirebaseStorage.getInstance()

    fun uploadImageFile(image: Uri): CompletableFuture<String> {
        val imageUrlFuture = CompletableFuture<String>()

        val refImage = fStorage.reference
            .child("tarantulas")
            .child(LocalDateTime.now().toString())

        refImage.putFile(image)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                refImage.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageUrlFuture.complete(downloadUri.toString())
                } else {
                    imageUrlFuture.completeExceptionally(task.exception)
                }
            }

        return imageUrlFuture
    }
}
