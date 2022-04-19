package tn.rnu.enicarthage.khadamni.shared;

import org.hibernate.internal.util.StringHelper;

import java.util.Optional;
import java.util.UUID;

public class Globals {
    public static final String PHOTO_PREFIX = "photo";
    public static final String RESUME_PREFIX = "resume";
    public static final String LOGO_PREFIX = "logo";

    private Globals() {};

    public static String generateFileName(String prefix, String fileName)
    {
        return String.format("%s_%s%s", prefix, UUID.randomUUID(), getExtensionFromFileName(fileName));
    }

    public static String getExtensionFromFileName(String fileName){
        return Optional.ofNullable(fileName).filter(name -> name.contains(".")).map(name -> name.substring(name.lastIndexOf("."))).orElse("");
    }

    public static String parseNullableString(String string){
        return !StringHelper.isBlank(string) ? string : null;
    }
}
