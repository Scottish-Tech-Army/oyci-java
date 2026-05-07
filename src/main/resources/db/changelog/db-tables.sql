-- ============================================================
--  Ochills Youth Community Improvement (OYCI)
--  Full schema DDL – PostgreSQL
--  Generated: 2026-03-30
--  Tables (in dependency order so FKs resolve):
--    designation, qualification, location,
--    staff, event_type,
--    staff_qualification, staff_holiday,
--    event_type_qualification,
--    event_instance, event_assignment
-- ============================================================


-- ------------------------------------------------------------
-- designation
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS designation
(
    id   BIGSERIAL    NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT designation_pkey PRIMARY KEY (id),
    CONSTRAINT designation_name_uq UNIQUE (name)
);

-- Seed values
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


-- ------------------------------------------------------------
-- qualification
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS qualification
(
    id   BIGSERIAL    NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT qualification_pkey PRIMARY KEY (id)
);


-- ------------------------------------------------------------
-- location
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS location
(
    id      BIGSERIAL    NOT NULL,
    name    VARCHAR(255) NOT NULL,
    address TEXT         NULL,
    CONSTRAINT location_pkey PRIMARY KEY (id)
);


-- ------------------------------------------------------------
-- staff
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS staff
(
    id                 BIGSERIAL    NOT NULL,
    name               VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    weekly_avail_hours INTEGER      NULL,
    experience_months  INTEGER      NULL,
    designation        VARCHAR(255) NULL,
    CONSTRAINT staff_pkey PRIMARY KEY (id),
    CONSTRAINT staff_email_uq UNIQUE (email)
);


-- ------------------------------------------------------------
-- event_type
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_type
(
    id                         BIGSERIAL    NOT NULL,
    name                       VARCHAR(255) NOT NULL,
    description                TEXT         NULL,
    def_dur_mins               INTEGER      NULL,
    required_experience_months INTEGER      NULL,
    CONSTRAINT event_type_pkey PRIMARY KEY (id)
);


-- ------------------------------------------------------------
-- staff_qualification  (composite PK: staff_id + qualification_id)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS staff_qualification
(
    staff_id         BIGINT NOT NULL,
    qualification_id BIGINT NOT NULL,
    CONSTRAINT staff_qualification_pkey PRIMARY KEY (staff_id, qualification_id),
    CONSTRAINT fk_sq_staff         FOREIGN KEY (staff_id)
        REFERENCES staff (id),
    CONSTRAINT fk_sq_qualification FOREIGN KEY (qualification_id)
        REFERENCES qualification (id)
);


-- ------------------------------------------------------------
-- staff_holiday
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS staff_holiday
(
    id         BIGSERIAL NOT NULL,
    staff_id   BIGINT    NOT NULL,
    start_date DATE      NOT NULL,
    end_date   DATE      NOT NULL,
    CONSTRAINT staff_holiday_pkey PRIMARY KEY (id),
    CONSTRAINT fk_sh_staff FOREIGN KEY (staff_id)
        REFERENCES staff (id)
);


-- ------------------------------------------------------------
-- event_type_qualification  (composite PK: event_type_id + qualification_id)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_type_qualification
(
    event_type_id              BIGINT  NOT NULL,
    qualification_id           BIGINT  NOT NULL,
    CONSTRAINT event_type_qualification_pkey PRIMARY KEY (event_type_id, qualification_id),
    CONSTRAINT fk_etq_event_type    FOREIGN KEY (event_type_id)
        REFERENCES event_type (id),
    CONSTRAINT fk_etq_qualification FOREIGN KEY (qualification_id)
        REFERENCES qualification (id)
);


-- ------------------------------------------------------------
-- event_instance
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_instance
(
    id                  BIGSERIAL                   NOT NULL,
    event_type_id       BIGINT                      NOT NULL,
    location_id         BIGINT                      NOT NULL,
    start_time          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    shift_start_time    TIMESTAMP WITHOUT TIME ZONE NULL,
    shift_end_time      TIMESTAMP WITHOUT TIME ZONE NULL,
    CONSTRAINT event_instance_pkey PRIMARY KEY (id),
    CONSTRAINT fk_ei_event_type FOREIGN KEY (event_type_id)
        REFERENCES event_type (id),
    CONSTRAINT fk_ei_location FOREIGN KEY (location_id)
        REFERENCES location (id)
);


-- ------------------------------------------------------------
-- event_assignment
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_assignment
(
    id                BIGSERIAL NOT NULL,
    event_instance_id BIGINT    NOT NULL,
    staff_id          BIGINT    NOT NULL,
    CONSTRAINT event_assignment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_ea_event_instance FOREIGN KEY (event_instance_id)
        REFERENCES event_instance (id),
    CONSTRAINT fk_ea_staff FOREIGN KEY (staff_id)
        REFERENCES staff (id)
);


