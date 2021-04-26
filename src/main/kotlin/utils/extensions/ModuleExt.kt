package utils.extensions

import com.android.tools.idea.util.androidFacet
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import constants.CodeGeneratorConstants
import execRunWriteAction
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory

fun Module.getPackageName(): String? {
    val manifest: Manifest? = Manifest.getMainManifest(this.androidFacet)
    return manifest?.`package`?.stringValue
}

fun Module.getPackageDir(): PsiDirectory? {
    val packageName: String? = getPackageName()
    return if (packageName != null) {
        getSrcPackage()?.findSubdirectoryByPackageName(packageName)
    } else null
}


fun Module.getSrcPackage(): PsiDirectory? {
    val moduleDir = getModulePackage()
    return moduleDir?.findSubdirectory(CodeGeneratorConstants.SRC_FOLDER_NAME)
        ?.findSubdirectory(CodeGeneratorConstants.MAIN_SOURCE_SET_FOLDER_NAME)
        ?.findSubdirectory(CodeGeneratorConstants.JAVA_SOURCE_FOLDER_NAME)
}

fun Module.getModulePackage(): PsiDirectory? {
    val fullModuleNameList: List<String> = name.split(Char.DOT)
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
