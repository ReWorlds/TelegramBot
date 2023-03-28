package net.reworlds.controller;

import lombok.AllArgsConstructor;
import net.reworlds.service.Command;
import net.reworlds.service.ServiceCommands;

@AllArgsConstructor
public class CommandController {
    private final ServiceCommands service;

    @Command(value = "/help", aliases = {"/start", "/h"})
    public void help() {
        service.help();
    }

    @Command(value = "/info", aliases = {"/i"})
    public void info() {
        service.info();
    }

    @Command(value = "/metrics", aliases = {"/m"})
    public void metrics() {
        service.metrics();
    }

    @Command(value = "/user", aliases = {"/u"})
    public void user() {
        service.user();
    }

    @Command(value = "/skin", aliases = {"/s"})
    public void skin() {
        service.skin();
    }

    @Command(value = "/account", aliases = {"/a", "/link", "/connect"})
    public void account() {
        service.account();
    }

    // Fun commands
    @Command("/coin")
    public void coin() {
        service.coin();
    }
}
