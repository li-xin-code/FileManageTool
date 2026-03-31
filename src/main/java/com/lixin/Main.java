package com.lixin;

import com.lixin.command.FileMoveCommand;
import picocli.CommandLine;

/**
 * @author lixin
 * @date 2024/5/1 16:35
 */
public class Main {
    public static void main(String... args) {
        int exitCode = new CommandLine(new FileMoveCommand()).execute(args);
        System.exit(exitCode);
    }
}
