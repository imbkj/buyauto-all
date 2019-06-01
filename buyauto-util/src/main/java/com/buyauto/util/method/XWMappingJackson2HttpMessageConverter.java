package com.buyauto.util.method;

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Implementation of
 * {@link org.springframework.http.converter.HttpMessageConverter
 * HttpMessageConverter} that can read and write JSON using <a
 * href="http://jackson.codehaus.org/">Jackson 2.x's</a> {@link ObjectMapper}.
 *
 * <p>
 * This converter can be used to bind to typed beans, or untyped
 * {@link java.util.HashMap HashMap} instances.
 *
 * <p>
 * By default, this converter supports {@code application/json}. This can be
 * overridden by setting the {@link #setSupportedMediaTypes supportedMediaTypes}
 * property.
 *
 * <p>
 * Compatible with Jackson 2.1 and higher.
 *
 * @author Arjen Poutsma
 * @author Keith Donald
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Sebastien Deleuze
 * @since 3.1.2
 */
public class XWMappingJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

	private String jsonPrefix;

	/**
	 * Construct a new {@code MappingJackson2HttpMessageConverter}.
	 */
	public XWMappingJackson2HttpMessageConverter() {
		super(new ObjectMapper(), new MediaType("application", "json", DEFAULT_CHARSET), new MediaType("application",
				"*+json", DEFAULT_CHARSET));
		SimpleModule module = new SimpleModule();
		
		module.addSerializer(Long.class, new JsonSerializer<Long>() {

			@Override
			public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
					JsonProcessingException {
				if (value != null && value.toString().length() > 16) {
					jgen.writeString(value + "");
				} else {
					jgen.writeNumber(value);
				}
			}

		});
		objectMapper.registerModule(module);
	}

	/**
	 * Specify a custom prefix to use for this view's JSON output. Default is
	 * none.
	 * 
	 * @see #setPrefixJson
	 */
	public void setJsonPrefix(String jsonPrefix) {
		this.jsonPrefix = jsonPrefix;
	}

	/**
	 * Indicate whether the JSON output by this view should be prefixed with
	 * "{} &&". Default is false.
	 * <p>
	 * Prefixing the JSON string in this manner is used to help prevent JSON
	 * Hijacking. The prefix renders the string syntactically invalid as a
	 * script so that it cannot be hijacked. This prefix does not affect the
	 * evaluation of JSON, but if JSON validation is performed on the string,
	 * the prefix would need to be ignored.
	 * 
	 * @see #setJsonPrefix
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.jsonPrefix = (prefixJson ? "{} && " : null);
	}

	@Override
	protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
		if (this.jsonPrefix != null) {
			generator.writeRaw(this.jsonPrefix);
		}
		String jsonpFunction = (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object)
				.getJsonpFunction() : null);
		if (jsonpFunction != null) {
			generator.writeRaw(jsonpFunction + "(");
		}
	}

	@Override
	protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
		String jsonpFunction = (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object)
				.getJsonpFunction() : null);
		if (jsonpFunction != null) {
			generator.writeRaw(");");
		}

	}
}
