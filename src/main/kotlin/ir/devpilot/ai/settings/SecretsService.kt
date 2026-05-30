package ir.devpilot.ai.settings

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service

@Service(Service.Level.APP)
class SecretsService {

    companion object {
        private const val SERVICE_NAME = "DevPilotAI.GapGptApiKey"
        private const val USERNAME = "gapgpt-api-key"

        fun getInstance(): SecretsService = service()
    }

    private fun credentialAttributes(): CredentialAttributes {
        return CredentialAttributes(SERVICE_NAME, USERNAME)
    }

    fun saveApiKey(apiKey: String?) {
        val attributes = credentialAttributes()

        if (apiKey.isNullOrBlank()) {
            PasswordSafe.instance.set(attributes, null)
            return
        }

        val credentials = Credentials(USERNAME, apiKey.trim())
        PasswordSafe.instance.set(attributes, credentials)
    }

    fun getApiKey(): String? {
        val attributes = credentialAttributes()
        return PasswordSafe.instance.get(attributes)
            ?.password
            ?.toString()
            ?.takeIf { it.isNotBlank() }
    }

    fun hasApiKey(): Boolean {
        return !getApiKey().isNullOrBlank()
    }

    fun clearApiKey() {
        PasswordSafe.instance.set(credentialAttributes(), null)
    }
}
