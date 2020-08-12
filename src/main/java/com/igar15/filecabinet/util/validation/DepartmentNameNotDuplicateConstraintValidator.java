package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class DepartmentNameNotDuplicateConstraintValidator implements ConstraintValidator<DepartmentNameNotDuplicate, Department> {

   @Autowired
   private DepartmentService departmentService;

   public void initialize(DepartmentNameNotDuplicate constraint) {
   }

   public boolean isValid(Department obj, ConstraintValidatorContext context) {
      if (departmentService == null) {
         return true;
      }

      Department department = null;
      try {
         department = departmentService.findByName(obj.getName());
      } catch (NotFoundException e) {
         return true;
      }
      if (obj.isNew()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Department already exists").addPropertyNode("name").addConstraintViolation();
         return false;
      }
      else if (!obj.getId().equals(department.getId())) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Department already exists").addPropertyNode("name").addConstraintViolation();
         return false;
      }
      return true;
   }
}
