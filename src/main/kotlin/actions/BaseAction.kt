package actions

import androidFacet
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import hasAndroidProject
import utils.extensions.getAppAndroidModuleOrNull

abstract class BaseAction : AnAction() {

    protected var module: Module? = null
    protected var project: Project? = null

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.hasAndroidProject()
    }

    override fun actionPerformed(e: AnActionEvent) {
        project = e.project
        module = e
            .androidFacet
            ?.module
            ?: e.project
                ?.getAppAndroidModuleOrNull()
    }
}