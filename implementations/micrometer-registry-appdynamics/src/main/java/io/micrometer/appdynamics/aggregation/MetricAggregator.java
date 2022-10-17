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
package io.micrometer.appdynamics.aggregation;

import io.micrometer.core.instrument.util.TimeUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Resettable metric aggregator.<br>
 * This class may be used as a cumulative or step aggregator, and allows to retrieve a
 * snapshot at a point in time.
 *
 * @author Ricardo Veloso
 */
public final class MetricAggregator {

    private final LongAdder count = new LongAdder();

    private final LongAdder total = new LongAdder();

    private final AtomicLong min = new AtomicLong();

    private final AtomicLong max = new AtomicLong();

    // private final TimeWindowMax max;

    // MetricAggregator(Clock clock, DistributionStatisticConfig
    // distributionStatisticConfig) {
    // max = new TimeWindowMax(clock, distributionStatisticConfig);
    // }

    public long count() {
        return count.longValue();
    }

    public long min() {
        return min.longValue();
    }

    public long max() {
        return max.longValue();
    }

    public long total() {
        return total.longValue();
    }

    public void recordNonNegative(long amount) {
        if (amount >= 0) {
            count.increment();
            total.add(amount);
            // max.record(amount);
            max.updateAndGet(prev -> Math.max(prev, amount));
            min.updateAndGet(prev -> prev > 0 ? Math.min(prev, amount) : amount);
        }
    }

    public void reset() {
        synchronized (this) {
            min.set(0);
            max.set(0);
            total.reset();
            count.reset();
        }
    }

    public MetricSnapshot snapshot() {
        return new MetricSnapshot(count(), min(), max(), total());
    }

    public MetricSnapshot snapshot(TimeUnit unit) {
        return new MetricSnapshot(count(), TimeUtils.nanosToUnit(min(), unit), TimeUtils.nanosToUnit(max(), unit), TimeUtils.nanosToUnit(total(), unit));
    }

}
