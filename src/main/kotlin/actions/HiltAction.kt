package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import constants.CodeGeneratorConstants.CLASSPATH_CONFIG_NAME
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import constants.CodeGeneratorConstants.KAPT_CONFIG_NAME
import core.ActionHandler
import model.DependencyModel
import model.ModificationModel
import model.ModificationStep

class HiltAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = HILT_D_COMPILER,
                                version = HILT_VERSION,
                                componentName = KAPT_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = HILT_D_MODULE,
                                version = HILT_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        ),
                        projectDependencies = listOf(
                            DependencyModel(
                                name = HILT_D_PROJECT,
                                version = HILT_VERSION,
                                componentName = CLASSPATH_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GradleModificationStep.PluginModification(
                        modulePlugins = listOf(HILT_P, HILT_P_KAPT)
                    )
                ),
                module = module!!
            )
            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}