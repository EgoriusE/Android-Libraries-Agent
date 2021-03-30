package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.KOIN_D_CORE
import constants.KOIN_D_SCOPE
import constants.KOIN_D_VIEW_MODEL
import core.ActionHandler
import model.ModificationModel
import model.ModificationStep

class KoinAction : BaseAction() {


    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.DependenciesStep(
                        dependencies = listOf(
                            KOIN_D_SCOPE,
                            KOIN_D_CORE,
                            KOIN_D_VIEW_MODEL
                        )
                    ),
                    ModificationStep.ExistingFiles()
                ),
                module = module!!
            )
            ActionHandler(project!!, dataModel).handle()
        }
    }
}