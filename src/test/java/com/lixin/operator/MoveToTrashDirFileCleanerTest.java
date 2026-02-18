package com.lixin.operator;

import com.lixin.operator.cleaner.TrashFileCleaner;

/**
 * @author lixin
 * @date 2026/2/18
 */
class MoveToTrashDirFileCleanerTest {
    @org.junit.jupiter.api.Test
    void clean() {
        TrashFileCleaner cleaner = new TrashFileCleaner();
        cleaner.clean("/Users/lixin/Downloads/test");
    }

}