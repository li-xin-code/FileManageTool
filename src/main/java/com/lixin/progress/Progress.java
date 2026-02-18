package com.lixin.progress;

/**
 * @author lixin
 * @date 2026/2/18
 */
public interface Progress {
    /**
     * 总量
     *
     * @return 总量
     */
    int getTotal();

    /**
     * 已完成
     *
     * @return 已完成
     */
    int getComplete();

    /**
     * 增加已完成
     *
     * @param increment 增量
     * @return 增加后的已完成
     */
    int increment(int increment);

    /**
     * 增加已完成
     *
     * @return 添加后的已完成
     */
    default int increment() {
        return increment(1);
    }
}
