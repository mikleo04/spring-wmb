package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.MENU_API)
public class MenuController {

    private final MenuService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Menu createMenu(@RequestBody Menu menu) {
        return service.create(menu);
    }

    @GetMapping("{id}")
    public Menu getMenuById(@PathVariable String id) {
        return null;
    }

    @GetMapping
    public List<Menu> getAllMenu() {
        return null;
    }

    @PutMapping
    public Menu updateMenu(@RequestBody Menu menu) {
        return null;
    }

    @DeleteMapping("{id}")
    public String deleteMenu(@PathVariable String id) {
        return null;
    }

    @PutMapping("{id}")
    public String updateStatusMenu(@PathVariable String id, @RequestParam(name = "status") Boolean status) {
        return null;
    }

}
