# SQL and PSQL Cheat Sheet

### Connect to psql
```shell
psql -h localhost -p 5432 -U postgres
```

### List databases in instance
```text
\l
```

### Connect to a database
```text
\c <database-name>
```

### Drop database
```text
DROP DATABASE <database-name>;
```

### List database tables
```text
\dt
```

### Describe table
```text
\d <table-name>
```

For additional metadata, use:
```text
\d+ <table-name>
```

### Common Queries
#### Select the rosters order by college
```postgresql
select c.code_name, r.season_year, a.first_name, a.last_name, r.class_code
from athlete a, college c, roster r
where r.athlete_id = a.id and r.college_id = c.id                                                                                               
order by c.code_name, r.season_year, a.last_name;
```