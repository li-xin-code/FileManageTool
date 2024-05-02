package com.lixin.operator;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lixin.FileTool;
import com.lixin.arrange.FileArranger;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.Data;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 文件操作器
 *
 * @author lixin
 * @date 2024/5/1 17:47
 */
@Data
public class FileOperator {
    private static final Logger logger = LoggerFactory.getLogger(FileOperator.class);
    private FileArranger arranger;
    private final ExecutorService threadPool;

    public FileOperator(FileArranger arranger, ExecutorService threadPool) {
        this.arranger = arranger;
        this.threadPool = threadPool;
    }

    public FileOperator(FileArranger arranger) {
        this(arranger, getDefaultThreadPool());
    }

    private static ExecutorService getDefaultThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true)
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    String msg = String.format("Thread name: %s,error: %s", thread.getName(), throwable.getMessage());
                    logger.error(msg, throwable);
                })
                .setNameFormat("pool-%d")
                .build();
        return new ThreadPoolExecutor(
                6,
                12,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    public Progress move(String fromDir, String toDir) {
        Map<Path, String> map = arranger.arrange(fromDir);
        Map<String, List<Path>> group = map.keySet().stream().collect(Collectors.groupingBy(FileTool::getParentName));
        CountDownLatch latch = new CountDownLatch(map.size());
        Progress progress = new Progress(map.size());
        group.values().forEach(list -> list.forEach(file -> {
            String newPath = toDir + File.separator + map.get(file);
            threadPool.submit(() -> {
                try {
                    FileTool.move(file.toFile(), newPath);
                } finally {
                    latch.countDown();
                    progress.getComplete().incrementAndGet();
                }
            });
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Set<String> parentDirs = map.keySet().stream()
                .map(Path::getParent)
                .map(Path::toString)
                .collect(Collectors.toSet());
        FileTool.recursionRemove(parentDirs);
        return progress;
    }
}
