package com.bobrusik.plugin.android_libraries_agent.core

import com.bobrusik.plugin.android_libraries_agent.constants.PluginConstants.DEFAULT_TEMPLATES_DIR_NAME
import com.bobrusik.plugin.android_libraries_agent.extensions.templateNameToFileName
import com.bobrusik.plugin.android_libraries_agent.model.FileModel
import com.bobrusik.plugin.android_libraries_agent.model.GeneratedFileModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import org.jetbrains.kotlin.idea.KotlinFileType
import java.io.StringWriter

@Service
class TemplateGenerator(
    private val project: Project
) {

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

    fun generateFiles(files: List<FileModel>): List<GeneratedFileModel> {
        return files.map { fileModel ->
            GeneratedFileModel(
                psiFile = generateFile(fileModel),
                isOpenInEditor = fileModel.isOpenInEditor
            )
        }
    }

    private fun generateFile(fileModel: FileModel): PsiFile {
        val template: Template = try {
            freeMarkerConfig.getTemplate(fileModel.name)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("Can't find template $fileModel")
        }

        val templateText = StringWriter().use { writer ->
            try {
                template.process(fileModel.templateModel, writer)
                writer.buffer.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                throw UnsupportedOperationException()
            }
        }
        return psiFileFactory.createFileFromText(
            fileModel.name.templateNameToFileName(),
            KotlinFileType.INSTANCE, templateText
        )
    }
}