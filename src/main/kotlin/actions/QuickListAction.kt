package actions

import com.intellij.ide.actions.QuickSwitchSchemeAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import constants.PluginConstants
import hasAndroidProject

class QuickListAction : QuickSwitchSchemeAction(), DumbAware {

    override fun fillActions(project: Project?, group: DefaultActionGroup, dataContext: DataContext) {

        addAction("com.bobrusik.plugin.android_libraries_agent.RetrofitAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.HttpLoggingInterceptorAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.GsonAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.RoomAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.DataStore", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.KoinAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.HiltAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.DaggerAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.TimberAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.RxJavaAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.FirebaseAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.CrashlyticsAction", group)
        addAction("com.bobrusik.plugin.android_libraries_agent.GlideAction", group)
    }

    private fun addAction(actionId: String, toGroup: DefaultActionGroup) {
        ActionManager
            .getInstance()
            .getAction(actionId)
            ?.let { toGroup.add(it) }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.hasAndroidProject()
    }

    override fun getPopupTitle(e: AnActionEvent) = PluginConstants.PLUGIN_NAME
}