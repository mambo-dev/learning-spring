CREATE TABLE IF NOT EXISTS smp (
    type        INTEGER   NOT NULL,
    datetime    TIMESTAMP NOT NULL,
    value       NUMERIC   NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'UTC'),
    modified_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'UTC'),
    CONSTRAINT smp_pk PRIMARY KEY (type, datetime)
);
CREATE INDEX IF NOT EXISTS smp_idx_01 ON smp (datetime DESC);

------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION "smp$upsert"(v_type INTEGER, v_datetime TIMESTAMP, v_value DOUBLE PRECISION)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
    now    TIMESTAMP := now() at time zone 'UTC';
    result INTEGER   := 0;
BEGIN
    INSERT INTO smp (type, datetime, value) VALUES (v_type, v_datetime, v_value)
        ON CONFLICT (type, datetime)
        DO UPDATE SET (value, modified_at) = (v_value, now);
    GET DIAGNOSTICS result = ROW_COUNT;
    RETURN result;
END;
$$;

------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION "smp$upsert"(v_type INTEGER, v_values JSONB)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
    now    TIMESTAMP := now() at time zone 'UTC';
    result INTEGER   := 0;
BEGIN
    INSERT INTO smp (type, datetime, value)
    SELECT v_type                                     AS type,
           TO_TIMESTAMP(t.datetime / 1000)::TIMESTAMP AS datetime,
            t.value
    FROM JSONB_TO_RECORDSET(v_values) AS t(datetime BIGINT, value DOUBLE PRECISION)
        ON CONFLICT (type, datetime)
        DO UPDATE SET (value, modified_at) = (excluded.value, now);
    GET DIAGNOSTICS result = ROW_COUNT;
    RETURN result;
END;
$$;

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION "smp$upsert_foreach"(v_type INTEGER, v_values JSONB)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
    _value JSONB;
    now    TIMESTAMP := now() at time zone 'UTC';
    result INTEGER   := 0;
    total_result INTEGER := 0;
BEGIN
    FOR _value IN SELECT jsonb_array_elements(v_values)
    LOOP
        INSERT INTO smp (type, datetime, value)
        VALUES (v_type, TO_TIMESTAMP((_value ->> 'datetime')::BIGINT / 1000)::TIMESTAMP, (_value ->> 'value')::DOUBLE PRECISION)
        ON CONFLICT (type, datetime)
        DO UPDATE SET (value, modified_at) = (excluded.value, now);
    GET DIAGNOSTICS result = ROW_COUNT;
    total_result := total_result + result;
    END LOOP;
    RETURN total_result;
END;
$$;

------------------------------------------------------------------------------------------------------------------------
-- vw_smp_hour
CREATE OR REPLACE VIEW vw_smp_hour AS
SELECT type,
       datetime,
       to_char(datetime AT TIME ZONE 'utc' AT TIME ZONE 'Asia/Seoul', 'YYYYMMDD') AS date,
       date_part('hour', datetime AT TIME ZONE 'utc' AT TIME ZONE 'Asia/Seoul') + 1 AS hour,
       value
FROM smp
ORDER BY date, hour;
------------------------------------------------------------------------------------------------------------------------

-- vw_smp_pivot_columns
CREATE OR REPLACE VIEW vw_smp_pivot_columns AS
SELECT DISTINCT type, to_char(datetime AT TIME ZONE 'utc' AT TIME ZONE 'Asia/Seoul', 'YYYYMMDD') AS date, date_part('hour', datetime) + 1 as hour FROM smp;
------------------------------------------------------------------------------------------------------------------------
-- pivot
SELECT *
FROM crosstab(
    'SELECT date, hour, value FROM vw_smp_hour' || ' WHERE type = ' || 1 || ' ORDER BY date, hour ',
    'SELECT DISTINCT hour FROM vw_smp_pivot_columns ORDER BY hour'
)
AS ct(date varchar,
    "1h" numeric, "2h" numeric, "3h" numeric, "4h" numeric, "5h" numeric, "6h" numeric,
    "7h" numeric, "8h" numeric, "9h" numeric, "10h" numeric, "11h" numeric, "12h" numeric,
    "13h" numeric, "14h" numeric, "15h" numeric, "16h" numeric, "17h" numeric, "18h" numeric,
    "19h" numeric, "20h" numeric, "21h" numeric, "22h" numeric, "23h" numeric, "24h" numeric);
------------------------------------------------------------------------------------------------------------------------

SELECT type, date, jsonb_object_agg(EXTRACT(EPOCH FROM datetime) * 1000, value) AS values
FROM vw_smp_hour
GROUP BY type, date;
------------------------------------------------------------------------------------------------------------------------

SELECT type, date, jsonb_object_agg(hour || 'h', value) AS values
FROM vw_smp_hour
GROUP BY type, date;
------------------------------------------------------------------------------------------------------------------------

SELECT type,
       date,
       jsonb_agg(
               jsonb_build_object(
                   'datetime', EXTRACT(EPOCH FROM datetime) * 1000,
                   'hour', hour,
                   'value', value
               )
       ) AS values
FROM vw_smp_hour
GROUP BY type, date;
------------------------------------------------------------------------------------------------------------------------