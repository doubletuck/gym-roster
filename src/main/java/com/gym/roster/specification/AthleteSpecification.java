package com.gym.roster.specification;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.dto.AthleteFilterParams;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AthleteSpecification {

    private AthleteSpecification() {}

    public static Specification<Athlete> build(AthleteFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasValue(params.firstName())) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), like(params.firstName())));
            }
            if (hasValue(params.lastName())) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), like(params.lastName())));
            }
            if (hasValue(params.homeCity())) {
                predicates.add(cb.like(cb.lower(root.get("homeCity")), like(params.homeCity())));
            }
            if (hasValue(params.homeState())) {
                State state = State.find(params.homeState());
                if (state == null) {
                    throw new IllegalArgumentException("Invalid homeState: " + params.homeState());
                }
                predicates.add(cb.equal(root.get("homeState"), state));
            }
            if (hasValue(params.homeCountry())) {
                Country country = Country.find(params.homeCountry());
                if (country == null) {
                    throw new IllegalArgumentException("Invalid homeCountry: " + params.homeCountry());
                }
                predicates.add(cb.equal(root.get("homeCountry"), country));
            }
            if (hasValue(params.clubName())) {
                predicates.add(cb.like(cb.lower(root.get("clubName")), like(params.clubName())));
            }

            if (hasValue(params.collegeCodeName()) || params.seasonYear() != null) {
                Subquery<AthleteRoster> sub = query.subquery(AthleteRoster.class);
                Root<AthleteRoster> roster = sub.from(AthleteRoster.class);
                sub.select(roster);

                List<Predicate> rosterPredicates = new ArrayList<>();
                rosterPredicates.add(cb.equal(roster.get("athlete"), root));

                if (hasValue(params.collegeCodeName())) {
                    rosterPredicates.add(cb.equal(roster.get("college").get("codeName"), params.collegeCodeName()));
                }
                if (params.seasonYear() != null) {
                    rosterPredicates.add(cb.equal(roster.get("seasonYear"), params.seasonYear()));
                }
                if (hasValue(params.academicYear())) {
                    AcademicYear academicYear = AcademicYear.find(params.academicYear());
                    if (academicYear == null) {
                        throw new IllegalArgumentException("Invalid academicYear: " + params.academicYear());
                    }
                    rosterPredicates.add(cb.equal(roster.get("academicYear"), academicYear));
                }

                sub.where(rosterPredicates.toArray(new Predicate[0]));
                predicates.add(cb.exists(sub));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    private static String like(String value) {
        return "%" + value.toLowerCase().trim() + "%";
    }
}
