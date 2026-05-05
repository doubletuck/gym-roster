# Meet API

Base path: `/meet`

Manages gymnastics meet records and scores. A meet represents a competition event where college teams compete and athletes receive scores across individual events.

---

## Endpoints

### POST /meet/file-import

Imports meet data, team scores, and individual athlete scores from an uploaded file.

**Request** — `multipart/form-data`

| Parameter | Type | Description |
|-----------|------|-------------|
| file | MultipartFile | File containing meet and score records to import |

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Import complete; returns a list of [MeetImportResult](#meetimportresult-object) |
| 500 Internal Server Error | Unexpected error during import |

---

## Data Objects

### Meet Object

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| id | Long | — | Auto-generated | Unique identifier |
| eventDate | LocalDate | Yes | — | Date the meet took place (ISO 8601 format, e.g. `2024-02-10`) |
| eventName | String | Yes | Max 200 characters | Name of the meet event |
| location | String | No | Max 100 characters | Venue or city where the meet was held |
| creationTimestamp | Instant | — | Auto-set on create | ISO 8601 UTC timestamp of record creation |
| lastUpdateTimestamp | Instant | — | Auto-set on create/update | ISO 8601 UTC timestamp of last update |

**Example**

```json
{
  "id": 5,
  "eventDate": "2024-02-10",
  "eventName": "Pac-12 Invitational",
  "location": "Los Angeles, CA",
  "creationTimestamp": "2024-08-01T12:00:00Z",
  "lastUpdateTimestamp": "2024-08-01T12:00:00Z"
}
```

---

### MeetImportResult Object

Returned for each row processed during a meet file import.

| Field | Type | Description |
|-------|------|-------------|
| fileName | String | Name of the source file |
| recordNumber | Long | Row number within the file |
| importStatus | String | One of: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR` |
| meetScore | MeetScore | The meet score record that was created or updated; `null` on error |
| message | String | Human-readable status or error message |

**Example**

```json
[
  {
    "fileName": "2024-pac12-invitational.csv",
    "recordNumber": 1,
    "importStatus": "CREATED",
    "meetScore": { ... },
    "message": null
  },
  {
    "fileName": "2024-pac12-invitational.csv",
    "recordNumber": 2,
    "importStatus": "ERROR",
    "meetScore": null,
    "message": "Athlete not found on roster for the given season"
  }
]
```
