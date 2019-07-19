/*
 * Copyright 2019 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.vlsi.gradle.release.dsl

import com.github.vlsi.gradle.release.BuildLicenseCopySpec
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.* // ktlint-disable

fun CopySpec.dependencyLicenses(task: BuildLicenseCopySpec) {
    from(task) // dependency
    with(task.copySpec)
}

fun Project.licensesCopySpec(
    source: Any,
    noticeFile: String? = "$rootDir/NOTICE",
    action: Action<in CopySpec>? = null
): BuildLicenseCopySpec {
    if (source !is TaskProvider<*>) {
        throw GradleException("'source' must be TaskProvider<*>")
    }
    return tasks.create<BuildLicenseCopySpec>("${source.name}CopySpec") {
        dependsOn(source)
    }.apply {
        noticeFile?.let { copySpec.from(it) }
        copySpec.from(source)
        action?.execute(copySpec)
    }
}
