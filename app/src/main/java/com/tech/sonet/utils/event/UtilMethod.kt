import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object UtilMethod {
    fun createMultiPart(filePath: String, partName: String, fileExtension: String): MultipartBody.Part {
        val file = File(filePath)
        val requestBody = RequestBody.create("image/$fileExtension".toMediaTypeOrNull(), file)
        val fileName = "${file.name}.$fileExtension"
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

}