package com.lixin.arrange;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author lixin
 * @date 2024/5/2 00:26
 */

public class ArrangerTest {
    private static final Logger logger = LoggerFactory.getLogger(ArrangerTest.class);

    @Test
    void testArrange() {
        FileArranger arranger = new Arranger(FileSelector.VIDEO_SELECTOR, ArrangePlan.GROUP);
        Map<Path, String> map = arranger.arrange("/Volumes/disk/collection");
        map.forEach((k, v) -> {
            String s = k.toString() + "-" + v;
            logger.info(s);
//            System.out.println(s);
        });
    }
}