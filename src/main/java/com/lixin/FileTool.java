package com.lixin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Set;

/**
 * @author lixin
 * @date 2024/5/1 13:00
 */
public final class FileTool {

    private FileTool() {
        throw new AssertionError("No com.lixin.FileTool instances for you!");
    }

    private static final Logger logger = LoggerFactory.getLogger(FileTool.class);

    public static void move(File sourceFile, String targetFilePath) {
        Objects.requireNonNull(sourceFile);
        if (!sourceFile.exists()) {
            return;
        }
        if (sourceFile.getAbsolutePath().equals(targetFilePath)) {
            return;
        }

        Path sourcePath = sourceFile.toPath();
        Path targetPath = FileSystems.getDefault().getPath(targetFilePath);
        File targetDir = targetPath.getParent().toFile();
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                logger.error("creat dir fail: {}", targetDir.getAbsolutePath());
            }
        }
        if (check(sourcePath, targetPath.getParent())) {
            return;
        }
        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES);
            logger.info("success {} move to {}", sourcePath, targetPath);
        } catch (IOException e) {
            logger.error("fail：{} move to {}", sourcePath, targetPath);
            logger.error("fail： ", e);
        }
    }

    private static boolean check(Path sourcePath, Path targetPath) {
        try {
            if (Objects.equals(Files.getFileStore(sourcePath), Files.getFileStore(targetPath))) {
                return true;
            } else {
                // 获取目标地址的可用空间
                File targetFile = targetPath.toFile();
                long availableSpace = targetFile.getFreeSpace();
                // 获取源文件的大小
                long sourceSize = sourcePath.toFile().length();
                if (availableSpace < sourceSize) {
                    logger.error("目标地址空间不足");
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public static String standardizedDir(String dir) {
        return dir.endsWith(File.separator) ? dir : dir + File.separator;
    }

    public static String getFileType(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1 || index + 1 > filename.length()) {
            return "";
        }
        return filename.substring(index + 1).toLowerCase();
    }

    public static String getFileType(File file) {
        return getFileType(file.getName());
    }

    public static String getParentName(File file) {
        String parent = file.getParent();
        parent = parent.substring(parent.lastIndexOf(File.separator));
        return parent;
    }

    public static String getParentName(Path path) {
        String parent = path.getParent().toString();
        parent = parent.substring(parent.lastIndexOf(File.separator));
        return parent;
    }


    /**
     * 递归删除目录（删除目录以及目录下所有文件）
     *
     * @param dirs ...
     **/
    public static void recursionRemove(Set<String> dirs) {
        dirs.forEach(FileTool::recursionRemove);
    }

    private static final SimpleFileVisitor<Path> DELETE_VISITOR = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            logger.info("delete file: {}", file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            logger.info("delete directory: {}", dir);
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    public static void recursionRemove(String directoryPath) {
        // 删除目录及其子目录和文件
        try {
            Path path = FileSystems.getDefault().getPath(directoryPath);
            if (path.toFile().exists()) {
                Files.walkFileTree(path, DELETE_VISITOR);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
