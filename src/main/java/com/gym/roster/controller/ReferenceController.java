package com.gym.roster.controller;

import com.doubletuck.gym.common.model.StaffRole;
import com.gym.roster.dto.ReferenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/reference")
public class ReferenceController {

    @GetMapping("/staffrole")
    public ResponseEntity<List<ReferenceResponse>> getStaffRoles() {
        List<ReferenceResponse> roles = Arrays.stream(StaffRole.values())
                .map(role -> new ReferenceResponse(role.name(), role.getLongName()))
                .toList();
        return ResponseEntity.ok(roles);
    }
}
