package ir.devpilot.ai.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import ir.devpilot.ai.errors.DevPilotError

object ErrorPresenter {

    fun show(project: Project?, error: DevPilotError, title: String = "DevPilot AI") {
        when (error) {
            is DevPilotError.Validation,
            is DevPilotError.Authentication,
            is DevPilotError.Network,
            is DevPilotError.RateLimited,
            is DevPilotError.Server,
            is DevPilotError.Storage,
            is DevPilotError.Unknown -> {
                Messages.showErrorDialog(project, error.messageForUser, title)
            }
        }
    }

    fun showInfo(project: Project?, message: String, title: String = "DevPilot AI") {
        Messages.showInfoMessage(project, message, title)
    }

    fun showWarning(project: Project?, message: String, title: String = "DevPilot AI") {
        Messages.showWarningDialog(project, message, title)
    }
}
