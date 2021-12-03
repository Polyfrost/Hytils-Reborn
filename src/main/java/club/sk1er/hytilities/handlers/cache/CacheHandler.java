package club.sk1er.hytilities.handlers.cache;

import club.sk1er.hytilities.Hytilities;
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
