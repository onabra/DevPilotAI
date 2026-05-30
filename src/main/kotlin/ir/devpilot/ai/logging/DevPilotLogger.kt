package ir.devpilot.ai.logging

import com.intellij.openapi.diagnostic.Logger
import ir.devpilot.ai.errors.DevPilotError

object DevPilotLogger {
    private val logger = Logger.getInstance("DevPilotAI")

    fun info(message: String) {
        logger.info(message)
    }

    fun warn(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            logger.warn(message, throwable)
        } else {
            logger.warn(message)
        }
    }

    fun error(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            logger.error(message, throwable)
        } else {
            logger.error(message)
        }
    }

    fun logError(error: DevPilotError) {
        when (error) {
            is DevPilotError.Validation -> warn(error.technicalMessage ?: error.messageForUser, error.cause)
            is DevPilotError.Authentication -> warn(error.technicalMessage ?: error.messageForUser, error.cause)
            is DevPilotError.Network -> warn(error.technicalMessage ?: error.messageForUser, error.cause)
            is DevPilotError.RateLimited -> warn(error.technicalMessage ?: error.messageForUser, error.cause)
            is DevPilotError.Server -> error(error.technicalMessage ?: error.messageForUser, error.cause)
            is DevPilotError.Storage -> error(error.technicalMessage ?: error.messageForUser, error.cause)
            is DevPilotError.Unknown -> error(error.technicalMessage ?: error.messageForUser, error.cause)
        }
    }
}
