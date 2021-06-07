package utils.extensions

import com.android.tools.idea.gradle.dsl.api.ProjectBuildModel
import com.android.tools.idea.util.androidFacet
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import constants.CodeGeneratorConstants
import constants.PluginConstants
import org.jetbrains.kotlin.idea.util.projectStructure.allModules


fun Project.getAppAndroidModuleOrNull(): Module? {
    return allModules()
        .find {
            val moduleBuildModel = ProjectBuildModel.get(this).getModuleBuildModel(it)
            moduleBuildModel
                ?.plugins()
                ?.find { pluginModel ->
                    pluginModel.name().valueAsString() == CodeGeneratorConstants.ANDROID_APPLICATION_PLUGIN
                } != null
//            it.name == name + Char.DOT + CodeGeneratorConstants.APP_MODULE_NAME
        }
}

fun Project.isAndroidProject(): Boolean {
    return allModules().any { it.androidFacet != null }
}

fun Project.showChooseSingleFileDialog(title: String? = null): Array<out VirtualFile>? {
    val fileDescriptor = FileChooserDescriptorFactory
        .createSingleFileDescriptor()
        .apply {
            title?.let { this.title = it }
        }
    val dialog = FileChooserDialogImpl(fileDescriptor, this)
    return dialog.choose(this, null)
}

fun Project.showMessage(msg: String) {
    Messages.showMessageDialog(
        this,
        msg,
        PluginConstants.PLUGIN_NAME,
        Messages.getInformationIcon()
    )
}