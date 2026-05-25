package com.gym.roster.dto;

import com.doubletuck.gym.common.model.StaffRole;
import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.util.List;

@Relation(collectionRelation = "content")
public record CoachResponse(
        Long coachId,
        Instant creationTimestamp,
        Instant lastUpdateTimestamp,
        String firstName,
        String lastName,
        List<CoachRosterEntry> rosters) {

    public record CoachRosterEntry(
            Long coachRosterId,
            String collegeCodeName,
            String collegeShortName,
            String collegeLongName,
            Short seasonYear,
            StaffRole roleCode) {
        public static CoachRosterEntry from(CoachRoster roster) {
            return new CoachRosterEntry(
                    roster.getId(),
                    roster.getCollege().getCodeName(),
                    roster.getCollege().getShortName(),
                    roster.getCollege().getLongName(),
                    roster.getSeasonYear(),
                    roster.getRoleCode());
        }
    }

    public static CoachResponse from(Coach coach, List<CoachRoster> rosters) {
        return new CoachResponse(
                coach.getId(),
                coach.getCreationTimestamp(),
                coach.getLastUpdateTimestamp(),
                coach.getFirstName(),
                coach.getLastName(),
                rosters.stream().map(CoachRosterEntry::from).toList());
    }
}
