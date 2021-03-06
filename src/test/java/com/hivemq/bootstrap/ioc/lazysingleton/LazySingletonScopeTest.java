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

package com.hivemq.bootstrap.ioc.lazysingleton;

import com.google.inject.Scope;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * @author Dominik Obermaier
 */
public class LazySingletonScopeTest {

    @Test
    public void test_lazy_singleton_scope_is_singleton() throws Exception {
        final Scope scope1 = LazySingletonScope.get();
        final Scope scope2 = LazySingletonScope.get();

        assertSame(scope1, scope2);
    }
}