package tn.rnu.enicarthage.khadamni.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {
    private long maxFileSize;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        maxFileSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(!Objects.nonNull(multipartFile)){
            return true;
        }

        if (maxFileSize > 0 && multipartFile.getSize() > maxFileSize)
        {
            return false;
        }

        return true;
    }
}
