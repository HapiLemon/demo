package org.example.controller;

import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keminfeng
 */
@RequestMapping("/api/event")
@RestController
public class Controller {

    @Autowired
    private EventService eventService;

    @GetMapping("test/{method}")
    public void test(@PathVariable String method) {
        if ("palletize".equals(method)) {
            eventService.palletize();
        }
    }

}
