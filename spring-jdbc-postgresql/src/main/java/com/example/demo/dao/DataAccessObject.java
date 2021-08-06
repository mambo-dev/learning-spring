package com.example.demo.dao;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DataAccessObject implements EnvironmentAware {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected Environment environment;

    protected JdbcTemplate jdbcTemplate;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
