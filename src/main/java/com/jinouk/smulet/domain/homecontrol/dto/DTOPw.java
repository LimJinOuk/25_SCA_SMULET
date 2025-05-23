package com.jinouk.smulet.domain.homecontrol.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter@Setter
public class DTOPw {
    private String pw;

    // getter & setter
    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
