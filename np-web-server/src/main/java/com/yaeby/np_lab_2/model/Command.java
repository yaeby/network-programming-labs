package com.yaeby.np_lab_2.model;

import lombok.Getter;

@Getter
public class Command implements Comparable<Command> {
    private final Runnable command;
    private final CommandType commandType;

    public Command(Runnable command, CommandType cmdType) {
        this.command = command;
        this.commandType = cmdType;
    }

    public void run() {
        command.run();
    }

    @Override
    public int compareTo(Command other) {
        return Integer.compare(this.commandType.getPriority(), other.commandType.getPriority());
    }
}
