package tn.rnu.enicarthage.khadamni.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static tn.rnu.enicarthage.khadamni.shared.Globals.getExtensionFromFileName;

public class AllowedFileExtensionsValidator implements ConstraintValidator<AllowedFileExtensions, MultipartFile> {
    private List<String> extensions;

    @Override
    public void initialize(AllowedFileExtensions constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        extensions = Arrays.asList(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(!Objects.nonNull(multipartFile)){
            return true;
        }

        if(extensions.contains(multipartFile.getContentType().split("/")[1]) && extensions.contains(getExtensionFromFileName(multipartFile.getOriginalFilename()).substring(1)))
        {
            return true;
        }

        return false;
    }
}
