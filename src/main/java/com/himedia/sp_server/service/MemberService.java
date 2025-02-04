package com.himedia.sp_server.service;

import com.himedia.sp_server.dao.MemberDao;
import com.himedia.sp_server.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    MemberDao mdao;

    public Member getMember(String userid) {
        return mdao.getMember( userid);
    }

    public void insertMember(Member member) {
        mdao.insertMember(member);
    }

    public void updateMember(Member member) {
        mdao.updateMember(member);
    }

    public void deleteMember(String userid) {
        mdao.deleteMember(userid);
    }
}
