package com.lixin.arrange;

import java.nio.file.Path;
import java.util.Map;

/**
 * 文件整理器
 *
 * @author lixin
 * @date 2024/5/1 12:08
 */
public interface FileArranger {

    /**
     * 文件整理
     *
     * @param fromDir  原目录
     * @param selector 文件选择器
     * @param plan     文件整理策略
     * @return ...
     */
    Map<Path, String> arrange(String fromDir, FileSelector selector, ArrangePlan plan);

    /**
     * arrange
     *
     * @param fromDir ...
     * @return ...
     **/
    Map<Path, String> arrange(String fromDir);
}
