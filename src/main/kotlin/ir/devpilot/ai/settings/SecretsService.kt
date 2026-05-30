package ir.devpilot.ai.settings

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import ir.devpilot.ai.errors.DevPilotError
import ir.devpilot.ai.errors.DevPilotResult
import ir.devpilot.ai.logging.DevPilotLogger

@Service(Service.Level.APP)
class SecretsService {

    companion object {
        private const val SERVICE_NAME = "DevPilotAI.GapGptApiKey"
        private const val USERNAME = "gapgpt-api-key"

        fun getInstance(): SecretsService = service()
    }

    private val attributes: CredentialAttributes
        get() = CredentialAttributes(SERVICE_NAME, USERNAME)

    fun saveApiKey(apiKey: String?): DevPilotResult<Unit> {
        return try {
            if (apiKey.isNullOrBlank()) {
                PasswordSafe.instance.set(attributes, null)
                DevPilotResult.Success(Unit)
            } else {
                PasswordSafe.instance.set(attributes, Credentials(USERNAME, apiKey.trim()))
                DevPilotResult.Success(Unit)
            }
        } catch (t: Throwable) {
            val error = DevPilotError.Storage(
                messageForUser = "Failed to save API key securely.",
                technicalMessage = "PasswordSafe save operation failed.",
                cause = t
            )
            DevPilotLogger.logError(error)
            DevPilotResult.Failure(error)
        }
    }

    fun getApiKey(): DevPilotResult<String?> {
        return try {
            val credentials = PasswordSafe.instance.get(attributes)
            val password = credentials?.password?.toString()?.takeIf { it.isNotBlank() }
            DevPilotResult.Success(password)
        } catch (t: Throwable) {
            val error = DevPilotError.Storage(
                messageForUser = "Failed to read API key securely.",
                technicalMessage = "PasswordSafe read operation failed.",
                cause = t
            )
            DevPilotLogger.logError(error)
            DevPilotResult.Failure(error)
        }
    }

    fun hasApiKey(): DevPilotResult<Boolean> {
        return when (val result = getApiKey()) {
            is DevPilotResult.Success -> DevPilotResult.Success(!result.value.isNullOrBlank())
            is DevPilotResult.Failure -> result
        }
    }

    fun clearApiKey(): DevPilotResult<Unit> {
        return try {
            PasswordSafe.instance.set(attributes, null)
            DevPilotResult.Success(Unit)
        } catch (t: Throwable) {
            val error = DevPilotError.Storage(
                messageForUser = "Failed to clear API key securely.",
                technicalMessage = "PasswordSafe clear operation failed.",
                cause = t
            )
            DevPilotLogger.logError(error)
            DevPilotResult.Failure(error)
        }
    }
}
