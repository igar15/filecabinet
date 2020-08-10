package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DeveloperFormatter implements Formatter<Department> {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public Department parse(String s, Locale locale) throws ParseException {
        return departmentService.findByName(s);
    }

    @Override
    public String print(Department department, Locale locale) {
        return department.getName();
    }
}
