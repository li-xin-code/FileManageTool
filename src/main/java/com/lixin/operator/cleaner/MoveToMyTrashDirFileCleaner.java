package com.lixin.operator.cleaner;

import com.lixin.FileTool;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author lixin
 * @date 2026/2/18
 */
public class MoveToMyTrashDirFileCleaner implements FileCleaner {
    private String myTrashDir;

    @Override
    public void clean(String directory) {
        /*
        将所有文件移动到删除目录
        删除目录为 当前目录的上级目录下创建一个文件夹
        名称为当前删除目录名称+"-del"+删除时间¬
         */
        Path directoryPath = FileSystems.getDefault().getPath(directory);
        Path parDir = directoryPath.getParent();
        String trashDir;
        if (StringUtils.isNotBlank(myTrashDir)) {
            myTrashDir = FileTool.standardizedDir(myTrashDir);
            Path myTrashDirPath = FileSystems.getDefault().getPath(myTrashDir);
            trashDir = myTrashDirPath.toString();
        } else {
            Path name = parDir.getName(parDir.getNameCount() - 1);
            String deleteTime = String.valueOf(System.currentTimeMillis());
            String newDirName = name + "-del" + deleteTime;
            Path trashDirPath = parDir.resolve(newDirName);
            trashDir = trashDirPath.toString();
        }
        FileTool.recursionMove(directory, trashDir);
    }
}
