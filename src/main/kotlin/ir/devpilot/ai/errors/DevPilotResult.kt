package ir.devpilot.ai.errors

sealed class DevPilotResult<out T> {
    data class Success<T>(val value: T) : DevPilotResult<T>()
    data class Failure(val error: DevPilotError) : DevPilotResult<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): DevPilotResult<T> {
        if (this is Success) action(value)
        return this
    }

    inline fun onFailure(action: (DevPilotError) -> Unit): DevPilotResult<T> {
        if (this is Failure) action(error)
        return this
    }
}
