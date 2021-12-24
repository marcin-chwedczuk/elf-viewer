package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

public abstract class BasePartialEnum {
    private final String name;

    public BasePartialEnum() {
        this.name = null;
    }
    public BasePartialEnum(String name) {
        this.name = requireNonNull(name);
    }

    protected abstract AtomicReference<Map<String, String>> name2ApiNameMappingContainer();

    public final String name() { return name; }
    public abstract String hexString();

    public final boolean isKnownValue() {
        return (this.name != null);
    }
    public final boolean isUnknownValue() {
        return !isKnownValue();
    }

    public String apiName() {
        AtomicReference<Map<String, String>> name2apiNameContainer = name2ApiNameMappingContainer();
        Map<String, String> name2apiName = name2apiNameContainer.get();

        if (name2apiName == null) {
            name2apiName = createName2ApiNameMapping();
            name2apiNameContainer.set(name2apiName);
        }

        if (name2apiName.containsKey(name())) {
            return name2apiName.get(name());
        } else {
            return hexString();
        }
    }

    private Map<String, String> createName2ApiNameMapping() {
        HashMap<String, String> tmp = new HashMap<>();
        Class<?> thisClass = this.getClass();

        for (Field field : getClass().getFields()) {
            // Static fields only
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;

            // Field must be of enum type
            if (!thisClass.isAssignableFrom(field.getType())) continue;

            // Field must be @ElfApi annotation
            ElfApi elfApi = field.getAnnotation(ElfApi.class);
            if (elfApi == null) continue;

            try {
                BasePartialEnum value = (BasePartialEnum)field.get(null);
                // Add mapping
                tmp.put(value.name(), elfApi.value());
            } catch(IllegalAccessException e) {
                // Log but otherwise ignore
                e.printStackTrace();
            }
        }

        return Collections.unmodifiableMap(tmp);
    }
}
