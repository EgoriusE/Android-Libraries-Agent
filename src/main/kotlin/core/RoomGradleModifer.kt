package core

import CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.intellij.openapi.command.executeCommand
import org.jetbrains.kotlin.idea.util.application.runWriteAction

class RoomGradleModifier : GradleModifier() {

    override fun addDependencies(buildModel: GradleBuildModel, dependencies: List<String>) {
        executeCommand {
            runWriteAction {
                buildModel.apply {
                    dependencies.forEach { dependencies().addArtifact(IMPLEMENTATION_CONFIG_NAME, it) }
                    applyChanges()
                }
            }
        }
    }

    override fun checkDependenciesExist(buildModel: GradleBuildModel) {
        buildModel.dependencies().all()
    }
}
