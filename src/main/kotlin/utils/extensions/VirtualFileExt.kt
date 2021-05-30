package utils.extensions

import com.intellij.json.JsonLanguage
import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile.isJsonFile(): Boolean = extension!!.toUpperCase() == JsonLanguage.INSTANCE.displayName