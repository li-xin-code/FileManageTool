package com.lixin.operator.cleaner;

/**
 * @author lixin
 * @date 2026/2/18
 */
public interface FileCleaner {
    /**
     * 清理文件
     *
     * @param directory 文件目录
     */
    void clean(String directory);
}
