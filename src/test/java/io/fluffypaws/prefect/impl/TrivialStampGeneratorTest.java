/*
 *  Copyright 2016 FluffyPaws Inc. (http://www.fluffypaws.io)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.fluffypaws.prefect.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;

import io.fluffypaws.prefect.api.Stamp;
import static org.assertj.core.api.Assertions.assertThat;

public class TrivialStampGeneratorTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder();
    private TemporaryObjectStore objectStore = new TemporaryObjectStore(temporaryFolder);

    @Rule
    public TestRule chain = RuleChain.outerRule(temporaryFolder).around(objectStore);

    @Test
    public void test_generation_speed() {
        /* The "problem" with the mechanism of using the regular clock which produces milli-second precision Instant
         * objects and then "appending" some nano-seconds is that in theory you can generate more Stamps than "fit"
         * in the available nano part. So let's test that. Is it possible to generate more than 999.999 stamps in a
         * millisecond? This test should fail when it's possible to generate more than 100.000 in a millisecond to be
         * on the safe side.
         */
        Instant start = Instant.now();

        final int count = 100*1000;
        Stamp[] stamps = new Stamp[count];
        for (int i = 0; i < count; i++) {
            stamps[i] = objectStore.getKeyValueStore().generateStamp();
        }

        Instant end = Instant.now();
        long gap = ChronoUnit.MILLIS.between(start, end);

        assertThat(gap).isGreaterThan(1);
    }

}