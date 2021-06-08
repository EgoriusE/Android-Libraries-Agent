package com.bobrusik.plugin.android_libraries_agent.extensions

import com.intellij.psi.PsiDirectory
import com.intellij.util.IncorrectOperationException

fun PsiDirectory.canCreateSubdirectory(name: String): Boolean {
    return try {
        checkCreateSubdirectory(name)
        true
    } catch (ex: IncorrectOperationException) {
        false
    }
}

fun PsiDirectory.canCreateFile(fileName: String): Boolean {
    return try {
        checkCreateFile(fileName)
        true
    } catch (ex: IncorrectOperationException) {
        false
    }
}

fun PsiDirectory.findSubdirectoryByPackageName(packageName: String): PsiDirectory {
    val directoriesNames = packageName.split(Char.DOT)
    var result: PsiDirectory = this
    for (item in directoriesNames) {
        result = result.findSubdirectory(item)
            ?: throw RuntimeException()
    }
    return result
}

fun PsiDirectory.hasChildDir(childDirName: String): Boolean {
    return findSubdirectory(childDirName) != null
}