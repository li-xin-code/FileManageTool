package com.lixin.command;

import com.lixin.arrange.ArrangePlan;
import com.lixin.arrange.Arranger;
import com.lixin.arrange.FileSelector;
import com.lixin.arrange.FileTypeSelector;
import com.lixin.operator.FileOperator;
import org.apache.commons.lang3.ArrayUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

import static com.lixin.arrange.FileSelector.VIDEO_TYPE_ARRAY;

/**
 * @author lixin
 * @date 2026/3/31
 */
@Command(name = "fm", description = "file move utils", mixinStandardHelpOptions = true)
public class FileMoveCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-s", "--source"}, description = "source path")
    private String source;
    @CommandLine.Option(names = {"-t", "--target"}, description = "target path")
    private String target;
    @CommandLine.Option(names = {"-ty", "--fileType"}, description = "file type")
    private String[] fileTypes;

    /**
     * 是否打印进度
     */
    @CommandLine.Option(names = {"-pp", "--printProgress"}, description = "print progress")
    private boolean printProgress = true;
    /**
     * 移动完成后是否使用软删除
     */
    @CommandLine.Option(names = {"-sd", "--softDel"}, description = "soft delete")
    private boolean softDel = true;
    /**
     * 移动完成后是否删除源目录
     */
    @CommandLine.Option(names = {"-cl", "--clearAfterMove"}, description = "clear after move")
    private boolean clearAfterMove = false;

    @Override
    public Integer call() {
        String[] types = ArrayUtils.isEmpty(fileTypes) ? VIDEO_TYPE_ARRAY : fileTypes;
        FileSelector selector = new FileTypeSelector(types);
        Arranger arranger = new Arranger(selector, ArrangePlan.GROUP);
        FileOperator operator = new FileOperator(arranger);
        operator.setClearAfterMove(clearAfterMove);
        operator.setSoftDel(softDel);
        operator.setPrintProgress(printProgress);
        operator.move(source, target);
        return 0;
    }
}
