/*
 * Copyright 2022 VMware, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.appdynamics;

import io.micrometer.core.instrument.Meter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathNamingConventionTest {

    @Test
    void shouldConvertPathName() {
        AppDynamicsConfig config = AppDynamicsConfig.DEFAULT;

        PathNamingConvention victim = new PathNamingConvention(config);
        String name = victim.name("counter", Meter.Type.COUNTER);

        assertEquals(config.prefix() + "|counter", name);
    }

}
