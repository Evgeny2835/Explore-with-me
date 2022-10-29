package ru.practicum.exploreWithMe.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stat.dto.HitDto;
import ru.practicum.exploreWithMe.stat.dto.StatDto;
import ru.practicum.exploreWithMe.stat.mapper.StatMapper;
import ru.practicum.exploreWithMe.stat.model.Stat;
import ru.practicum.exploreWithMe.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final StatMapper statMapper;

    @Override
    public StatDto save(StatDto statDto) {
        log.info("Start processing a request to save statistics, data={}", statDto);
        Stat stat = statMapper.toStat(statDto);
        stat.setCreated(LocalDateTime.now());
        return statMapper.toStatDto(statRepository.save(stat));
    }

    @Override
    public List<HitDto> getAll(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique,
            String app) {
        log.info("Start of processing a request for statistics with parameters: " +
                "start={}, end={}, uris={}, unique={}, app={}", start, end, uris, unique, app);
        List<HitDto> hits = statRepository.getAll(start, end, uris, unique, app);
        hits.forEach(s -> s.setApp(app));
        return hits;
    }
}