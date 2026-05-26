# Reference API

Base path: `/reference`

Provides read-only reference data (enumerated values) for use by front-end clients.

---

## Endpoints

### GET /reference/staffrole

Returns the list of all valid staff role codes and their display names.

**Responses**

| Status | Description |
|--------|-------------|
| 200 OK | Returns a list of [ReferenceResponse](#referenceresponse-object) |

---

## ReferenceResponse Object

| Field | Type | Description |
|-------|------|-------------|
| codeName | String | Enum constant name used as the code value (e.g. `HEAD_COACH`) |
| longName | String | Human-readable display name (e.g. `Head Coach`) |

**Example**

```json
[
  { "codeName": "HEAD_COACH", "longName": "Head Coach" },
  { "codeName": "ACTING_HEAD_COACH", "longName": "Acting Head Coach" },
  { "codeName": "ASSOC_HEAD_COACH", "longName": "Associate Head Coach" },
  { "codeName": "ASST_HEAD_COACH", "longName": "Assistant Head Coach" },
  { "codeName": "FIRST_ASST_COACH", "longName": "First Assistant Coach" },
  { "codeName": "ASST_COACH", "longName": "Assistant Coach" },
  { "codeName": "ACTING_ASST_COACH", "longName": "Acting Assistant Coach" },
  { "codeName": "INTERIM_ASST_COACH", "longName": "Interim Assistant Coach" },
  { "codeName": "GRADUATE_ASST_COACH", "longName": "Graduate Student Assistant Coach" },
  { "codeName": "GRADUATE_COACH", "longName": "Graduate Student Coach" },
  { "codeName": "STUDENT_ASST_COACH", "longName": "Student Assistant Coach" },
  { "codeName": "STUDENT_COACH", "longName": "Student Coach" },
  { "codeName": "UNDERGRAD_ASST_COACH", "longName": "Undergraduate Student Assistant Coach" },
  { "codeName": "UNDERGRAD_COACH", "longName": "Undergraduate Student Coach" },
  { "codeName": "VOLUNTEER_ASST_COACH", "longName": "Volunteer Assistant Coach" },
  { "codeName": "VOLUNTEER_COACH", "longName": "Volunteer Coach" }
]
```
