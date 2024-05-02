package com.lixin.operator;

import com.lixin.arrange.ArrangePlan;
import com.lixin.arrange.Arranger;
import com.lixin.arrange.FileSelector;
import org.junit.jupiter.api.Test;

/**
 * @author lixin
 * @date 2024/5/2 11:59
 */
class FileOperatorTest {

    @Test
    void move() {
        Arranger arranger = new Arranger(FileSelector.VIDEO_SELECTOR, ArrangePlan.GROUP);
        FileOperator operator = new FileOperator(arranger);
        operator.move("", "");
    }
}