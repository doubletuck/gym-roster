# Athlete API

Base path: `/athlete`

Manages individual athlete records. An athlete represents a person who competes and may appear on a college roster across multiple seasons.

---

## Endpoints

### GET /athlete/{id}

Returns a single athlete by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Athlete ID |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Athlete found; returns [Athlete](#athlete-object) |
| 404 Not Found | No athlete with the given ID |

---

### GET /athlete

Returns a paginated, filtered list of athletes. All filter parameters are optional and may be combined freely. Omitting all filter parameters returns all athletes.

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Zero-based page index |
| size | int | 10 | Number of records per page |
| firstName | String | — | Partial, case-insensitive match on first name |
| lastName | String | — | Partial, case-insensitive match on last name |
| homeCity | String | — | Partial, case-insensitive match on home city |
| homeState | String | — | Exact match on home state code (e.g. `CA`, `TX`) |
| homeCountry | String | — | Exact match on home country code (e.g. `USA`, `CAN`) |
| clubName | String | — | Partial, case-insensitive match on club name |
| collegeCodeName | String | — | Exact match on college code name; returns athletes who appeared on that college's roster in any season |
| seasonYear | Short | — | Filters to athletes who appeared on any roster in this season year (e.g. `2024`) |
| academicYear | String | — | Filters by academic standing (e.g. `SR`, `Junior`); requires `seasonYear` |

**Validation**

- `academicYear` requires `seasonYear` to be present; omitting `seasonYear` while providing `academicYear` returns `400 Bad Request`
- Invalid values for `homeState`, `homeCountry`, or `academicYear` return `400 Bad Request`

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Paginated result; returns a HATEOAS `PagedModel` containing a list of [Athlete](#athlete-object) objects under the `content` key |
| 400 Bad Request | Invalid filter parameter value |

---

### POST /athlete

Creates a new athlete.

**Request Body** — [Athlete](#athlete-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 201 Created | Athlete created; returns the new [Athlete](#athlete-object) |

---

### PUT /athlete/{id}

Updates an existing athlete. Replaces `clubName`, `firstName`, `lastName`, `homeCountry`, `homeState`, and `homeCity`.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Athlete ID |

**Request Body** — [Athlete](#athlete-object) (JSON)

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Athlete updated; returns the updated [Athlete](#athlete-object) |
| 404 Not Found | No athlete with the given ID |

---

### DELETE /athlete/{id}

Deletes an athlete by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Athlete ID |

**Responses**

| Status | Description |
|--------|-------------|
| 204 No Content | Athlete deleted |

---

## Athlete Object

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| id | Long | — | Auto-generated | Unique identifier |
| firstName | String | Yes | Max 40 characters | Athlete's first name |
| lastName | String | Yes | Max 40 characters | Athlete's last name |
| homeCity | String | Yes | Max 50 characters | Athlete's home city |
| homeState | String | No | 2-character state code (e.g. `CA`, `TX`) | Athlete's home state |
| homeCountry | String | No | 3-character country code (e.g. `USA`, `CAN`) | Athlete's home country |
| clubName | String | No | Max 100 characters | Name of the club team the athlete trains with |
| creationTimestamp | Instant | — | Auto-set on create | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | — | Auto-set on create/update | ISO 8601 UTC timestamp of last update |

**Unique constraint:** `firstName` + `lastName` + `homeCity`

**Example**

```json
{
  "id": 42,
  "firstName": "Jordan",
  "lastName": "Smith",
  "homeCity": "Austin",
  "homeState": "TX",
  "homeCountry": "USA",
  "clubName": "Texas Twisters",
  "creationTimestamp": "2024-08-01T12:00:00Z",
  "lastUpdateTimestamp": "2024-08-01T12:00:00Z"
}
```
