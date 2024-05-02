package com.lixin.arrange;

import com.lixin.FileTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @author lixin
 * @date 2024/5/1 12:33
 */
@Slf4j
public abstract class AbstractFileArranger implements FileArranger {

    @Override
    public Map<Path, String> arrange(String fromDir, FileSelector selector, ArrangePlan plan) {
        fromDir = FileTool.standardizedDir(fromDir);
        File file = new File(fromDir);
        if (!file.exists() || file.isFile()) {
            return Collections.emptyMap();
        }
        Path fromDirPath = file.toPath();
        Set<Path> allFile = getAllFile(fromDirPath);
        List<Path> select = selector.select(allFile);
        return plan.arrange(select, fromDirPath);
    }

    protected Set<Path> getAllFile(Path path) {
        FileVisitor visitor = new FileVisitor();
        try {
            Files.walkFileTree(path, visitor);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return visitor.getPaths();
    }

    private static class FileVisitor extends SimpleFileVisitor<Path> {
        @Getter
        private final Set<Path> paths;

        public FileVisitor() {
            super();
            this.paths = new HashSet<>();
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            this.paths.add(path);
            return super.visitFile(path, attrs);
        }
    }

}
