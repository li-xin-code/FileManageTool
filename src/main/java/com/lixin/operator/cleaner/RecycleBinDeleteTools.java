package com.lixin.operator.cleaner;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * @author lixin
 */
public class RecycleBinDeleteTools {

    /**
     * SHFileOperation 的结构体
     */
    public static class SHFILEOPSTRUCT extends com.sun.jna.Structure {
        public Pointer hwnd;
        public int wFunc;
        public String pFrom;
        public String pTo;
        public int fFlags;
        public boolean fAnyOperationsAborted;
        public Pointer hNameMappings;
        public String lpszProgressTitle;

        @Override
        protected java.util.List<String> getFieldOrder() {
            return java.util.Arrays.asList("hwnd", "wFunc", "pFrom", "pTo", "fFlags", "fAnyOperationsAborted", "hNameMappings", "lpszProgressTitle");
        }
    }

    /**
     * 定义需要的常量
     */
    public interface Operations {
        int FO_MOVE = 0x0001;
        int FO_COPY = 0x0002;
        int FO_DELETE = 0x0003;
        int FO_RENAME = 0x0004;
    }

    /**
     * 定义标志位
     */
    public interface Flags {
        int FOF_MULTIDESTFILES = 0x0001;
        int FOF_CONFIRMMOUSE = 0x0002;
        int FOF_SILENT = 0x0004;
        int FOF_RENAMEONCOLLISION = 0x0008;
        int FOF_NOCONFIRMATION = 0x0010; // Don't prompt the user.
        int FOF_WANTMAPPINGHANDLE = 0x0020;
        int FOF_ALLOWUNDO = 0x0040; // Use the recycle bin.
        int FOF_FILESONLY = 0x0080; // Only operate on files.
        int FOF_SIMPLEPROGRESS = 0x0100;
        int FOF_ALLOWUNDOANDCONFIRMATION = 0x0410; // Undo with confirmation
    }

    /**
     * 定义 User32
     */
    public interface Shell32 extends StdCallLibrary {
        // 加载 shell32.dll 并创建实例
        Shell32 INSTANCE = Native.load("shell32", Shell32.class, W32APIOptions.UNICODE_OPTIONS);

        /**
         * 定义 SHFileOperation
         *
         * @param fileOp ...
         * @return ...
         * @date 2026/2/18 14:09
         **/
        @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
        int SHFileOperation(SHFILEOPSTRUCT fileOp);
    }

    public static boolean moveToRecycleBin(String path) {
        SHFILEOPSTRUCT fileOp = new SHFILEOPSTRUCT();
        fileOp.wFunc = Operations.FO_DELETE;
        // 必须是以 null 结尾的双 null-terminated 字符串
        fileOp.pFrom = path + "\0";
        fileOp.fFlags = Flags.FOF_ALLOWUNDO | Flags.FOF_NOCONFIRMATION | Flags.FOF_SILENT;

        // 执行操作
        int result = Shell32.INSTANCE.SHFileOperation(fileOp);
        // 返回是否成功
        return result == 0 && !fileOp.fAnyOperationsAborted;
    }

    public static void main(String[] args) {
        String filePath = "C:\\path\\to\\file_or_folder"; // 替换为要删除的文件或文件夹路径

        boolean result = moveToRecycleBin(filePath); // 将文件或目录移动到回收站
        if (result) {
            System.out.println("文件/目录已成功移到回收站！");
        } else {
            System.out.println("操作失败！");
        }
    }
}
