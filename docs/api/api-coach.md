# Coach API

Base path: `/coach`

Manages individual coach records. A coach represents a staff member who may appear on a college roster across multiple seasons.

---

## Endpoints

### GET /coach/{id}

Returns a single coach by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Coach ID |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Coach found; returns [Coach](#coach-object) |
| 404 Not Found | No coach with the given ID |

---

### GET /coach

Returns a paginated list of coaches, each enriched with their roster history.

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Zero-based page index |
| size | int | 10 | Number of records per page |
| q | String | — | Full-text search across `firstName`, `lastName`, and affiliated college names |
| firstName | String | — | Partial match on first name (case-insensitive) |
| lastName | String | — | Partial match on last name (case-insensitive) |
| collegeCodeName | String | — | Exact match on college code name; filters to coaches with a roster entry at that college |
| seasonYear | Short | — | Filters to coaches with a roster entry in that season year |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Paginated HATEOAS result; returns `PagedModel` containing a list of [CoachResponse](#coachresponse-object) objects |

---

### POST /coach

Creates a new coach.

**Request Body** — [Coach](#coach-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 201 Created | Coach created; returns the new [Coach](#coach-object) |

---

### PUT /coach/{id}

Updates an existing coach. Replaces `firstName` and `lastName`.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Coach ID |

**Request Body** — [Coach](#coach-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Coach updated; returns the updated [Coach](#coach-object) |
| 404 Not Found | No coach with the given ID |

---

### DELETE /coach/{id}

Deletes a coach by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Coach ID |

**Responses**

| Status | Description |
|--------|-------------|
| 204 No Content | Coach deleted |

---

## Coach Object

Used as the request/response body for `POST` and `PUT` endpoints.

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| id | Long | — | Auto-generated | Unique identifier |
| firstName | String | Yes | Max 40 characters | Coach's first name |
| lastName | String | Yes | Max 40 characters | Coach's last name |
| creationTimestamp | Instant | — | Auto-set on create | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | — | Auto-set on create/update | ISO 8601 UTC timestamp of last update |

**Unique constraint:** `firstName` + `lastName`

**Example**

```json
{
  "id": 7,
  "firstName": "Alex",
  "lastName": "Rivera",
  "creationTimestamp": "2024-08-01T12:00:00Z",
  "lastUpdateTimestamp": "2024-08-01T12:00:00Z"
}
```

---

## CoachResponse Object

Returned by `GET /coach` and `GET /coach/{id}` (future). Includes the coach's full roster history.

| Field | Type | Description |
|-------|------|-------------|
| coachId | Long | Unique identifier |
| creationTimestamp | Instant | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | ISO 8601 UTC timestamp of last update |
| firstName | String | Coach's first name |
| lastName | String | Coach's last name |
| rosters | List\<CoachRosterEntry\> | All roster entries for this coach |

### CoachRosterEntry

| Field | Type | Description |
|-------|------|-------------|
| coachRosterId | Long | Roster entry identifier |
| collegeCodeName | String | College code name |
| collegeShortName | String | College short name |
| collegeLongName | String | College full name |
| seasonYear | Short | Season year |
| roleCode | StaffRole | Staff role (e.g. `HEAD_COACH`, `ASSISTANT_COACH`) |

**Example**

```json
{
  "coachId": 7,
  "creationTimestamp": "2024-08-01T12:00:00Z",
  "lastUpdateTimestamp": "2024-08-01T12:00:00Z",
  "firstName": "Alex",
  "lastName": "Rivera",
  "rosters": [
    {
      "coachRosterId": 42,
      "collegeCodeName": "UCLA",
      "collegeShortName": "UCLA",
      "collegeLongName": "University of California, Los Angeles",
      "seasonYear": 2024,
      "roleCode": "HEAD_COACH"
    }
  ]
}
```
