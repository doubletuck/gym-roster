package com.gym.roster.controller;

import com.gym.roster.domain.Member;
import com.gym.roster.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> findById(@PathVariable UUID id) {
        return memberService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Member> create(@RequestBody Member member) {
        Member createdMember = memberService.save(member);
        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> update(@PathVariable UUID id, @RequestBody Member member) {
        Optional<Member> existingMember = memberService.findById(id);
        if (existingMember.isPresent()) {
            Member updatedMember = existingMember.get();
            updatedMember.setFirstName(member.getFirstName());
            updatedMember.setLastName(member.getLastName());
            return new ResponseEntity<>(memberService.save(updatedMember), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        memberService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}