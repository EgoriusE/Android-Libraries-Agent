package utils.extensions

import com.android.tools.idea.util.androidFacet
import com.intellij.openapi.module.Module
import org.jetbrains.android.dom.manifest.Manifest

fun Module.getPackageName(): String? {
    val manifest: Manifest? = Manifest.getMainManifest(this.androidFacet)
    return manifest?.`package`?.stringValue
}