/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.changedetection.rules

import org.gradle.api.internal.changedetection.state.FileCollectionSnapshot
import org.gradle.api.internal.changedetection.state.FileCollectionSnapshotter
import org.gradle.api.internal.changedetection.state.OutputFilesCollectionSnapshotter
import org.gradle.api.internal.changedetection.state.TaskHistoryRepository
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.internal.classloader.ClassLoaderHierarchyHasher
import spock.lang.Subject

@Subject(TaskUpToDateState)
class TaskUpToDateStateTest extends AbstractTaskStateChangesTest {
    private TaskHistoryRepository.History stubHistory
    private OutputFilesCollectionSnapshotter stubOutputFileSnapshotter
    private FileCollectionSnapshotter stubInputFileSnapshotter
    private FileCollectionFactory fileCollectionFactory = Mock(FileCollectionFactory)
    private classLoaderHierarchyHasher = Mock(ClassLoaderHierarchyHasher)

    def setup() {
        this.stubHistory = Stub(TaskHistoryRepository.History)
        this.stubOutputFileSnapshotter = Stub(OutputFilesCollectionSnapshotter)
        this.stubInputFileSnapshotter = Stub(FileCollectionSnapshotter)
    }

    def "constructor invokes snapshots" () {
        setup:
        FileCollectionSnapshot stubSnapshot = Stub(FileCollectionSnapshot)
        OutputFilesCollectionSnapshotter mockOutputFileSnapshotter = Mock(OutputFilesCollectionSnapshotter)
        FileCollectionSnapshotter mockInputFileSnapshotter = Mock(FileCollectionSnapshotter)

        when:
        new TaskUpToDateState(stubTask, stubHistory, mockOutputFileSnapshotter, mockInputFileSnapshotter, fileCollectionFactory, classLoaderHierarchyHasher)

        then:
        noExceptionThrown()
        1 * mockInputs.getProperties() >> [:]
        1 * mockInputs.getFileProperties() >> fileProperties(prop: "a")
        1 * mockOutputs.getFileProperties() >> fileProperties(out: "b")
        1 * mockOutputFileSnapshotter.snapshot(_, _, _) >> stubSnapshot
        1 * mockInputFileSnapshotter.snapshot(_, _, _) >> stubSnapshot
    }
}
