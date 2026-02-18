package com.lixin.operator.cleaner;

import com.lixin.FileTool;

/**
 * @author lixin
 * @date 2026/2/18
 */
public class DelFileCleaner implements FileCleaner {
    @Override
    public void clean(String directory) {
        FileTool.recursionRemove(directory);
    }
}
