package ru.practicum.exploreWithMe.event.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatClient {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl) {
        this.restTemplate = new RestTemplate();
        this.serverUrl = serverUrl;
    }

    public void saveStat(String app, String uri, String ip) {
        StatDto stat = StatDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        HttpEntity<StatDto> requestBody = new HttpEntity<>(stat);
        restTemplate.postForObject(serverUrl + "/hit", requestBody, StatDto.class);
    }

    public List<HitDto> getStat(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique) {
        String requestUri = serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("start", start.format(formatter));
        urlParam.put("end", end.format(formatter));
        urlParam.put("uris", String.join(",", uris));
        urlParam.put("unique", Boolean.toString(unique));

        ResponseEntity<HitDto[]> entity = restTemplate.getForEntity(requestUri, HitDto[].class, urlParam);

        return entity.getBody() != null ? Arrays.asList(entity.getBody()) : Collections.emptyList();
    }
}