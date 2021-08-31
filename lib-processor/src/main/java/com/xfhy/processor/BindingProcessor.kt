package com.xfhy.processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.xfhy.annotation.BindView
import java.io.IOException
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement


/**
 * @author : xfhy
 * Create time : 2021/8/31 7:50 上午
 * Description : APT 注解处理器
 */
class BindingProcessor : AbstractProcessor() {

    var filer: Filer? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        filer = processingEnvironment.filer
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        for (element in roundEnv.rootElements) {
            val packageStr: String = element.enclosingElement.toString()
            val classStr: String = element.simpleName.toString()
            val className = ClassName.get(packageStr, classStr + "Binding")
            val constructorBuilder: MethodSpec.Builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(packageStr, classStr), "activity")
            var hasBinding = false
            for (enclosedElement in element.enclosedElements) {
                if (enclosedElement.kind === ElementKind.FIELD) {
                    val bindView: BindView? = enclosedElement.getAnnotation(BindView::class.java)
                    if (bindView != null) {
                        hasBinding = true
                        constructorBuilder.addStatement(
                            "activity.\$N = activity.findViewById(\$L)",
                            enclosedElement.simpleName, bindView.value
                        )
                    }
                }
            }
            val builtClass = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructorBuilder.build())
                .build()
            if (hasBinding) {
                try {
                    JavaFile.builder(packageStr, builtClass)
                        .build().writeTo(filer)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return Collections.singleton(BindView::class.java.canonicalName)
    }

}