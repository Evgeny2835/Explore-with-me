package ru.practicum.exploreWithMe.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stat.dto.HitDto;
import ru.practicum.exploreWithMe.stat.dto.StatDto;
import ru.practicum.exploreWithMe.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public StatDto save(@RequestBody StatDto statDto) {
        log.info("Request to save statistics is sent for processing, data={}", statDto);
        return statService.save(statDto);
    }

    @GetMapping("/stats")
    public List<HitDto> getAll(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "ewm-main-service") String app) {
        log.info("Request for statistics is sent for processing, start={}, end={}, uris={}, unique={}, app={}",
                start, end, uris, unique, app);
        return statService.getAll(start, end, uris, unique, app);
    }
}