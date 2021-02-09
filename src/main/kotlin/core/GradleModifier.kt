package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel

abstract class GradleModifier {
    abstract fun checkDependenciesExist(buildModel: GradleBuildModel)
    abstract fun addDependencies(buildModel: GradleBuildModel, dependencies: List<String>)
}