package com.bobrusik.plugin.android_libraries_agent.extensions

import com.android.tools.idea.util.androidFacet
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import execRunWriteAction
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory

fun Module.getPackageName(): String? {
    val manifest = Manifest.getMainManifest(this.androidFacet)
    return manifest?.`package`?.stringValue
}

fun Module.getPackageDir(): PsiDirectory? {
    val packageName = getPackageName()
    return if (packageName != null) {
        getSrcPackage()?.findSubdirectoryByPackageName(packageName)
    } else null
}


fun Module.getSrcPackage(): PsiDirectory? {
    val moduleDir = getModulePackage()
    val resDir = moduleDir
        ?.findSubdirectory(CodeGeneratorConstants.SRC_FOLDER_NAME)
        ?.findSubdirectory(CodeGeneratorConstants.MAIN_SOURCE_SET_FOLDER_NAME)

    return resDir?.let {
        if (resDir.hasChildDir(CodeGeneratorConstants.JAVA_SOURCE_FOLDER_NAME)) {
            resDir.findSubdirectory(CodeGeneratorConstants.JAVA_SOURCE_FOLDER_NAME)
        } else {
            resDir.findSubdirectory(CodeGeneratorConstants.KOTLIN_SOURCE_FOLDER_NAME)
        }
    }
}

fun Module.getModulePackage(): PsiDirectory? {
    val fullModuleNameList = name.split(Char.DOT)
    val moduleName = fullModuleNameList[fullModuleNameList.size - 1]
    return rootDir?.findSubdirectory(moduleName)
}

fun Module.generatePackage(dirName: String): PsiDirectory? {
    var dir: PsiDirectory? = null
    execRunWriteAction { dir = getPackageDir()?.createSubdirectory(dirName) }
    return dir
}

val Module.rootDir: PsiDirectory?
    get() = project.guessProjectDir()?.toPsiDirectory(project)
