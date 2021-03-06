package com.seepine.json.mapper;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.deser.key.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.key.ZonedDateTimeKeySerializer;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * solve time serialize
 *
 * @author seepine
 * @since 0.0.2
 */
public class JavaTimeModule extends SimpleModule {
  private static final long serialVersionUID = 1L;
  public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
  public static final String NORM_TIME_PATTERN = "HH:mm:ss";

  public JavaTimeModule() {
    super(PackageVersion.VERSION);
    // First deserializers

    // // Instant variants:
    addDeserializer(Instant.class, InstantDeserializer.INSTANT);
    addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);
    addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);

    // // Other deserializers
    addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
    addDeserializer(MonthDay.class, MonthDayDeserializer.INSTANCE);
    addDeserializer(OffsetTime.class, OffsetTimeDeserializer.INSTANCE);
    addDeserializer(Period.class, JSR310StringParsableDeserializer.PERIOD);
    addDeserializer(Year.class, YearDeserializer.INSTANCE);
    addDeserializer(YearMonth.class, YearMonthDeserializer.INSTANCE);
    addDeserializer(ZoneId.class, JSR310StringParsableDeserializer.ZONE_ID);
    addDeserializer(ZoneOffset.class, JSR310StringParsableDeserializer.ZONE_OFFSET);

    // then serializers:
    addSerializer(Duration.class, DurationSerializer.INSTANCE);
    addSerializer(Instant.class, InstantSerializer.INSTANCE);
    addSerializer(MonthDay.class, MonthDaySerializer.INSTANCE);
    addSerializer(OffsetDateTime.class, OffsetDateTimeSerializer.INSTANCE);
    addSerializer(OffsetTime.class, OffsetTimeSerializer.INSTANCE);
    addSerializer(Period.class, new ToStringSerializer(Period.class));
    addSerializer(Year.class, YearSerializer.INSTANCE);
    addSerializer(YearMonth.class, YearMonthSerializer.INSTANCE);

    /* 27-Jun-2015, tatu: This is the real difference from the old
     *  {@link JSR310Module}: default is to produce ISO-8601 compatible
     *  serialization with timezone offset only, not timezone id.
     *  But this is configurable.
     */
    addSerializer(ZonedDateTime.class, ZonedDateTimeSerializer.INSTANCE);

    // since 2.11: need to override Type Id handling
    // (actual concrete type is `ZoneRegion`, but that's not visible)
    addSerializer(ZoneId.class, new ZoneIdSerializer());
    addSerializer(ZoneOffset.class, new ToStringSerializer(ZoneOffset.class));

    // key serializers
    addKeySerializer(ZonedDateTime.class, ZonedDateTimeKeySerializer.INSTANCE);

    // key deserializers
    addKeyDeserializer(Duration.class, DurationKeyDeserializer.INSTANCE);
    addKeyDeserializer(Instant.class, InstantKeyDeserializer.INSTANCE);
    addKeyDeserializer(MonthDay.class, MonthDayKeyDeserializer.INSTANCE);
    addKeyDeserializer(OffsetDateTime.class, OffsetDateTimeKeyDeserializer.INSTANCE);
    addKeyDeserializer(OffsetTime.class, OffsetTimeKeyDeserializer.INSTANCE);
    addKeyDeserializer(Period.class, PeriodKeyDeserializer.INSTANCE);
    addKeyDeserializer(Year.class, YearKeyDeserializer.INSTANCE);
    addKeyDeserializer(YearMonth.class, YearMonthKeyDeserializer.INSTANCE);
    addKeyDeserializer(ZonedDateTime.class, ZonedDateTimeKeyDeserializer.INSTANCE);
    addKeyDeserializer(ZoneId.class, ZoneIdKeyDeserializer.INSTANCE);
    addKeyDeserializer(ZoneOffset.class, ZoneOffsetKeyDeserializer.INSTANCE);

    this.addSerializer(
        LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
    this.addSerializer(
        LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(NORM_DATE_PATTERN)));
    this.addSerializer(
        LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(NORM_TIME_PATTERN)));

    this.addDeserializer(
        LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
    this.addDeserializer(
        LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(NORM_DATE_PATTERN)));
    this.addDeserializer(
        LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(NORM_TIME_PATTERN)));
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    context.addValueInstantiators(
        new ValueInstantiators.Base() {
          @Override
          public ValueInstantiator findValueInstantiator(
              DeserializationConfig config,
              BeanDescription beanDesc,
              ValueInstantiator defaultInstantiator) {
            JavaType type = beanDesc.getType();
            Class<?> raw = type.getRawClass();

            // 15-May-2015, tatu: In theory not safe, but in practice we do need to do "fuzzy"
            // matching
            // because we will (for now) be getting a subtype, but in future may want to downgrade
            // to the common base type. Even more, serializer may purposefully force use of base
            // type.
            // So... in practice it really should always work, in the end. :)
            if (ZoneId.class.isAssignableFrom(raw)) {
              // let's assume we should be getting "empty" StdValueInstantiator here:
              if (defaultInstantiator instanceof StdValueInstantiator) {
                StdValueInstantiator inst = (StdValueInstantiator) defaultInstantiator;
                // one further complication: we need ZoneId info, not sub-class
                AnnotatedClass ac;
                if (raw == ZoneId.class) {
                  ac = beanDesc.getClassInfo();
                } else {
                  // we don't need Annotations, so constructing directly is fine here
                  // even if it's not generally recommended
                  ac =
                      AnnotatedClassResolver.resolve(
                          config, config.constructType(ZoneId.class), config);
                }
                if (!inst.canCreateFromString()) {
                  AnnotatedMethod factory = _findFactory(ac, "of", String.class);
                  if (factory != null) {
                    inst.configureFromStringCreator(factory);
                  }
                  // otherwise... should we indicate an error?
                }
                // return ZoneIdInstantiator.construct(config, beanDesc, defaultInstantiator);
              }
            }
            return defaultInstantiator;
          }
        });
  }

  protected AnnotatedMethod _findFactory(AnnotatedClass cls, String name, Class<?>... argTypes) {
    final int argCount = argTypes.length;
    for (AnnotatedMethod method : cls.getFactoryMethods()) {
      if (!name.equals(method.getName()) || (method.getParameterCount() != argCount)) {
        continue;
      }
      for (int i = 0; i < argCount; ++i) {
        Class<?> argType = method.getParameter(i).getRawType();
        if (!argType.isAssignableFrom(argTypes[i])) {
          continue;
        }
      }
      return method;
    }
    return null;
  }
}
