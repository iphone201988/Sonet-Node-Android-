
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tech.sonet.utils.event.RealPathUtil

object RealPathUtilExits {

    fun Intent?.getFilePath(context: Context): String {
        return this?.data?.let { data -> RealPathUtil.getRealPath(context, data) ?: "" } ?: ""
    }

    fun Uri?.getFilePath(context: Context): String {
        return this?.let { uri -> RealPathUtil.getRealPath(context, uri) ?: "" } ?: ""
    }

    fun ClipData.Item?.getFilePath(context: Context): String {
        return this?.uri?.getFilePath(context) ?: ""
    }

}