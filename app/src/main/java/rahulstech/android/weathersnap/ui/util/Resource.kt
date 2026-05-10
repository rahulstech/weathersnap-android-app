package rahulstech.android.weathersnap.ui.util

sealed class Resource<out T> {
    object Idle : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val cause: Throwable) : Resource<Nothing>()
}
