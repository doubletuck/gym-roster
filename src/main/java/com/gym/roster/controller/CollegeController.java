package com.gym.roster.controller;

import com.gym.roster.domain.College;
import com.gym.roster.parser.CollegeImportResult;
import com.gym.roster.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/college")
public class CollegeController {

    private final CollegeService collegeService;

    @Autowired
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<College> findById(@PathVariable Long id) {
        return collegeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<College> create(@RequestBody College college) {
        College createdCollege = collegeService.save(college);
        return new ResponseEntity<>(createdCollege, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<College> update(@PathVariable Long id, @RequestBody College college) {
        Optional<College> existingCollege = collegeService.findById(id);
        if (existingCollege.isPresent()) {
            College updatedCollege = existingCollege.get();
            updatedCollege.setConference(college.getConference());
            updatedCollege.setCity(college.getCity());
            updatedCollege.setDivision(college.getDivision());
            updatedCollege.setRegion(college.getRegion());
            updatedCollege.setLongName(college.getLongName());
            updatedCollege.setShortName(college.getShortName());
            return ResponseEntity.ok(collegeService.save(updatedCollege));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        collegeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<College>> getPaginatedEntities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<College> colleges = collegeService.getPaginatedEntities(pageable);
        return ResponseEntity.ok(colleges);
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<CollegeImportResult>> importCollegesFromFile(@RequestParam MultipartFile file) throws Exception {
        List<CollegeImportResult> collegeImportResults = collegeService.importCollegesFromFile(file);
        return ResponseEntity.ok(collegeImportResults);
    }
}
