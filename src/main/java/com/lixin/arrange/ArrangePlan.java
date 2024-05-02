package com.lixin.arrange;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 制定整理计划
 *
 * @author lixin
 * @date 2024/5/1 12:21
 */
public interface ArrangePlan {

    /**
     * 输出文件整理后应在目录地址
     *
     * @param paths    ...
     * @param rootPath 整理目录
     * @return ...
     **/
    Map<Path, String> arrange(Collection<Path> paths, Path rootPath);

    ArrangePlan GROUP = (paths, rootPath) -> {
        Map<Path, String> map = new HashMap<>(paths.size());
        List<Path> inRootDir = paths.stream().filter(path -> path.getParent().equals(rootPath))
                .collect(Collectors.toList());
        inRootDir.forEach(path -> map.put(path,path.getFileName().toString()));
        paths.removeAll(inRootDir);
        Map<Path, List<Path>> group = paths.stream().collect(Collectors.groupingBy(Path::getParent));
        for (Path g : group.keySet()) {
            List<Path> list = group.get(g);
            if (list.size() == 1) {
                list.stream().findFirst().ifPresent(path ->
                        map.put(path, path.getFileName().toString())
                );
            }
            if (list.size() > 1) {
                Path first = list.stream().findFirst()
                        .orElseThrow(() -> new NullPointerException("has not element."));
                if (!first.startsWith(rootPath)) {
                    String errorMsg = String.format("异常路径：rootPath: %s ,filePath: %s", rootPath, first);
                    throw new RuntimeException(errorMsg);
                }
                String groupName = Optional.of(rootPath.relativize(first).getName(0))
                        .map(Path::toString).orElseThrow(() -> {
                            String errorMsg = String.format("异常路径：rootPath: %s ,filePath: %s", rootPath, first);
                            return new RuntimeException(errorMsg);
                        });
                list.forEach(path -> map.put(path, groupName + File.separator + path.getFileName()));
            }
        }
        return map;
    };


}
