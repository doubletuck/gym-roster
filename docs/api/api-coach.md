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

Returns a paginated list of coaches.

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Zero-based page index |
| size | int | 10 | Number of records per page |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Paginated result; returns a Spring Data `Page` containing a list of [Coach](#coach-object) objects |

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
