package net.reworlds.controller;

import lombok.AllArgsConstructor;
import net.reworlds.service.Command;
import net.reworlds.service.ServiceCommands;

@AllArgsConstructor
public class CommandController {
    private final ServiceCommands service;

    @Command(value = "/help", aliases = {"/start"})
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

    @Command(value = "/user", aliases = {"/player"})
    public void user() {
        service.user();
    }

    @Command("/skin")
    public void skin() {
        service.skin();
    }

    @Command(value = "/account", aliases = {"/link", "/connect"})
    public void account() {
        service.account();
    }

    @Command(value = "/release", aliases = {"/version", "/update"})
    public void release() {
        service.release();
    }

    // Fun commands
    @Command("/coin")
    public void coin() {
        service.coin();
    }
}
