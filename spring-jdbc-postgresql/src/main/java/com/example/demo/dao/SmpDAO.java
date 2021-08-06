package com.example.demo.dao;

import com.example.demo.bean.Smp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class SmpDAO extends DataAccessObject {

    public Integer upsert(Smp smp) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("public")
                .withFunctionName("smp$upsert")
                .declareParameters(
                        new SqlOutParameter("result", Types.INTEGER),
                        new SqlParameter("v_type", Types.INTEGER),
                        new SqlParameter("v_datetime", Types.OTHER),
                        new SqlParameter("v_value", Types.DOUBLE)
                ).withoutProcedureColumnMetaDataAccess();

        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("v_type", smp.getType().getValue(), Types.INTEGER)
                .addValue("v_datetime", new Date(smp.getDatetime()), Types.TIMESTAMP)
                .addValue("v_value", smp.getValue(), Types.DOUBLE);

        return simpleJdbcCall.executeFunction(Integer.class, source);
    }

    public Integer upsert(Smp.Type type, List<Smp> values) throws JsonProcessingException, SQLException {
        PGobject valuesObj = new PGobject();
        valuesObj.setType("jsonb");
        valuesObj.setValue(objectMapper.writeValueAsString(values));

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("public")
                .withFunctionName("smp$upsert")
                .declareParameters(
                        new SqlOutParameter("result", Types.INTEGER),
                        new SqlParameter("v_type", Types.INTEGER),
                        new SqlParameter("v_values", Types.OTHER)
                ).withoutProcedureColumnMetaDataAccess();

        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("v_type", type.getValue(), Types.INTEGER)
                .addValue("v_values", valuesObj, Types.OTHER);

        return simpleJdbcCall.executeFunction(Integer.class, source);
    }

    public Integer upsertWithForeach(Smp.Type type, List<Smp> values) throws JsonProcessingException, SQLException {
        PGobject valuesObj = new PGobject();
        valuesObj.setType("jsonb");
        valuesObj.setValue(objectMapper.writeValueAsString(values));

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("public")
                .withFunctionName("smp$upsert_foreach")
                .declareParameters(
                        new SqlOutParameter("result", Types.INTEGER),
                        new SqlParameter("v_type", Types.INTEGER),
                        new SqlParameter("v_values", Types.OTHER)
                ).withoutProcedureColumnMetaDataAccess();

        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("v_type", type.getValue(), Types.INTEGER)
                .addValue("v_values", valuesObj, Types.OTHER);

        return simpleJdbcCall.executeFunction(Integer.class, source);
    }
}
