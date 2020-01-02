/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shiohara.provider.auth;

import java.util.List;

import com.viglet.shiohara.bean.provider.auth.ShAuthProviderInstanceBean;
import com.viglet.shiohara.persistence.model.provider.auth.ShAuthProviderInstance;
import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.provider.auth.ShAuthProviderInstanceRepository;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
@Component
public class ShAuthProviderService {
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ShAuthProviderService.class);

	private static final String PROVIDER_PATH = "/provider/auth/%s";

	@Autowired
	private ShConfigVarRepository shConfigVarRepository;
	@Autowired
	private ShAuthProviderInstanceRepository shAuthProviderInstanceRepository;

	public ShAuthProviderInstanceBean getShAuthProviderInstanceBean(String providerId) {
		ShAuthProviderInstance shAuthProviderInstance = shAuthProviderInstanceRepository.findById(providerId)
				.orElse(null);
		ShAuthProviderInstanceBean shAuthProviderInstanceBean = new ShAuthProviderInstanceBean();
		if (shAuthProviderInstance != null) {
			shAuthProviderInstanceBean = new ShAuthProviderInstanceBean();
			shAuthProviderInstanceBean.setId(shAuthProviderInstance.getId());
			shAuthProviderInstanceBean.setName(shAuthProviderInstance.getName());
			shAuthProviderInstanceBean.setDescription(shAuthProviderInstance.getDescription());
			shAuthProviderInstanceBean.setVendor(shAuthProviderInstance.getVendor());
			shAuthProviderInstanceBean.setEnabled(shAuthProviderInstance.getEnabled());

			String providerInstancePath = String.format(PROVIDER_PATH, shAuthProviderInstance.getId());

			List<ShConfigVar> shConfigVars = shConfigVarRepository.findByPath(providerInstancePath);

			for (ShConfigVar shConfigVar : shConfigVars) {
				shAuthProviderInstanceBean.getProperties().put(shConfigVar.getName(), shConfigVar.getValue());
			}
		}

		return shAuthProviderInstanceBean;
	}
}
