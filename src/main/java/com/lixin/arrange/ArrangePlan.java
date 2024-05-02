package com.lixin.arrange;

import com.lixin.FileTool;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param files ...
     * @return java.util.Map<java.io.File, java.lang.String>
     **/
    Map<Path, String> arrange(List<Path> files);

    ArrangePlan GROUP = files -> {
        Map<Path, String> map = new HashMap<>(files.size());
        for (Path path : files) {
            String parent = FileTool.getParentName(path);
            map.put(path, parent + File.separator + path.getFileName());
        }
        return map;
    };


}
