# College API

Base path: `/college`

Manages college program records. A college represents a university or collegiate program that fields a gymnastics team.

---

## Endpoints

### GET /college/{id}

Returns a single college by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | College ID |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | College found; returns [College](#college-object) |
| 404 Not Found | No college with the given ID |

---

### GET /college

Returns a paginated list of colleges.

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Zero-based page index |
| size | int | 10 | Number of records per page |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Paginated result; returns a Spring Data `Page` containing a list of [College](#college-object) objects |

---

### POST /college

Creates a new college.

**Request Body** — [College](#college-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 201 Created | College created; returns the new [College](#college-object) |

---

### PUT /college/{id}

Updates an existing college. Replaces `conference`, `city`, `division`, `region`, `longName`, and `shortName`.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | College ID |

**Request Body** — [College](#college-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | College updated; returns the updated [College](#college-object) |
| 404 Not Found | No college with the given ID |

---

### DELETE /college/{id}

Deletes a college by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | College ID |

**Responses**

| Status | Description |
|--------|-------------|
| 204 No Content | College deleted |

---

### POST /college/file-import

Imports colleges from an uploaded file.

**Request** — `multipart/form-data`

| Parameter | Type | Description |
|-----------|------|-------------|
| file | MultipartFile | File containing college records to import |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Import complete; returns a list of [CollegeImportResult](#collegeimportresult-object) |

---

## College Object

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| id | Long | — | Auto-generated | Unique identifier |
| codeName | String | Yes | Max 20 characters; unique | Short identifier code used in imports and lookups (e.g. `UCLA`) |
| shortName | String | Yes | Max 30 characters | Abbreviated display name (e.g. `UCLA`) |
| longName | String | Yes | Max 100 characters | Full institutional name (e.g. `University of California, Los Angeles`) |
| city | String | Yes | Max 50 characters | City where the college is located |
| state | String | Yes | 2-character state code (e.g. `CA`) | State where the college is located |
| conference | String | Yes | Conference enum code | Athletic conference (e.g. `BIG_TEN`, `SEC`) |
| division | String | Yes | Division enum code | NCAA division |
| region | String | Yes | Region enum code | Geographic region used for rankings |
| nickname | String | No | Max 20 characters | Team nickname (e.g. `Bruins`) |
| teamUrl | String | No | — | URL for the college's gymnastics team page |
| creationTimestamp | Instant | — | Auto-set on create | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | — | Auto-set on create/update | ISO 8601 UTC timestamp of last update |

**Unique constraint:** `codeName`

**Example**

```json
{
  "id": 1,
  "codeName": "UCLA",
  "shortName": "UCLA",
  "longName": "University of California, Los Angeles",
  "city": "Los Angeles",
  "state": "CA",
  "conference": "BIG_TEN",
  "division": "DI",
  "region": "WEST",
  "nickname": "Bruins",
  "teamUrl": "https://uclabruins.com/sports/gymnastics",
  "creationTimestamp": "2024-08-01T12:00:00Z",
  "lastUpdateTimestamp": "2024-08-01T12:00:00Z"
}
```

---

## CollegeImportResult Object

Returned for each row processed during a file import.

| Field | Type | Description |
|-------|------|-------------|
| fileName | String | Name of the source file |
| recordNumber | Long | Row number within the file |
| collegeCodeName | String | Code name of the college in this row |
| importStatus | String | One of: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR` |
| college | College | The college record that was created or updated; `null` on error |
| message | String | Human-readable status or error message |

**Example**

```json
[
  {
    "fileName": "colleges.csv",
    "recordNumber": 1,
    "collegeCodeName": "UCLA",
    "importStatus": "CREATED",
    "college": { ... },
    "message": null
  },
  {
    "fileName": "colleges.csv",
    "recordNumber": 2,
    "collegeCodeName": "UF",
    "importStatus": "EXISTS",
    "college": { ... },
    "message": "College already exists"
  }
]
```
