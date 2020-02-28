package com.unmatched.mapper.rowmapper;

import com.unmatched.pojo.Step;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StepRowMapper implements RowMapper<Step> {
    @Override
    public Step mapRow(ResultSet resultSet, int i) throws SQLException {
        Step step=new Step();
        step.setStep_Id(resultSet.getDouble("STEP_ID"));
        step.setStepName(resultSet.getString("STEPNAME"));
        step.setAssemblyName(resultSet.getString("ASSEMBLYNAME"));
        step.setStepIndex(resultSet.getDouble("STEPINDEX"));
        step.setRun(resultSet.getInt("RUN"));
        step.setSkip(resultSet.getInt("SKIP"));
        step.setLoop(resultSet.getInt("LOOP"));
        step.setProcedure_Name(resultSet.getString("PROCEDURE_NAME"));
        return step;
    }
}
