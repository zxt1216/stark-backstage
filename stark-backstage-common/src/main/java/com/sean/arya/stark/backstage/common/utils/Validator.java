package com.sean.arya.stark.backstage.common.utils;

import com.sean.arya.stark.backstage.common.data.Code;
import com.sean.arya.stark.backstage.common.exceptions.ParamException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Sean
 * @date 2018/5/10 11:51
 * @description  参数校验
 * @vesion 1.0.0
 */

@Slf4j
public class Validator {
    private static javax.validation.Validator validator;
    private Validator(){}
    static{
        validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
    /**
     * 验证
     *
     * @param object 对象
     * @param groups 分组
     * @param <T>    泛型
     */
    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);
        for (ConstraintViolation constraintViolation : constraintViolations) {
            String message = constraintViolation.getMessage();
            Path propertyPath = constraintViolation.getPropertyPath();
            log.warn("对象属性:{},国际化key:{},错误信息:{}", propertyPath, constraintViolation.getMessageTemplate(), message);
            throw new ParamException(Code.ILLEGAL_PARAM.code, propertyPath + message);
        }
    }

    /**
     * 验证所有参数
     *
     * @param object 对象
     * @param groups 分组
     * @param <T>    泛型
     */
    public static <T> void validateAll(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);
        String message;
        Path propertyPath;
        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            message = constraintViolation.getMessage();
            propertyPath = constraintViolation.getPropertyPath();
            log.warn("对象属性:{},国际化key:{},错误信息:{}", propertyPath, constraintViolation.getMessageTemplate(), message);
            messageList.add(propertyPath + message);
        }
        if (messageList.size() > 0) {
            throw new ParamException(Code.ILLEGAL_PARAM.code, messageList.toString());
        }
    }
}
