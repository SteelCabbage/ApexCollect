package com.chinapex.analytics.aop

import com.android.build.gradle.AppExtension
import com.chinapex.analytics.aop.collect.AopSettings
import com.chinapex.analytics.aop.collect.AopTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class AopEntry implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('AopSettings', AopSettings)
        project.task('AopSettings') << {
            println project.AopSettings.msg
            println project.AopSettings.isDebug
        }

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new AopTransform())


    }
}