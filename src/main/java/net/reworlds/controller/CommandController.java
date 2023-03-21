package net.reworlds.controller;

import lombok.AllArgsConstructor;
import net.reworlds.service.Command;
import net.reworlds.service.ServiceCommands;

@AllArgsConstructor
public class CommandController {
    private final ServiceCommands service;

    @Command("/start")
    public void start() {
        service.help();
    }

    @Command("/help")
    public void help() {
        service.help();
    }

    @Command("/info")
    public void info() {
        service.info();
    }

    @Command("/metrics")
    public void metrics() {
        service.metrics();
    }

    @Command("/user")
    public void user() {
        service.user();
    }

    @Command("/skin")
    public void skin() {
        service.skin();
    }

    // Fun commands
    @Command("/coin")
    public void coin() {
        service.coin();
    }
}
