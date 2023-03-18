package net.reworlds.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandController {
    private final Service service;

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

    @Command("/player")
    public void player() {
        service.player();
    }

    @Command("/id")
    public void id() {
        service.id();
    }

    @Command("/skin")
    public void skin() {
        service.skin();
    }

    @Command("/punish")
    public void punish() {
        service.punish();
    }

    @Command("/stats")
    public void stats() {
        service.stats();
    }
}
