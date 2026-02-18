package com.lixin.operator;

import com.lixin.arrange.ArrangePlan;
import com.lixin.arrange.Arranger;
import com.lixin.arrange.FileSelector;
import com.lixin.arrange.FileTypeSelector;
import org.junit.jupiter.api.Test;

/**
 * @author lixin
 * @date 2024/5/2 11:59
 */
class FileOperatorTest {

    @Test
    void move() {
//        FileSelector selector = FileSelector.VIDEO_SELECTOR;
        String[] types ={"xlsx"};
        FileSelector selector = new FileTypeSelector(types);
        Arranger arranger = new Arranger(selector, ArrangePlan.GROUP);
        FileOperator operator = new FileOperator(arranger);
        operator.move("/Users/lixin/Downloads", "/Users/lixin/test");
    }
}