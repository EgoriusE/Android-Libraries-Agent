package com.bobrusik.plugin.android_libraries_agent.core

import com.android.tools.idea.projectsystem.ProjectSystemSyncManager
import com.android.tools.idea.projectsystem.gradle.GradleProjectSystemSyncManager
import com.bobrusik.plugin.android_libraries_agent.helpers.FirebaseHelper
import com.bobrusik.plugin.android_libraries_agent.helpers.NotificationsFactory
import com.bobrusik.plugin.android_libraries_agent.helpers.PackageHelper
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service
class ActionHandler(
    project: Project
) {
    private val gradleProjectSystemSyncManager = GradleProjectSystemSyncManager(project)

    private val notificationFactory = project.service<NotificationsFactory>()
    private val generatorCodeProcessor = project.service<GeneratorCodeProcessor>()
    private val gradleModificationProcessor = project.service<GradleModificationProcessor>()

    companion object {
        fun getInstance(project: Project): ActionHandler = project.service()
    }

    fun handle(model: ModificationModel) {
        val packageHelper = PackageHelper(model.module)

        model.steps.forEach { step ->
            when (step) {

                is ModificationStep.GradleModificationStep -> {
                    gradleModificationProcessor.process(step, model)
                }

                is ModificationStep.GenerateCodeStep -> {
                    generatorCodeProcessor.process(step, packageHelper)
                }

//                is ModificationStep.ExistingFiles -> {
//                    SrcModifier(project).modify(model)
//                }

                is ModificationStep.NotificationStep -> {
                    notificationFactory.info(step.message)
                }

                is ModificationStep.OpenInEditorFiles -> {
                    OpeningFilesProcessor().process(step, packageHelper)
                }

                is ModificationStep.CopyJsonToProjectStep -> {
                    FirebaseHelper.execute(model.module)
                }
            }
        }

        gradleProjectSystemSyncManager.syncProject(ProjectSystemSyncManager.SyncReason.PROJECT_MODIFIED)
    }
}