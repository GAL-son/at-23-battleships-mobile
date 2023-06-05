package com.battleships.server.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="\"Player\"")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accSeq")
    @SequenceGenerator(name="accSeq", initialValue = 1, allocationSize = 1)
    @Column(name="\"PlayerId\"", unique = true, nullable = false)
    private Integer uid;

    @Column(name="\"Name\"")
    private String login;

    @JsonIgnore
    @Column(name="\"Password\"")
    private String password;

    @JsonIgnore
    @Column(name="\"EmailAddress\"")
    private String email;

    @Column(name="\"RankingScore\"")
    private float gamerScore;   
}
