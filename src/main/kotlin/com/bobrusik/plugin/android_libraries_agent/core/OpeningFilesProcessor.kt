package com.bobrusik.plugin.android_libraries_agent.core

import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants
import com.bobrusik.plugin.android_libraries_agent.helpers.PackageHelper
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.bobrusik.plugin.android_libraries_agent.model.OpenInEditorFileType
import openInEditor

class OpeningFilesProcessor {

    fun process(step: ModificationStep.OpenInEditorFiles, packageHelper: PackageHelper) {
        step.fileTypes.forEach { type ->
            when (type) {
                OpenInEditorFileType.BUILD_GRADLE_APP -> {
                    val moduleDir = packageHelper.getModulePackage()
                    val buildFile = moduleDir?.findFile(CodeGeneratorConstants.BUILD_GRADLE_FILE_NAME)
                    buildFile?.openInEditor()
                }
                OpenInEditorFileType.BUILD_GRADLE_PROJECT -> {
                    val rootDir = packageHelper.rootDir
                    val buildFile = rootDir?.findFile(CodeGeneratorConstants.BUILD_GRADLE_FILE_NAME)
                    buildFile?.openInEditor()
                }
                else -> {
                } // TODO (not implemented yet)
            }
        }
    }
}