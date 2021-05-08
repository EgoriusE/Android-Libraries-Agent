package actions

import androidFacet
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project

abstract class BaseAction : AnAction() {

    protected var module: Module? = null
    protected var project: Project? = null

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.androidFacet != null

    }

    override fun actionPerformed(e: AnActionEvent) {
        module = e.androidFacet?.module
        project = e.project
    }
}