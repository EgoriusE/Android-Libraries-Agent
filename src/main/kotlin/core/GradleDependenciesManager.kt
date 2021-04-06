package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.dependencies.ArtifactDependencyModel
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import execRunWriteAction
import isEquals

class GradleDependenciesManager {

    fun addDependencies(buildModel: GradleBuildModel, dependencies: List<String>, version: String) {
        execRunWriteAction {
            buildModel.apply {
                dependencies.forEach { dependencyName ->
                    if (!isDependencyExist(buildModel, dependencyName)) {
                        dependencies().addArtifact(IMPLEMENTATION_CONFIG_NAME, dependencyName + version)
                    }
                }
                applyChanges()
            }
        }
    }

    private fun isDependencyExist(buildModel: GradleBuildModel, dependencyName: String): Boolean {
        return buildModel
            .dependencies()
            .all()
            .any { dependencyModel ->
                dependencyModel is ArtifactDependencyModel
                        && dependencyModel.isEquals(dependencyName)
            }
    }
}
