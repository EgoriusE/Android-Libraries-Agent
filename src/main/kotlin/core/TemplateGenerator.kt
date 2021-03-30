package core

import PluginConstants.DEFAULT_TEMPLATES_DIR_NAME
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import utils.extensions.templateNameToFileName
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import org.jetbrains.kotlin.idea.KotlinFileType
import java.io.StringWriter

class TemplateGenerator(private val project: Project) {

    private val psiFileFactory by lazy {
        PsiFileFactory.getInstance(project)
    }

    private val freeMarkerConfig by lazy {
        Configuration().apply {
            setClassForTemplateLoading(TemplateGenerator::class.java, DEFAULT_TEMPLATES_DIR_NAME)
            defaultEncoding = Charsets.UTF_8.name()
            templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
            // logTemplateExceptions = false
            // wrapUncheckedExceptions = true
        }
    }

    fun generateFiles(templateNames: List<String>, model: Map<String, Any>): List<PsiFile> {
        return templateNames.map { templateName -> generateFile(templateName, model) }
    }

    fun generateFile(templateName: String, model: Map<String, Any>): PsiFile {

        val template: Template = try {
            freeMarkerConfig.getTemplate(templateName)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("Can't find template $templateName")
        }

        val templateText = StringWriter().use { writer ->
            try {
                template.process(model, writer)
                writer.buffer.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                throw UnsupportedOperationException()
            }
        }
        return psiFileFactory.createFileFromText(
            templateName.templateNameToFileName(),
            KotlinFileType.INSTANCE, templateText
        )
    }
}