package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.koin.KoinDependenciesConstants
import core.ActionHandler
import model.ModificationModel
import model.ModificationStep

class KoinAction : LibraryAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.DependenciesStep(
                        dependencies = listOf(
                            KoinDependenciesConstants.D_CORE,
                            KoinDependenciesConstants.D_SCOPE,
                            KoinDependenciesConstants.D_VIEW_MODEL
                        )
                    ),
                    ModificationStep.ExistingFiles()
                ),
                module = module!!
            )
            ActionHandler(project!!).handle(dataModel)
        }
    }
}