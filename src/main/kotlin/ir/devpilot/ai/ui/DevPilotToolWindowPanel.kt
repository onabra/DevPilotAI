package ir.devpilot.ai.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import ir.devpilot.ai.settings.SecretsService
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel

class DevPilotToolWindowPanel(
    private val project: Project
) : JPanel(BorderLayout()) {

    private val secretsService = SecretsService.getInstance()

    private val apiKeyField = JBPasswordField()
    private val promptField = JBTextArea(6, 40)
    private val outputArea = JBTextArea()

    init {
        outputArea.isEditable = false
        promptField.lineWrap = true
        promptField.wrapStyleWord = true
        outputArea.lineWrap = true
        outputArea.wrapStyleWord = true

        val saveApiKeyButton = JButton("Save API Key")
        val sendButton = JButton("Send")

        val savedApiKey = secretsService.getApiKey()
        if (!savedApiKey.isNullOrBlank()) {
            apiKeyField.text = savedApiKey
        }

        saveApiKeyButton.addActionListener {
            val apiKey = String(apiKeyField.password)

            if (apiKey.isBlank()) {
                secretsService.clearApiKey()
                Messages.showInfoMessage(project, "API key cleared.", "DevPilot AI")
            } else {
                secretsService.saveApiKey(apiKey)
                Messages.showInfoMessage(project, "API key saved securely.", "DevPilot AI")
            }
        }

        sendButton.addActionListener {
            val prompt = promptField.text.trim()
            if (prompt.isBlank()) {
                Messages.showWarningDialog(project, "Please enter a prompt.", "DevPilot AI")
                return@addActionListener
            }

            val apiKey = secretsService.getApiKey()
            if (apiKey.isNullOrBlank()) {
                Messages.showWarningDialog(project, "Please save your GapGPT API key first.", "DevPilot AI")
                return@addActionListener
            }

            outputArea.text = buildString {
                appendLine("Prompt:")
                appendLine(prompt)
                appendLine()
                appendLine("Status:")
                appendLine("GapGPT integration is not connected yet.")
            }
        }

        val form = FormBuilder.createFormBuilder()
            .addLabeledComponent("GapGPT API Key:", apiKeyField)
            .addComponent(saveApiKeyButton)
            .addLabeledComponent("Prompt:", JBScrollPane(promptField))
            .addComponent(sendButton)
            .addLabeledComponent("Response:", JBScrollPane(outputArea))
            .addComponentFillVertically(JPanel(), 0)
            .panel

        add(form, BorderLayout.CENTER)
    }
}
