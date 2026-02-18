package com.lixin.arrange;

import com.lixin.FileTool;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixin
 * @date 2026/2/18
 */
@RequiredArgsConstructor
public class FileTypeSelector implements FileSelector {
    private final String[] typeArray;

    @Override
    public List<Path> select(Collection<Path> files) {
        Set<String> types = new HashSet<>(Arrays.asList(typeArray));
        return files.stream().filter(path -> {
            String type = FileTool.getFileType(path.toString());
            return types.contains(type);
        }).collect(Collectors.toList());
    }
}
