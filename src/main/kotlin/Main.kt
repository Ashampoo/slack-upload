import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.files.FilesDeleteRequest
import com.slack.api.methods.request.files.FilesListRequest
import com.slack.api.methods.request.files.FilesUploadRequest
import java.io.File
import java.net.SocketTimeoutException

fun main() {

    val productName = checkNotNull(System.getenv("PRODUCT_NAME")) {
        "Environment variable PRODUCT_NAME must be set."
    }

    val path = checkNotNull(System.getenv("FILE_PATH")) {
        "Environment variable FILE_PATH must be set."
    }

    val channel = checkNotNull(System.getenv("SLACK_CHANNEL")) {
        "Environment variable SLACK_CHANNEL must be set."
    }

    val token = checkNotNull(System.getenv("SLACK_TOKEN")) {
        "Environment variable SLACK_TOKEN must be set."
    }

    val releaseVersion = checkNotNull(System.getenv("RELEASE_VERSION")) {
        "Environment variable RELEASE_VERSION must be set."
    }

    println("Uploading $productName v$releaseVersion = $path")

    val slack = Slack.getInstance()

    val methods: MethodsClient = slack.methods(token)

    /* First delete all old uploads. */

    val filesListRequest = FilesListRequest.builder().build()

    val filesListResponse = methods.filesList(filesListRequest)

    for (file in filesListResponse.files) {

        val deleteRequest = FilesDeleteRequest.builder()
            .file(file.id)
            .build()

        methods.filesDelete(deleteRequest)
    }

    /* Upload the new file */

    val file = File(path)

    val uploadRequest = FilesUploadRequest.builder()
        .channels(listOf(channel))
        .file(file)
        .filename(file.name)
        .title("$productName v$releaseVersion")
        .build()

    try {

        methods.filesUpload(uploadRequest)

    } catch (ignore: SocketTimeoutException) {

        /* It works. Seems to be a problem on Slacks end. */
        println("Ignored SocketTimeoutException.")
    }

    println("Done.")
}