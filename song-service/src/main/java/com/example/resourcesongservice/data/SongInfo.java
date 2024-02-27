package com.example.resourcesongservice.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "song_metadata")
@Getter
@Setter
@RequiredArgsConstructor
public class SongInfo {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String artist;

    @Column
    private String album;

    @Column
    private String length;

    @Column
    private Long resourceId;

    @Column
    private String year;
}
