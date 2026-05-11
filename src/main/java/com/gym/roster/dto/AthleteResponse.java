package com.gym.roster.dto;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.util.List;

@Relation(collectionRelation = "content")
public record AthleteResponse(
        Long athleteId,
        Instant creationTimestamp,
        Instant lastUpdateTimestamp,
        String firstName,
        String lastName,
        String homeCity,
        State homeState,
        Country homeCountry,
        String clubName,
        List<AthleteRosterEntry> rosters) {
    public record AthleteRosterEntry(
            Long athleteRosterId,
            String collegeCodeName,
            String collegeShortName,
            String collegeLongName,
            Short seasonYear,
            AcademicYear academicYear) {
        public static AthleteRosterEntry from(AthleteRoster roster) {
            return new AthleteRosterEntry(
                    roster.getId(),
                    roster.getCollege().getCodeName(),
                    roster.getCollege().getShortName(),
                    roster.getCollege().getLongName(),
                    roster.getSeasonYear(),
                    roster.getAcademicYear());
        }
    }

    public static AthleteResponse from(Athlete athlete, List<AthleteRoster> rosters) {
        return new AthleteResponse(
                athlete.getId(),
                athlete.getCreationTimestamp(),
                athlete.getLastUpdateTimestamp(),
                athlete.getFirstName(),
                athlete.getLastName(),
                athlete.getHomeCity(),
                athlete.getHomeState(),
                athlete.getHomeCountry(),
                athlete.getClubName(),
                rosters.stream().map(AthleteRosterEntry::from).toList());
    }
}
