package com.librarymgmt.service;

import com.librarymgmt.model.Member;
import com.librarymgmt.util.DatabaseHelper;
import com.librarymgmt.util.IdGenerator;

import java.sql.SQLException;
import java.util.List;

public class MemberService {
    public Member createMember(String name, String email, String phone) {
        String id = IdGenerator.generate("MB");
        Member m = new Member(id, name, email, phone);
        try {
            DatabaseHelper.saveMember(m);
            System.out.println("Member created: " + m);
            return m;
        } catch (SQLException e) {
            System.err.println("Error saving member: " + e.getMessage());
            return null;
        }
    }

    public List<Member> listAllMembers() {
        try {
            return DatabaseHelper.fetchAllMembers();
        } catch (SQLException e) {
            System.err.println("Error fetching members: " + e.getMessage());
            return List.of();
        }
    }

    public Member getMemberById(int id) {
        return null;
    }

    public boolean addMember(Member member) {
        return false;
    }

    public boolean deleteMemberById(int id) {
        return false;
    }
}

