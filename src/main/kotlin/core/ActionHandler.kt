package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider
import com.intellij.openapi.project.Project
import model.ModificationModel
import model.ModificationStep

class ActionHandler(private val project: Project) {

    fun handle(model: ModificationModel) {
        model.steps.forEach { step ->
            when (step) {

                is ModificationStep.DependenciesStep -> {
                    val buildModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(model.module)
                    if (buildModel != null) {
                        RoomGradleModifier().addDependencies(buildModel, step.dependencies)
                    }
                }

                is ModificationStep.BoilerPlateStep -> {
                    val dirHelper = DirHelper(model.module)
                    val boilerPlateDir = if (step.dirName != null) {
                        dirHelper.createBoilerplateDir(step.dirName)
                    } else dirHelper.getPackageDir()

                    val templateGenerator = TemplateGenerator(project)
                    val generatedFiles = templateGenerator.generateFiles(step.filesNames, step.model)
                    FileAdder(project).addFiles(generatedFiles, boilerPlateDir)
                }

                is ModificationStep.ExistingFiles -> {
                SrcModifier(project).modify(model)
                }
            }
        }
    }
}