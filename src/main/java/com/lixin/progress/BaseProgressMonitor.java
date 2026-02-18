package com.lixin.progress;

import lombok.RequiredArgsConstructor;

/**
 * @author lixin
 * @date 2026/2/18
 */
@RequiredArgsConstructor
public abstract class BaseProgressMonitor implements Progress {
    protected final Progress progress;

    @Override
    public int getTotal() {
        return progress.getTotal();
    }

    @Override
    public int getComplete() {
        return progress.getComplete();
    }

    @Override
    public int increment(int increment) {
        int res = progress.increment(increment);
        onCompleteChange(res);
        return res;
    }

    /**
     * 进度改变时调用
     *
     * @param complete 进度
     */
    protected abstract void onCompleteChange(int complete);
}
