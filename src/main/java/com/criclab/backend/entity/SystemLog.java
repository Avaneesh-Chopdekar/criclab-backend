package com.criclab.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "system_log")
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    @Lob
    private String description;
}
