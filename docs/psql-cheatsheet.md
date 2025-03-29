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