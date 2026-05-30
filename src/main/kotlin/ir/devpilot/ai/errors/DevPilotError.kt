package ir.devpilot.ai.errors

sealed class DevPilotError(
    open val messageForUser: String,
    open val technicalMessage: String? = null,
    open val cause: Throwable? = null
) {
    data class Validation(
        override val messageForUser: String,
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)

    data class Authentication(
        override val messageForUser: String = "API key is invalid or unauthorized.",
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)

    data class Network(
        override val messageForUser: String = "Network error occurred. Please check your connection.",
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)

    data class RateLimited(
        override val messageForUser: String = "Request limit reached. Please try again later.",
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)

    data class Server(
        override val messageForUser: String = "The AI service is temporarily unavailable.",
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)

    data class Storage(
        override val messageForUser: String = "Failed to securely store or read the API key.",
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)

    data class Unknown(
        override val messageForUser: String = "An unexpected error occurred.",
        override val technicalMessage: String? = null,
        override val cause: Throwable? = null
    ) : DevPilotError(messageForUser, technicalMessage, cause)
}
