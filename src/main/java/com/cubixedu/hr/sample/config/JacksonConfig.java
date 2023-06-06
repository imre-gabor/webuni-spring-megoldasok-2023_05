//package com.cubixedu.hr.sample.config;
//
//import java.time.format.DateTimeFormatter;
//
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//
//@Configuration
//public class JacksonConfig {
//
//	private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
//
//	@Bean
//	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//		
//		return builder -> {
//			// builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss"); --> java.time típusokra nem hat
//			builder.serializers(new LocalDateTimeSerializer(
//					DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
//			builder.deserializers(new LocalDateTimeDeserializer(
//					DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
//		};
//	}
//	
//}
