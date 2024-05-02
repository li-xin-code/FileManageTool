package com.lixin.arrange;

import lombok.AllArgsConstructor;

import java.nio.file.Path;
import java.util.Map;

/**
 * 分组整理
 *
 * @author lixin
 * @date 2024/5/1 16:46
 */
@AllArgsConstructor
public class Arranger extends AbstractFileArranger {
    private final FileSelector selector;
    private final ArrangePlan plan;

    @Override
    public Map<Path, String> arrange(String fromDir) {
        return super.arrange(fromDir, selector, plan);
    }
}
