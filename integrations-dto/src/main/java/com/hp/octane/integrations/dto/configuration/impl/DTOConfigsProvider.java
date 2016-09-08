package com.hp.octane.integrations.dto.configuration.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hp.octane.integrations.dto.DTOBase;
import com.hp.octane.integrations.dto.DTOInternalProviderBase;
import com.hp.octane.integrations.dto.configuration.OctaneConfiguration;
import com.hp.octane.integrations.dto.configuration.CIProxyConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gullery on 10/02/2016.
 */

public final class DTOConfigsProvider extends DTOInternalProviderBase {
	private final Map<Class, Class> dtoPairs = new HashMap<>();

	private DTOConfigsProvider() {
	}

	@Override
	protected Class[] getXMLAbleClasses() {
		return new Class[0];
	}

	public static void ensureInit(Map<Class<? extends DTOBase>, DTOInternalProviderBase> registry, ObjectMapper objectMapper) {
		registry.put(OctaneConfiguration.class, INSTANCE_HOLDER.instance);
		registry.put(CIProxyConfiguration.class, INSTANCE_HOLDER.instance);

		INSTANCE_HOLDER.instance.dtoPairs.put(OctaneConfiguration.class, OctaneConfigurationImpl.class);
		INSTANCE_HOLDER.instance.dtoPairs.put(CIProxyConfiguration.class, CIProxyConfigurationImpl.class);

		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(OctaneConfiguration.class, OctaneConfigurationImpl.class);
		resolver.addMapping(CIProxyConfiguration.class, CIProxyConfigurationImpl.class);
		SimpleModule module = new SimpleModule();
		module.setAbstractTypes(resolver);
		objectMapper.registerModule(module);
	}

	public <T> T instantiateDTO(Class<T> targetType) throws InstantiationException, IllegalAccessException {
		T result = null;
		if (dtoPairs.containsKey(targetType)) {
			result = (T) dtoPairs.get(targetType).newInstance();
		}
		return result;
	}

	private static final class INSTANCE_HOLDER {
		private static final DTOConfigsProvider instance = new DTOConfigsProvider();
	}
}
