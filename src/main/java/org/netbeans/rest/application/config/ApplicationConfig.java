/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Gbenga
 */
@javax.ws.rs.ApplicationPath("service")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.cometbid.commerce.util.rs.UtilityManagerService.class);
        resources.add(com.cometbid.commerce.utility.JSONRequestFilter.class);
        resources.add(com.ng.emts.eng.vas.mappers.AuthorizationDeniedMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.CountryNotFoundMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.CurrencyNotFoundMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.InvalidParameterMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.LanguageNotFoundMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.MediaTypeNotSpecifiedMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.RegionNotFoundMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.StateProvNotFoundMapper.class);
        resources.add(com.ng.emts.eng.vas.mappers.SubscriptionTypeNotFoundMapper.class);
    }
    
}
