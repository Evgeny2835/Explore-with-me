package ru.practicum.exploreWithMe.stat.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stat")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false)
    private String uri;

    @Column(length = 28, nullable = false)
    private String ip;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime created;
}