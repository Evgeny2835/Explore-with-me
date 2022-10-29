package ru.practicum.exploreWithMe.stat.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stat.dto.HitDto;
import ru.practicum.exploreWithMe.stat.model.Stat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.join;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StatRepositoryImpl implements StatRepository {
    public static final String TABLE_NAME = "stat";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Stat save(Stat stat) {

        log.info("Start recording statistics in the repository, data={}", stat);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_NAME).usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("app", stat.getApp())
                .addValue("uri", stat.getUri())
                .addValue("ip", stat.getIp())
                .addValue("created", stat.getCreated());
        Number id = jdbcInsert.executeAndReturnKey(parameters);
        stat.setId(id.intValue());
        return stat;
    }

    @Override
    public List<HitDto> getAll(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique,
            String app) {

        log.info("Getting statistics in the repository by parameters: start={}, end={}, uris={}, unique={}, app={}",
                start, end, uris, unique, app);

        String sql = format("SELECT uri, COUNT(%s) count " +
                        "FROM %s " +
                        "WHERE created>? AND created<? AND app=? %s " +
                        "GROUP BY uri",
                unique ? "DISTINCT ip" : "*",
                TABLE_NAME,
                uris == null ? "" : "AND uri IN(?)"
        );

        if (uris == null) {
            return jdbcTemplate.query(sql, this::mapRowToStatDto, start, end, app);
        }
        return jdbcTemplate.query(sql, this::mapRowToStatDto, start, end, app, join(", ", uris));
    }

    private HitDto mapRowToStatDto(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() == 0) return null;
        return HitDto.builder()
                .uri(rs.getString("uri"))
                .hits(rs.getInt("count"))
                .build();
    }
}