package com.oohoo.spacestationspringbootstarter.validate;

import com.oohoo.spacestationspringbootstarter.config.SpringUtils;
import com.oohoo.spacestationspringbootstarter.utils.MyStringUtils;
import com.oohoo.spacestationspringbootstarter.validate.annotation.MyValidate;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintViolationCreationContext;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/3
 */
public class MyValidator implements ConstraintValidator<MyValidate, Object> {

    public MyValidator() {
    }

    MyValidate myValidate = null;

    Validate verify = null;



    @Override
    public void initialize(MyValidate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.myValidate = constraintAnnotation;
        String name = myValidate.verify().getName();
        verify = SpringUtils.getBean(MyStringUtils.getClassName(name));
        if (null == verify) {
            try {
                verify = myValidate.verify().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String fieldName = "";
        if(constraintValidatorContext instanceof ConstraintValidatorContextImpl) {
            List<ConstraintViolationCreationContext> constraintViolationCreationContexts =
                    ((ConstraintValidatorContextImpl) constraintValidatorContext).getConstraintViolationCreationContexts();
            PathImpl path = constraintViolationCreationContexts.get(0).getPath();
            fieldName = path.getLeafNode().getName();
        }
        boolean validate = false;
        if (null != verify && verify instanceof SingleValidate) {
            SingleValidate singleValidate = (SingleValidate) verify;
            validate = singleValidate.validate(o);
        }else if(null != verify && verify instanceof GroupValidate) {
            Map<String, Map<String, Object>> contractFieldThreadLocal = GroupValidateData.getContractFieldThreadLocal();
            if(null == contractFieldThreadLocal) {
                Map<String,Map<String,Object>> map = new HashMap<>();
                map.put(myValidate.groupName(), new HashMap<String,Object>());
                GroupValidateData.setContractFieldThreadLocal(map);
            }

            Map<String, Object> stringObjectMap = GroupValidateData.getContractFieldThreadLocal().get(myValidate.groupName());
            stringObjectMap.put(fieldName,o);
            if(myValidate.groupSize() <= stringObjectMap.size()) {
                GroupValidate groupValidate = (GroupValidate) verify;
                validate = groupValidate.validate(stringObjectMap);
                GroupValidateData.clearContractFieldThreadLocal();
            }else {
                validate = true;
            }
        }

        return validate;
    }





}
