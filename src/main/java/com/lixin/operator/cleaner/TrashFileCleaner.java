package com.lixin.operator.cleaner;

import com.apple.eio.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author lixin
 * @date 2026/2/18
 */
public class TrashFileCleaner implements FileCleaner {
    private static final Logger logger = LoggerFactory.getLogger(TrashFileCleaner.class);

    @Override
    public void clean(String directory) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.startsWith("mac os")) {
                // macOS: 移动到废纸篓
                FileManager.moveToTrash(new File(directory));
            } else if (os.contains("windows")) {
                // Windows: 移动到回收站 使用jna 删除目录
                RecycleBinDeleteTools.moveToRecycleBin(directory);
            } else {
                MoveToMyTrashDirFileCleaner moveToMyTrashDirFileCleaner = new MoveToMyTrashDirFileCleaner();
                moveToMyTrashDirFileCleaner.clean(directory);
            }
        } catch (Exception e) {
            logger.error("Failed to move directory to trash: {}", directory, e);
            throw new RuntimeException(e);
        }
    }
}
