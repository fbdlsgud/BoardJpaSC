package com.himedia.sp_server.dto;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberDTO extends User {
    public MemberDTO(String username, String password, String name, String email, String phone, Timestamp indate, String provider, String sns_id, String sns_user, List<String> roleNames) {
        super(
                username,
                password,
                roleNames.stream().map(
                        str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList())
                );
        this.userid = username;
        System.out.println(username +  password);
        this.pwd=password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.indate = indate;
        this.provider = provider;
        this.sns_id = sns_id;
        this.sns_user = sns_user;
    }
    private String userid;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private Timestamp indate;
    private String provider;
    private String sns_id;
    private String sns_user;

    // jwt 토큰 생성시에 그 안에 넣을 개인 정보들을  Map 형식으로 구성합니다
    // 암호화 jwt 토근 생성시에 그 Map을 통채로 암호화합니다
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("userid", userid);
        dataMap.put("pwd", pwd);
        dataMap.put("email", email);
        dataMap.put("name", name);
        dataMap.put("phone", phone);
        dataMap.put("indate", indate);
        dataMap.put("provider", provider);
        dataMap.put("sns_id", sns_id);
        dataMap.put("sns_user", sns_user);
        return dataMap;

    }
}
