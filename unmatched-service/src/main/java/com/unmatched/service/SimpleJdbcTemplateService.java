package com.unmatched.service;

import com.unmatched.mapper.rowmapper.StepRowMapper;
import com.unmatched.mapper.rowmapper.UserRowMapper;
import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

@Service
//@Lazy
public class SimpleJdbcTemplateService {

    /**
     * JdbcOperations 是一个接口，定义了JdbcTemplate所实现的操作，
     * 通过注入JdbcOperations而不是具体的JdbcTemplate从而达到松耦合
     * */
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcOperations jdbcOperations;

    private static final String QUERY_SQL="select * from TTRD_DAYEND_STEP s where s.STEP_ID=?";

    private static final String QUERY_USER_INFO="select * from user where userName=?";

    @Deprecated
    public String queryStepInfo(){
        Step step = jdbcOperations.queryForObject(QUERY_SQL, new StepRowMapper(), 37);
        return step.toString();
    }

    public String queryUserInfo(String userName){
        User user=jdbcOperations.queryForObject(QUERY_USER_INFO,new UserRowMapper(),userName);
        return user.toString();
    }

}
