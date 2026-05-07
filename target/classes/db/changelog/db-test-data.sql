-- ============================================================
--  OYCI – Test / Seed Data
--  PostgreSQL
--  Generated: 2026-03-30
-- ============================================================

-- ------------------------------------------------------------
-- designation  (9 values)
-- ------------------------------------------------------------
INSERT INTO designation (name)
SELECT 'Admin'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Admin');

INSERT INTO designation (name)
SELECT 'Session Support Worker'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Session Support Worker');

INSERT INTO designation (name)
SELECT 'Youth Worker'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Youth Worker');

INSERT INTO designation (name)
SELECT 'Chief Officer'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Chief Officer');

INSERT INTO designation (name)
SELECT 'Office Manager'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Office Manager');

INSERT INTO designation (name)
SELECT 'AMC'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'AMC');

INSERT INTO designation (name)
SELECT 'Drama'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Drama');

INSERT INTO designation (name)
SELECT 'Sports'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Sports');

INSERT INTO designation (name)
SELECT 'Technology'
WHERE NOT EXISTS (SELECT 1 FROM designation WHERE name = 'Technology');


-- ------------------------------------------------------------
-- qualification  (sample values for event type requirements)
-- ------------------------------------------------------------
INSERT INTO qualification (name)
SELECT 'Drama'
WHERE NOT EXISTS (SELECT 1 FROM qualification WHERE name = 'Drama');

INSERT INTO qualification (name)
SELECT 'ASC'
WHERE NOT EXISTS (SELECT 1 FROM qualification WHERE name = 'ASC');

INSERT INTO qualification (name)
SELECT 'Sports'
WHERE NOT EXISTS (SELECT 1 FROM qualification WHERE name = 'Sports');

INSERT INTO qualification (name)
SELECT 'Technology expert'
WHERE NOT EXISTS (SELECT 1 FROM qualification WHERE name = 'Technology expert');


-- ------------------------------------------------------------
-- event_type  (8 values with full column data)
-- ------------------------------------------------------------
INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Volunteering', 'Community volunteering activities', 120, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Volunteering');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Walk', 'Outdoor walking sessions for health and wellbeing', 90, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Walk');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'ASC', 'After School Club activities', 180, 12
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'ASC');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Drama', 'Drama and theater workshops for young people', 120, 24
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Drama');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'FFF', 'Fun, Friends and Food social gathering', 150, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'FFF');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Junior Youth Club', 'Youth club sessions for younger members', 120, 12
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Junior Youth Club');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Hi5', 'High-energy sports and activities', 90, 6
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Hi5');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Stress Free Sundays', 'Relaxed Sunday activities and social time', 180, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Stress Free Sundays');


-- ------------------------------------------------------------
-- event_type_qualification  (qualification requirements for select event types)
-- ------------------------------------------------------------

-- Junior Youth Club requires First Aid and Safeguarding
INSERT INTO event_type_qualification (event_type_id, qualification_id)
SELECT et.id, q.id
FROM event_type et, qualification q
WHERE et.name = 'Youth Theatre' AND q.name = 'Drama'
  AND NOT EXISTS (
    SELECT 1 FROM event_type_qualification etq
    WHERE etq.event_type_id = et.id AND etq.qualification_id = q.id
);

-- Hi5 requires First Aid and Coaching License
INSERT INTO event_type_qualification (event_type_id, qualification_id)
SELECT et.id, q.id
FROM event_type et, qualification q
WHERE et.name = 'Gaming' AND q.name = 'Sports'
  AND NOT EXISTS (
    SELECT 1 FROM event_type_qualification etq
    WHERE etq.event_type_id = et.id AND etq.qualification_id = q.id
);

--INSERT INTO event_type_qualification (event_type_id, qualification_id)
--SELECT et.id, q.id
--FROM event_type et, qualification q
--WHERE et.name = 'Hi5' AND q.name = 'Technology expert'
--  AND NOT EXISTS (
--    SELECT 1 FROM event_type_qualification etq
--    WHERE etq.event_type_id = et.id AND etq.qualification_id = q.id
--);

-- ASC requires Safeguarding
INSERT INTO event_type_qualification (event_type_id, qualification_id)
SELECT et.id, q.id
FROM event_type et, qualification q
WHERE et.name = 'ASC' AND q.name = 'ASC'
  AND NOT EXISTS (
    SELECT 1 FROM event_type_qualification etq
    WHERE etq.event_type_id = et.id AND etq.qualification_id = q.id
);

-- ============================================================
--  Additional seed data
-- ============================================================

-- ------------------------------------------------------------
-- location
-- ------------------------------------------------------------
INSERT INTO location (name, address)
SELECT 'London', '10 Downing Street, London, SW1A 2AA'
WHERE NOT EXISTS (SELECT 1 FROM location WHERE name = 'London');

INSERT INTO location (name, address)
SELECT 'Tillicoultry', 'Town Hall, Tillicoultry, FK13 6AA'
WHERE NOT EXISTS (SELECT 1 FROM location WHERE name = 'Tillicoultry');

-- ------------------------------------------------------------
-- qualification  (new entries for the new event types)
-- ------------------------------------------------------------
INSERT INTO qualification (name)
SELECT 'First Aid'
WHERE NOT EXISTS (SELECT 1 FROM qualification WHERE name = 'First Aid');

INSERT INTO qualification (name)
SELECT 'Safeguarding'
WHERE NOT EXISTS (SELECT 1 FROM qualification WHERE name = 'Safeguarding');

-- ------------------------------------------------------------
-- event_type  (new entries)
-- Youth Theatre  – requires Drama qualification
-- Gaming         – no qualification requirement
-- Volunteering   – 24 months experience required (no extra qualification)
-- FFF            – no requirement (already exists but guard with NOT EXISTS)
-- Evening Walk   – no requirement
-- Saturday Birdwatch – no requirement
-- ------------------------------------------------------------
INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Youth Theatre', 'Drama and performing arts sessions for young people', 120, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Youth Theatre');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Gaming', 'Gaming and esports sessions for young people', 90, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Gaming');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Evening Walk', 'Guided evening walking sessions', 90, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Evening Walk');

INSERT INTO event_type (name, description, def_dur_mins, required_experience_months)
SELECT 'Saturday Birdwatch', 'Saturday morning bird watching activity', 120, NULL
WHERE NOT EXISTS (SELECT 1 FROM event_type WHERE name = 'Saturday Birdwatch');

-- Update Volunteering required_experience_months to 24 if it exists with NULL
UPDATE event_type SET required_experience_months = 24
WHERE name = 'Volunteering' AND (required_experience_months IS NULL OR required_experience_months != 24);

-- ------------------------------------------------------------
-- event_type_qualification  (new entries)
-- Youth Theatre requires Drama qualification
-- ASC requires ASC qualification  (already inserted above, guard again)
-- ------------------------------------------------------------
INSERT INTO event_type_qualification (event_type_id, qualification_id)
SELECT et.id, q.id
FROM event_type et, qualification q
WHERE et.name = 'Youth Theatre' AND q.name = 'Drama'
  AND NOT EXISTS (
    SELECT 1 FROM event_type_qualification etq
    WHERE etq.event_type_id = et.id AND etq.qualification_id = q.id
);

-- ------------------------------------------------------------
-- staff  (Joe, Bill, Kirsty, Daniel)
-- ------------------------------------------------------------
INSERT INTO staff (name, email, weekly_avail_hours, experience_months, designation)
SELECT 'Joe Smith', 'joe.smith@oyci.org', 35, 24, 'Youth Worker'
WHERE NOT EXISTS (SELECT 1 FROM staff WHERE email = 'joe.smith@oyci.org');

INSERT INTO staff (name, email, weekly_avail_hours, experience_months, designation)
SELECT 'Bill Jones', 'bill.jones@oyci.org', 28, 18, 'Session Support Worker'
WHERE NOT EXISTS (SELECT 1 FROM staff WHERE email = 'bill.jones@oyci.org');

INSERT INTO staff (name, email, weekly_avail_hours, experience_months, designation)
SELECT 'Kirsty MacDonald', 'kirsty.macdonald@oyci.org', 32, 36, 'Youth Worker'
WHERE NOT EXISTS (SELECT 1 FROM staff WHERE email = 'kirsty.macdonald@oyci.org');

INSERT INTO staff (name, email, weekly_avail_hours, experience_months, designation)
SELECT 'Daniel Robertson', 'daniel.robertson@oyci.org', 20, 12, 'Session Support Worker'
WHERE NOT EXISTS (SELECT 1 FROM staff WHERE email = 'daniel.robertson@oyci.org');

-- ------------------------------------------------------------
-- event_instance  (two events)
--
-- Event 1: Youth Theatre @ Tillicoultry
--   Date: 25 Mar 2026, 14:30–16:30, shift 14:00–17:00
--
-- Event 2: Walk @ London
--   Date: 28 Mar 2026, 17:30–19:30, shift 17:00–20:00
-- ------------------------------------------------------------
INSERT INTO event_instance (event_type_id, location_id, start_time, end_time, shift_start_time, shift_end_time)
SELECT et.id, l.id,
       TIMESTAMP '2026-03-25 14:30:00',
       TIMESTAMP '2026-03-25 16:30:00',
       TIMESTAMP '2026-03-25 14:00:00',
       TIMESTAMP '2026-03-25 17:00:00'
FROM event_type et, location l
WHERE et.name = 'Youth Theatre' AND l.name = 'Tillicoultry'
  AND NOT EXISTS (
    SELECT 1 FROM event_instance ei
    WHERE ei.event_type_id = et.id
      AND ei.start_time = TIMESTAMP '2026-03-25 14:30:00'
);

INSERT INTO event_instance (event_type_id, location_id, start_time, end_time, shift_start_time, shift_end_time)
SELECT et.id, l.id,
       TIMESTAMP '2026-03-28 17:30:00',
       TIMESTAMP '2026-03-28 19:30:00',
       TIMESTAMP '2026-03-28 17:00:00',
       TIMESTAMP '2026-03-28 20:00:00'
FROM event_type et, location l
WHERE et.name = 'Walk' AND l.name = 'London'
  AND NOT EXISTS (
    SELECT 1 FROM event_instance ei
    WHERE ei.event_type_id = et.id
      AND ei.start_time = TIMESTAMP '2026-03-28 17:30:00'
);

-- ------------------------------------------------------------
-- event_assignment
--
-- Youth Theatre (25 Mar) → Joe, Bill
-- Walk (28 Mar)          → Kirsty, Bill
-- ------------------------------------------------------------

-- Youth Theatre → Joe
INSERT INTO event_assignment (event_instance_id, staff_id)
SELECT ei.id, s.id
FROM event_instance ei
JOIN event_type et ON et.id = ei.event_type_id
JOIN staff s ON s.email = 'joe.smith@oyci.org'
WHERE et.name = 'Youth Theatre'
  AND ei.start_time = TIMESTAMP '2026-03-25 14:30:00'
  AND NOT EXISTS (
    SELECT 1 FROM event_assignment ea
    WHERE ea.event_instance_id = ei.id AND ea.staff_id = s.id
);

-- Youth Theatre → Bill
INSERT INTO event_assignment (event_instance_id, staff_id)
SELECT ei.id, s.id
FROM event_instance ei
JOIN event_type et ON et.id = ei.event_type_id
JOIN staff s ON s.email = 'bill.jones@oyci.org'
WHERE et.name = 'Youth Theatre'
  AND ei.start_time = TIMESTAMP '2026-03-25 14:30:00'
  AND NOT EXISTS (
    SELECT 1 FROM event_assignment ea
    WHERE ea.event_instance_id = ei.id AND ea.staff_id = s.id
);

-- Walk → Kirsty
INSERT INTO event_assignment (event_instance_id, staff_id)
SELECT ei.id, s.id
FROM event_instance ei
JOIN event_type et ON et.id = ei.event_type_id
JOIN staff s ON s.email = 'kirsty.macdonald@oyci.org'
WHERE et.name = 'Walk'
  AND ei.start_time = TIMESTAMP '2026-03-28 17:30:00'
  AND NOT EXISTS (
    SELECT 1 FROM event_assignment ea
    WHERE ea.event_instance_id = ei.id AND ea.staff_id = s.id
);

-- Walk → Bill
INSERT INTO event_assignment (event_instance_id, staff_id)
SELECT ei.id, s.id
FROM event_instance ei
JOIN event_type et ON et.id = ei.event_type_id
JOIN staff s ON s.email = 'bill.jones@oyci.org'
WHERE et.name = 'Walk'
  AND ei.start_time = TIMESTAMP '2026-03-28 17:30:00'
  AND NOT EXISTS (
    SELECT 1 FROM event_assignment ea
    WHERE ea.event_instance_id = ei.id AND ea.staff_id = s.id
);

