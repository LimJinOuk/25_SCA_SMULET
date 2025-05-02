package com.jinouk.smulet.domain.homecontrol.entity;

import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user")
public class user
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String pw;

    public static user tomemberentity(userdto userdto)
    {
        user user = new user();

        user.setId(userdto.getId());
        user.setEmail(userdto.getEmail());
        user.setName(userdto.getName());
        user.setPw(userdto.getPw());

        return user;
    }

}
