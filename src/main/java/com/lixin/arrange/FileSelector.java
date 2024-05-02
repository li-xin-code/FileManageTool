package com.lixin.arrange;

import com.lixin.FileTool;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixin
 * @date 2024/5/1 12:21
 */
@FunctionalInterface
public interface FileSelector {
    /**
     * 选择需要整理的文件
     *
     * @param files 文件列表
     * @return ...
     */
    List<Path> select(Collection<Path> files);

    String[] VIDEO_TYPE_ARRAY = {"mp4", "wmv", "avi", "mov", "mkv", "rmvb"};

    FileSelector VIDEO_SELECTOR = list -> {
        Set<String> types = new HashSet<>(Arrays.asList(VIDEO_TYPE_ARRAY));
        return list.stream().filter(path -> {
            String type = FileTool.getFileType(path.toString());
            return types.contains(type);
        }).collect(Collectors.toList());
    };
}
