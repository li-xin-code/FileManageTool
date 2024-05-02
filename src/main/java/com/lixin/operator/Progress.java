package com.lixin.operator;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 操作进度
 *
 * @author lixin
 * @date 2024/5/1 18:35
 */
@Data
@RequiredArgsConstructor
public class Progress {
    /**
     * 总量
     */
    private final Integer total;
    /**
     * 已完成
     */
    private final AtomicInteger complete = new AtomicInteger(0);
}
