/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.handlers.cache;

import net.wyvest.hytilities.Hytilities;
import gg.essential.lib.caffeine.cache.Cache;
import gg.essential.lib.caffeine.cache.Caffeine;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CacheHandler<A, B> extends Handler<B> {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
            50, 50,
            0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), (r) -> new Thread(
            r,
            String.format("%s Cache Thread (Handler %s) %s", Hytilities.MOD_NAME, getClass().getSimpleName(), counter.incrementAndGet())
    )
    );

    public final Cache<A, B> cache = Caffeine.newBuilder().executor(POOL).maximumSize(100).build();
}
