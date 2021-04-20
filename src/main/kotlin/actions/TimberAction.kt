package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import constants.TIMBER_D
import constants.TIMBER_VERSION
import core.ActionHandler
import model.DependencyModel
import model.ModificationModel
import model.ModificationStep
import model.OpenInEditorFileType

class TimberAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = TIMBER_D,
                                version = TIMBER_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                        ),
                    ),
                    ModificationStep.OpenInEditorFiles(
                        fileTypes = listOf(OpenInEditorFileType.BUILD_GRADLE_APP)
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}