package kr.milibrary.aspect;

import kr.milibrary.exception.UnprocessableEntityException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class ValidateBean {
    @Before(value = "@annotation(kr.milibrary.annotation.ValidBean)")
    public void validateBean(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult errors = (BindingResult) arg;
                if (errors.hasErrors()) {
                    List<String> errorMessages = new ArrayList<>();
                    for (ObjectError error : errors.getAllErrors())
                        errorMessages.add(error.getDefaultMessage());

                    throw new UnprocessableEntityException(errorMessages);
                }
            }
        }
    }
}
