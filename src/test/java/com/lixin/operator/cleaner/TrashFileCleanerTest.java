package com.lixin.operator.cleaner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lixin
 * @date 2026/2/18
 */
class TrashFileCleanerTest {

    @Test
    void clean() {
        TrashFileCleaner cleaner = new TrashFileCleaner();
        cleaner.clean("/Users/lixin/Downloads/test");
    }
}