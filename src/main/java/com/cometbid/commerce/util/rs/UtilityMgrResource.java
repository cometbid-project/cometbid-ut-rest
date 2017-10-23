/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cometbid.commerce.util.rs;

import com.cometbid.ut.embeddables.StateCitiesEO;
import com.cometbid.ut.embeddables.StateLgaEO;
import com.cometbid.ut.entities.CountryEO;
import com.cometbid.ut.entities.CurrencyEO;
import com.cometbid.ut.entities.LanguageEO;
import com.cometbid.ut.entities.StateProvEO;
import com.cometbid.ut.exceptions.CountryNotFoundException;
import com.cometbid.ut.exceptions.CurrencyNotFoundException;
import com.cometbid.ut.exceptions.LanguageNotFoundException;
import com.cometbid.ut.exceptions.RegionNotFoundException;
import com.cometbid.ut.exceptions.StateProvNotFoundException;
import com.cometbid.ut.exceptions.SubscriptionTypeNotFoundException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gbenga
 */
public interface UtilityMgrResource {

    Response getAllSubscriptionTypes() throws SubscriptionTypeNotFoundException;

    Response getSubscriptionWithoutCount() throws SubscriptionTypeNotFoundException;

    Response getSubscriptionById(@QueryParam("id") Integer subscriptionId)
            throws SubscriptionTypeNotFoundException;

    Response getAllCountries() throws CountryNotFoundException;

    Response getCountriesWithoutCount() throws CountryNotFoundException;

    Response getCountriesPaginated(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws CountryNotFoundException;

    Response getCountryById(@QueryParam("id") Integer countryId)
            throws CountryNotFoundException;

    Response addCountry(CountryEO newCountry);

    Response getCallingCodes(@QueryParam("id") Integer countryId)
            throws CountryNotFoundException;

    Response getDiallingCodes(@QueryParam("id") Integer countryId) throws CountryNotFoundException;

    Response getPhoneFormats(@QueryParam("id") Integer countryId) throws CountryNotFoundException;

    Response updateCountry(CountryEO updatedCountry) throws CountryNotFoundException;

    Response removeCountry(@QueryParam("id") Integer countryId) throws CountryNotFoundException;

    Response getCountryStates(@PathParam("id") Integer countryId) throws CountryNotFoundException;

    Response getCountryStates(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize,
            @PathParam("id") Integer countryId) throws CountryNotFoundException;

    Response removePhoneFormat(@PathParam("id") Integer countryId, int phoneFormat)
            throws CountryNotFoundException;

    Response addPhoneFormat(@PathParam("id") Integer countryId, int phoneFormat)
            throws CountryNotFoundException;

    Response removeDiallingCode(@PathParam("id") Integer countryId,
            @PathParam("value") String diallingCode) throws CountryNotFoundException;

    Response addDiallingCode(@PathParam("id") Integer countryId, @PathParam("value") String diallingCode)
            throws CountryNotFoundException;

    Response removeCallingCode(@PathParam("id") Integer countryId, @PathParam("value") String callingCode)
            throws CountryNotFoundException;

    Response addCallingCode(@QueryParam("id") Integer countryId, @PathParam("value") String callingCode)
            throws CountryNotFoundException;

    Response getAllCurrencies() throws CurrencyNotFoundException;

    Response getCurrenciesWithoutCount() throws CurrencyNotFoundException;

    Response getCurrenciesPaginated(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws CurrencyNotFoundException;

    Response getCurrencyById(@QueryParam("id") Integer currencyId)
            throws CurrencyNotFoundException;

    Response addCurrency(CurrencyEO newCurrency);

    Response updateCurrency(CurrencyEO updatedCurrency) throws CurrencyNotFoundException;

    Response removeCurrency(@PathParam("id") Integer currencyId) throws CurrencyNotFoundException;

    Response getLanguageById(@QueryParam("id") Integer languageId)
            throws LanguageNotFoundException;

    Response getAllLanguages() throws LanguageNotFoundException;

    Response getLanguagesWithoutCount() throws LanguageNotFoundException;

    Response getLanguagesPaginated(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws LanguageNotFoundException;

    Response addCurrency(LanguageEO newLanguage);

    Response updateLanguage(LanguageEO updatedLanguage) throws LanguageNotFoundException;

    Response removeLanguage(@PathParam("id") Integer languageId) throws LanguageNotFoundException;

    Response getAllRegions() throws RegionNotFoundException;

    Response getRegionsWithoutCount() throws RegionNotFoundException;

    Response getRegionById(@QueryParam("id") Integer regionId)
            throws RegionNotFoundException;

    //  Response getStatesWithCount(@PathParam("id") Integer countryId) throws StateProvNotFoundException;
    Response getStatesWithoutCount(@PathParam("id") Integer countryId)
            throws StateProvNotFoundException, CountryNotFoundException;

    Response getAllStatesPaginated(
            @DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws StateProvNotFoundException;

    Response getStatesById(@QueryParam("id") Integer statesId) throws StateProvNotFoundException;

    Response addCountry(@PathParam("countryId") Integer countryId, StateProvEO newStateProv)
            throws CountryNotFoundException;

    Response updateStateProv(@PathParam("countryId") Integer countryId, StateProvEO updatedStateProv)
            throws CountryNotFoundException, StateProvNotFoundException;

    Response removeStatesProv(@PathParam("countryId") Integer countryId,
            @PathParam("statesId") Integer statesId)
            throws CountryNotFoundException, StateProvNotFoundException;

    Response addCityToState(@PathParam("id") Integer stateId, StateCitiesEO cityToAdd)
            throws StateProvNotFoundException;

    Response addLgaToState(@PathParam("id") Integer stateId, StateLgaEO lgaToAdd)
            throws StateProvNotFoundException;

    Response removeCityFromStatesProv(@PathParam("id") Integer stateId, StateCitiesEO cityToRemove)
            throws StateProvNotFoundException;

    Response removeLgaFromStatesProv(@PathParam("id") Integer stateId, StateLgaEO lgaToRemove)
            throws StateProvNotFoundException;

    Response getStatesWithCount(@PathParam("id") Integer countryId)
            throws StateProvNotFoundException, CountryNotFoundException;

}
