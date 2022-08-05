package com.brighter.api.BrighterSheetBot.controller;

import com.brighter.api.BrighterSheetBot.dto.UserDto;
import com.brighter.api.BrighterSheetBot.writer.SheetWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SheetWriter sheetWriter;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    String addUser(@RequestBody UserDto userDto) {
        sheetWriter.write(userDto);
        return "Written";
    }

    @PostMapping(value = "/add/{sheetId}")
    String addUser(@PathVariable("sheetId") String sheetId, @RequestBody Map<String, String> input) {
        sheetWriter.write(input, sheetId);
        return "Written";
    }

}
