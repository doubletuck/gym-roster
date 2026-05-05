# Roster API

Base paths: `/roster/athlete`, `/roster/coach`

Manages team rosters for a given season. An athlete roster entry places an athlete on a college team for a specific season year. A coach roster entry places a coach on a college staff for a specific season year.

---

## Athlete Roster Endpoints

### POST /roster/athlete/file-import

Imports athlete roster entries from an uploaded file. Creates athlete and roster records that do not already exist.

**Request** — `multipart/form-data`

| Parameter | Type | Description |
|-----------|------|-------------|
| file | MultipartFile | File containing athlete roster records to import |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Import complete; returns a list of [AthleteRosterImportResult](#athleterosterimportresult-object) |

---

### POST /roster/athlete/directory-import

Imports athlete roster entries from all eligible files in a server-side directory.

**Request** — `multipart/form-data`

| Parameter | Type | Description |
|-----------|------|-------------|
| directoryPath | String | Absolute path to the directory on the server |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Import complete; returns `true` |

---

## Coach Roster Endpoints

### POST /roster/coach

Creates a new coach roster entry.

**Request Body** — [CoachRoster](#coachroster-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Roster entry created; returns the new [CoachRoster](#coachroster-object) |

---

### GET /roster/coach/{id}

Returns a single coach roster entry by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | CoachRoster ID |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Entry found; returns [CoachRoster](#coachroster-object) |
| 404 Not Found | No entry with the given ID |

---

### GET /roster/coach/{seasonYear}/{collegeCodeName}

Returns all coach roster entries for a college in a given season.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| seasonYear | Short | Four-digit season year (e.g. `2024`) |
| collegeCodeName | String | College code name (e.g. `UCLA`) |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Returns a list of [CoachRoster](#coachroster-object) objects (empty list if none found) |

---

### DELETE /roster/coach/{id}

Deletes a coach roster entry by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | CoachRoster ID |

**Responses**

| Status | Description |
|--------|-------------|
| 204 No Content | Entry deleted |

---

### DELETE /roster/coach/{seasonYear}/{collegeCodeName}

Deletes all coach roster entries for a college in a given season.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| seasonYear | Short | Four-digit season year (e.g. `2024`) |
| collegeCodeName | String | College code name (e.g. `UCLA`) |

**Responses**

| Status | Description |
|--------|-------------|
| 204 No Content | Entries deleted |

---

### POST /roster/coach/file-import

Imports coach roster entries from an uploaded file. Creates coach and roster records that do not already exist.

**Request** — `multipart/form-data`

| Parameter | Type | Description |
|-----------|------|-------------|
| file | MultipartFile | File containing coach roster records to import |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Import complete; returns a list of [CoachRosterImportResult](#coachrosterimportresult-object) |
| 500 Internal Server Error | Unexpected error during import |

---

### POST /roster/coach/directory-import

Imports coach roster entries from all eligible files in a server-side directory.

**Request** — `multipart/form-data`

| Parameter | Type | Description |
|-----------|------|-------------|
| directoryPath | String | Absolute path to the directory on the server |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Import complete; returns `true` |
| 500 Internal Server Error | Unexpected error during import |

---

## Data Objects

### AthleteRoster Object

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| id | Long | — | Auto-generated | Unique identifier |
| college | College | Yes | — | The college this roster entry belongs to |
| seasonYear | Short | Yes | — | Four-digit season year (e.g. `2024`) |
| athlete | Athlete | Yes | — | The athlete on this roster |
| academicYear | String | Yes | Academic year enum code | Athlete's academic standing (e.g. `FRESHMAN`, `SOPHOMORE`, `JUNIOR`, `SENIOR`) |
| events | String | No | Max 20 characters | Event codes the athlete competes in (e.g. `VAULT`, `BARS`) |
| creationTimestamp | Instant | — | Auto-set on create | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | — | Auto-set on create/update | ISO 8601 UTC timestamp of last update |

**Unique constraint:** `seasonYear` + `college` + `athlete`

---

### CoachRoster Object

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| id | Long | — | Auto-generated | Unique identifier |
| college | College | Yes | — | The college this roster entry belongs to |
| seasonYear | Short | Yes | — | Four-digit season year (e.g. `2024`) |
| coach | Coach | Yes | — | The coach on this roster |
| roleCode | String | Yes | StaffRole enum code | The coach's staff role (e.g. `HEAD_COACH`, `ASSISTANT_COACH`) |
| creationTimestamp | Instant | — | Auto-set on create | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | — | Auto-set on create/update | ISO 8601 UTC timestamp of last update |

**Unique constraint:** `seasonYear` + `college` + `coach`

**Example**

```json
{
  "id": 10,
  "college": { "id": 1, "codeName": "UCLA", ... },
  "seasonYear": 2024,
  "coach": { "id": 7, "firstName": "Alex", "lastName": "Rivera", ... },
  "roleCode": "HEAD_COACH",
  "creationTimestamp": "2024-08-01T12:00:00Z",
  "lastUpdateTimestamp": "2024-08-01T12:00:00Z"
}
```

---

### AthleteRosterImportResult Object

Returned for each row processed during an athlete roster file import.

| Field | Type | Description |
|-------|------|-------------|
| fileName | String | Name of the source file |
| recordNumber | Long | Row number within the file |
| athleteImportStatus | String | Status for the athlete record: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR` |
| rosterImportStatus | String | Status for the roster record: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR` |
| roster | AthleteRoster | The roster entry that was created or updated; `null` on error |
| message | String | Human-readable status or error message |

---

### CoachRosterImportResult Object

Returned for each row processed during a coach roster file import.

| Field | Type | Description |
|-------|------|-------------|
| fileName | String | Name of the source file |
| recordNumber | Long | Row number within the file |
| coachImportStatus | String | Status for the coach record: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR` |
| rosterImportStatus | String | Status for the roster record: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR` |
| roster | CoachRoster | The roster entry that was created or updated; `null` on error |
| message | String | Human-readable status or error message |
