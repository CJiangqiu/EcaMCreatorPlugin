Each row binds one `event_id` from the BossShow event track to a procedure. When playback reaches that event cue, the procedure runs server-side. Subtitle and screen-effect tracks are handled directly by ECA and need no mapping here.

Available procedure dependencies:
- `entity` — the target entity the cutscene is anchored to
- `sourceentity` — the viewer (player watching the cutscene)
- `world`, `x`, `y`, `z` — taken from the target entity
