package com.librarymgmt.controller;

import com.librarymgmt.model.Member;
import com.librarymgmt.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // GET /api/members
    @GetMapping
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.listAllMembers());
    }

    // GET /api/members/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Member> getById(@PathVariable int id) {
        Member m = memberService.getMemberById(id);
        if (m == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(m);
    }

    // POST /api/members
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Member member) {
        boolean ok = memberService.addMember(member);
        if (!ok) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create member");
        // if your service returns created member with id, you can return that
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    // PUT /api/members/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Member update) {
        Member existing = memberService.getMemberById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setName(update.getName());
        existing.setEmail(update.getEmail());
        existing.setPhone(update.getPhone());
        boolean ok = memberService.addMember(existing); // assume addMember upserts; change if needed
        if (!ok) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not update member");
        return ResponseEntity.ok(existing);
    }

    // DELETE /api/members/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean removed = memberService.deleteMemberById(id);
        if (!removed) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
