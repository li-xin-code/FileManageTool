package com.lixin.progress;

import com.lixin.tools.ConsoleProgressBar;

/**
 * @author lixin
 * @date 2026/2/18
 */
public class ProgressPrintMonitor extends BaseProgressMonitor {
    public ProgressPrintMonitor(Progress progress) {
        super(progress);
    }

    @Override
    protected void onCompleteChange(int complete) {
        int total = super.progress.getTotal();
        ConsoleProgressBar bar = new ConsoleProgressBar(total);
        bar.setCurrent(complete);
    }
}
