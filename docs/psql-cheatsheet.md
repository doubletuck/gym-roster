# PSQL Cheat Sheet

### Connect to psql
```shell
psql -h localhost -p 5432 -U postgres
```

### List of databases in instance
```text
\l
```

Example:
```text
postgres=# \l
```

### Connect to a database
```text
\c <database-name>
```

Example:
```text
postgres=# \c gymroster 
```

### Drop database
```text
DROP DATABASE <database-name>;
```

Example:
```text
drop database gymroster;    
```