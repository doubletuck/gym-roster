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

Returns a paginated list of athletes.

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Zero-based page index |
| size | int | 10 | Number of records per page |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Paginated result; returns a HATEOAS `PagedModel` containing a list of [Athlete](#athlete-object) objects under the `content` key |

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
