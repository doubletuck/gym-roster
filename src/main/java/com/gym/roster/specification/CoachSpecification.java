package com.gym.roster.specification;

import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.dto.CoachFilterParams;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CoachSpecification {

    private CoachSpecification() {}

    public static Specification<Coach> build(CoachFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasValue(params.firstName())) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), like(params.firstName()), ESCAPE_CHAR));
            }
            if (hasValue(params.lastName())) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), like(params.lastName()), ESCAPE_CHAR));
            }

            if (hasValue(params.q())) {
                String pattern = like(params.q());
                Subquery<CoachRoster> collegeSub = query.subquery(CoachRoster.class);
                Root<CoachRoster> collegeRoster = collegeSub.from(CoachRoster.class);
                collegeSub.select(collegeRoster);
                collegeSub.where(
                        cb.equal(collegeRoster.get("coach"), root),
                        cb.or(
                                cb.like(cb.lower(collegeRoster.get("college").get("shortName")), pattern, ESCAPE_CHAR),
                                cb.like(cb.lower(collegeRoster.get("college").get("longName")), pattern, ESCAPE_CHAR)
                        )
                );
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern, ESCAPE_CHAR),
                        cb.like(cb.lower(root.get("lastName")), pattern, ESCAPE_CHAR),
                        cb.exists(collegeSub)
                ));
            }

            if (hasValue(params.collegeCodeName()) || params.seasonYear() != null) {
                Subquery<CoachRoster> sub = query.subquery(CoachRoster.class);
                Root<CoachRoster> roster = sub.from(CoachRoster.class);
                sub.select(roster);

                List<Predicate> rosterPredicates = new ArrayList<>();
                rosterPredicates.add(cb.equal(roster.get("coach"), root));

                if (hasValue(params.collegeCodeName())) {
                    rosterPredicates.add(cb.equal(roster.get("college").get("codeName"), params.collegeCodeName()));
                }
                if (params.seasonYear() != null) {
                    rosterPredicates.add(cb.equal(roster.get("seasonYear"), params.seasonYear()));
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

    private static final char ESCAPE_CHAR = '\\';

    private static String like(String value) {
        String escaped = value.toLowerCase().trim()
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
