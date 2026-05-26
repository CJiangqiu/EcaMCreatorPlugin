Each row binds one keyframe `event id` (declared inside the BossShow cutscene) to a procedure. When the cutscene reaches that keyframe tick during playback, the procedure runs server-side.

Available procedure dependencies:
- `entity` — the target entity the cutscene is anchored to
- `sourceentity` — the viewer (player watching the cutscene)
- `world`, `x`, `y`, `z` — taken from the target entity
