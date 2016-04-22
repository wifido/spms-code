package com.sf.module.common.domain.sql;

import static com.sf.module.common.domain.Constants.COLON;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlHelper {
    public static final List<ValueTranslator> TRANSLATORS = new ArrayList<ValueTranslator>();

    static {
        TRANSLATORS.add(new ValueTranslator() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return value == null;
            }

            @Override
            public String translateValue(Object value) {
                return "NULL";
            }
        });

        TRANSLATORS.add(new StringValueTranslator<String>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return CharSequence.class.isAssignableFrom(type);
            }

            @Override
            public String translateValue(String value) {
                return value;
            }
        });

        TRANSLATORS.add(new ValueTranslator<Persistable>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return Persistable.class.isAssignableFrom(type);
            }

            @Override
            public String translateValue(Persistable value) {
                return value.getPersistableValue();
            }
        });

        TRANSLATORS.add(new ValueTranslator<Enum>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return Enum.class.isAssignableFrom(type);
            }

            @Override
            public Integer translateValue(Enum value) {
                return value.ordinal();
            }
        });

        TRANSLATORS.add(new ValueTranslator<DateTime>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return DateTime.class.isAssignableFrom(type);
            }

            @Override
            public Long translateValue(DateTime value) {
                return value.getMillis();
            }
        });

        TRANSLATORS.add(new ValueTranslator<Date>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return Date.class.isAssignableFrom(type);
            }

            @Override
            public Long translateValue(Date value) {
                return value.getTime();
            }
        });

        TRANSLATORS.add(new ValueTranslator() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return true;
            }

            @Override
            public Object translateValue(Object value) {
                return value;
            }
        });
    }

    public static String toSqlValue(Object value) {
        return findValueTranslator(value).toSqlValue(value);
    }
    
    public static String toSqlParam(String paramName) {
		return COLON + paramName;
	}

    private static ValueTranslator findValueTranslator(final Object value) {
        final Class type = value == null ? null : value.getClass();

        return Iterables.find(TRANSLATORS, new Predicate<ValueTranslator>() {
            @Override
            public boolean apply(ValueTranslator valueTranslator) {
                return valueTranslator.isApplicable(type, value);
            }
        });
    }

    public abstract static class ValueTranslator<T> {
        public abstract boolean isApplicable(Class type, Object value);

        protected abstract Object translateValue(T value);

        public String toQueryValue(Object value) {
            //noinspection unchecked
            return translateValue((T) value).toString();
        }

        public String toSqlValue(Object value) {
            return toQueryValue(value);
        }
    }

    public abstract static class StringValueTranslator<T> extends ValueTranslator<T> {
        @Override
        public String toSqlValue(Object value) {
            return "\"" + toQueryValue(value) + "\"";
        }
    }
}
