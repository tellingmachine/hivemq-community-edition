/*
 * Copyright 2019 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hivemq.extensions.loader;

import com.google.common.collect.ImmutableList;
import com.hivemq.extensions.HiveMQExtensions;
import com.hivemq.extensions.HiveMQPluginEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Christoph Schäbel
 */
@SuppressWarnings("NullabilityAnnotations")
public class PluginLifecycleHandlerImplTest {

    private PluginLifecycleHandlerImpl pluginLifecycleHandler;

    @Mock
    ScheduledExecutorService pluginStartStopExecutor;

    @Mock
    HiveMQExtensions hiveMQExtensions;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        pluginLifecycleHandler = new PluginLifecycleHandlerImpl(hiveMQExtensions, pluginStartStopExecutor);
    }

    @Test
    public void test_handlePluginEvents_enable() {

        final ImmutableList<HiveMQPluginEvent> events = ImmutableList.of(
                new HiveMQPluginEvent(HiveMQPluginEvent.Change.ENABLE, "test-extension",
                        temporaryFolder.getRoot().toPath()));

        pluginLifecycleHandler.handlePluginEvents(events);

        final ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(pluginStartStopExecutor, times(1)).execute(runnableArgumentCaptor.capture());
        runnableArgumentCaptor.getValue().run();

        verify(hiveMQExtensions).extensionStart(eq("test-extension"));
    }

    @Test
    public void test_handlePluginEvents_disable() {

        final ImmutableList<HiveMQPluginEvent> events = ImmutableList.of(
                new HiveMQPluginEvent(HiveMQPluginEvent.Change.DISABLE, "test-extension",
                        temporaryFolder.getRoot().toPath()));

        pluginLifecycleHandler.handlePluginEvents(events);

        final ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(pluginStartStopExecutor, times(1)).execute(runnableArgumentCaptor.capture());
        runnableArgumentCaptor.getValue().run();

        verify(hiveMQExtensions).extensionStop(eq("test-extension"), eq(true));
    }

    @Test
    public void test_stop_plugin() {

        pluginLifecycleHandler.pluginStop("test-extension");

        final ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(pluginStartStopExecutor, times(1)).execute(runnableArgumentCaptor.capture());
        runnableArgumentCaptor.getValue().run();

        verify(hiveMQExtensions).extensionStop(eq("test-extension"), eq(false));
    }

}