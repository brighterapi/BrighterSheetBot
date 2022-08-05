package com.brighter.api.BrighterSheetBot.writer;

import com.brighter.api.BrighterSheetBot.dto.UserDto;

import java.util.Map;

public interface Writer {

    void write(UserDto userDto);

    void write(Map<String,String> input, String spreadSheetId);
}
