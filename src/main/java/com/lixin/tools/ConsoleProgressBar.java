package com.lixin.tools;

import java.io.PrintStream;

/**
 * @author lixin
 */
public class ConsoleProgressBar {
    /**
     * 进度条总长度
     */
    private final int total;
    /**
     * 当前进度
     */
    private volatile int current = 0;
    /**
     * 进度条长度
     */
    private final int barLength;

    private final PrintStream out;

    private static final int DEFAULT_BAR_LENGTH = 50;
    private static final int MIN_BAR_LENGTH = 20;
    private static final int MAX_BAR_LENGTH = 100;

    public ConsoleProgressBar(int total) {
        this(total, DEFAULT_BAR_LENGTH);
    }

    public ConsoleProgressBar(int total, int barLength) {
        this(total, barLength, System.out);
    }

    public ConsoleProgressBar(int total, int barLength, PrintStream out) {
        if (total <= 0) {
            throw new IllegalArgumentException("Total must be greater than 0");
        }
        if (barLength < MIN_BAR_LENGTH) {
            throw new IllegalArgumentException("Bar length must be at least 10");
        }

        this.total = total;
        this.barLength = Math.min(barLength, MAX_BAR_LENGTH);
        this.out = out;
    }

    /**
     * 设置当前进度
     *
     * @param current 设定当前进度量
     */
    public synchronized void setCurrent(int current) {
        if (current < 0 || current > total) {
            throw new IllegalArgumentException("Current progress must be in range [0, total]");
        }
        this.current = current;
        printProgress();
    }

    /**
     * 增加当前进度
     *
     * @param increment 增量
     */
    @SuppressWarnings("unused")
    public synchronized void increment(int increment) {
        setCurrent(this.current + increment);
    }

    /**
     * 打印进度条或百分比
     */
    private void printProgress() {
        // 计算进度百分比
        float progress = (float) current / total;
        // 根据进度计算完成的块数
        int completedBars = (int) (progress * barLength);

        StringBuilder sb = new StringBuilder();
        sb.append("\r[");
        for (int i = 0; i < completedBars; i++) {
            sb.append("=");
        }
        for (int i = completedBars; i < barLength; i++) {
            sb.append(" ");
        }
        sb.append("] ");

        // 显示百分比
        sb.append(String.format("%.2f%%", progress * 100));

        out.print(sb);
        if (current == total) {
            out.println();
        }
    }
}
