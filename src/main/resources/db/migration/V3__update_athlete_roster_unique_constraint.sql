ALTER TABLE athlete_roster DROP CONSTRAINT uk_athlete_roster;
ALTER TABLE athlete_roster ADD CONSTRAINT uk_athlete_roster UNIQUE (season_year, athlete_id);
