package core

import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.intellij.openapi.command.executeCommand
import execRunWriteAction
import org.jetbrains.kotlin.idea.util.application.runWriteAction

class GradleDependenciesManager {

    fun addDependencies(buildModel: GradleBuildModel, dependencies: List<String>) {
        execRunWriteAction {
            buildModel.apply {
                dependencies.forEach { dependencies().addArtifact(IMPLEMENTATION_CONFIG_NAME, it) }
                applyChanges()
            }
        }
    }

    fun checkDependenciesExist(buildModel: GradleBuildModel) {
        buildModel.dependencies().all()
    }
}
