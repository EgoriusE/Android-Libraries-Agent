package core

import com.android.tools.idea.gradle.dsl.api.BuildScriptModel
import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.dependencies.ArtifactDependencyModel
import com.android.tools.idea.gradle.dsl.api.dependencies.DependenciesModel
import execRunWriteAction
import isEqualName
import isPluginExist
import model.DependencyModel

class GradleDependenciesManager {

    fun addDependencies(buildModel: GradleBuildModel, dependencies: List<DependencyModel>) = execRunWriteAction {
        dependencies.forEach { dependency ->
            if (!isDependencyExist(buildModel.dependencies(), dependency.name)) {
                buildModel
                    .dependencies()
                    .addArtifact(dependency.componentName, dependency.fullName)
            }
        }
        buildModel.applyChanges()
    }


    private fun isDependencyExist(dependenciesModel: DependenciesModel, dependencyName: String): Boolean {
        return dependenciesModel
            .all()
            .any { dependencyModel ->
                dependencyModel is ArtifactDependencyModel
                        && dependencyModel.isEqualName(dependencyName)
            }
    }

    fun addDependencies(buildModel: BuildScriptModel, dependencies: List<DependencyModel>) = execRunWriteAction {
        dependencies.forEach { dependency ->
            if (!isDependencyExist(buildModel.dependencies(), dependency.name)) {
                buildModel
                    .dependencies()
                    .addArtifact(dependency.componentName, dependency.fullName)
            }
        }
    }

    fun addPlugins(buildModel: GradleBuildModel, pluginNames: List<String>) = execRunWriteAction {
        pluginNames.forEach { pluginName ->
            if (!buildModel.isPluginExist(pluginName)) {
                buildModel.applyPlugin(pluginName)
            }
        }
        buildModel.applyChanges()
    }
}
