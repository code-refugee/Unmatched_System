package com.unmatched.converter;

import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;

import java.util.List;

public interface CoreAcctRecordConverter {
    String encode(User user, List<Step> steps);
}
