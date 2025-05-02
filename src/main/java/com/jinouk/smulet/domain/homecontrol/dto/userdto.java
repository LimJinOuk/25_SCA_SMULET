package com.jinouk.smulet.domain.homecontrol.dto;


import com.jinouk.smulet.domain.homecontrol.entity.user;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class userdto {
    private int id;
    private String name;
    private String email;
    private String pw;

    public static userdto tomemberdto(user user)
    {
        userdto userdto = new userdto();

        userdto.setId(user.getId());
        userdto.setName(user.getName());
        userdto.setEmail(user.getEmail());
        userdto.setPw(user.getPw());

        return userdto;
    }
}
