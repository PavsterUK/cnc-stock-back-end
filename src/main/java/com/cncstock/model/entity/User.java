package com.cncstock.model.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User {
    @Id
    @NotNull

    private int rotavalID;

    private String name;

    private String surname;

    private String password;

    public User() {}
}
