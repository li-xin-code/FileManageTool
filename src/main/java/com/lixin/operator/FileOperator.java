package com.lixin.operator;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lixin.FileTool;
import com.lixin.arrange.FileArranger;
import com.lixin.operator.cleaner.DelFileCleaner;
import com.lixin.operator.cleaner.FileCleaner;
import com.lixin.operator.cleaner.TrashFileCleaner;
import com.lixin.operator.progress.Progress;
import com.lixin.operator.progress.ProgressPrintMonitor;
import com.lixin.operator.progress.SimpleProgress;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private boolean printProgress = true;
    private boolean softDel = true;
    private boolean clearAfterMove = true;

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

    @SuppressWarnings("UnusedReturnValue")
    public Progress move(String fromDir, String toDir) {
        Map<Path, String> map = arranger.arrange(fromDir);
        Map<String, List<Path>> group = map.keySet().stream().collect(Collectors.groupingBy(FileTool::getParentName));
        CountDownLatch latch = new CountDownLatch(map.size());
        Progress progress = new SimpleProgress(map.size());
        if (printProgress) {
            progress = new ProgressPrintMonitor(progress);
        }
        final Progress finalProgress = progress;
        group.values().forEach(list -> list.forEach(file -> {
            String newPath = toDir + File.separator + map.get(file);
            threadPool.submit(() -> {
                try {
                    FileTool.move(file.toFile(), newPath);
                } finally {
                    latch.countDown();
                    finalProgress.increment();
                }
            });
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (clearAfterMove) {
            Set<String> parentDirs = map.keySet().stream()
                    .map(Path::getParent)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
            FileCleaner cleaner = softDel ? new TrashFileCleaner() : new DelFileCleaner();
            parentDirs.forEach(cleaner::clean);
        }
        return progress;
    }
}
