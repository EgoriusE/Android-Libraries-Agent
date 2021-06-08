package com.bobrusik.plugin.android_libraries_agent.helpers

import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.JAVA_SOURCE_FOLDER_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.KOTLIN_SOURCE_FOLDER_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.MAIN_SOURCE_SET_FOLDER_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.SRC_FOLDER_NAME
import com.bobrusik.plugin.android_libraries_agent.extensions.*
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import execRunWriteAction
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory

class PackageHelper(private val module: Module) {

    private val project = module.project
    val rootDir = project.guessProjectDir()?.toPsiDirectory(project)

    private val notificationFactory = project.service<NotificationsFactory>()

    fun getModulePackage(): PsiDirectory? {
        val fullModuleNameList = module.name.split(Char.DOT)
        val moduleName = fullModuleNameList[fullModuleNameList.size - 1]
        return rootDir?.findSubdirectory(moduleName)
    }

    fun getSrcPackage(): PsiDirectory? {
        val moduleDir = getModulePackage()
        val resDir = moduleDir
            ?.findSubdirectory(SRC_FOLDER_NAME)
            ?.findSubdirectory(MAIN_SOURCE_SET_FOLDER_NAME)

        return resDir?.let {
            if (resDir.hasChildDir(JAVA_SOURCE_FOLDER_NAME)) {
                resDir.findSubdirectory(JAVA_SOURCE_FOLDER_NAME)
            } else {
                resDir.findSubdirectory(KOTLIN_SOURCE_FOLDER_NAME)
            }
        }
    }

    fun getPackageDir(): PsiDirectory? {
        val packageName: String? = module.getPackageName()
        return if (packageName != null) {
            getSrcPackage()?.findSubdirectoryByPackageName(packageName)
        } else {
            null
        }
    }

    fun generatePackage(dirName: String): PsiDirectory? {
        var dir: PsiDirectory? = null

        getPackageDir()?.let { parentDir ->
            execRunWriteAction {
                dir = if (parentDir.canCreateSubdirectory(dirName)) {
                    parentDir.createSubdirectory(dirName)
                } else {
                    notificationFactory.info("Directory $dirName is already exist.")
                    parentDir.findSubdirectory(dirName)
                }
            }
        }
        return dir
    }
}