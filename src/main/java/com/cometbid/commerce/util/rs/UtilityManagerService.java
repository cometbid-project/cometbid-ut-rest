package com.cometbid.commerce.util.rs;

import com.cometbid.commerce.ut.cdi.HitCounterInterceptor;
import com.cometbid.commerce.ut.cdi.TimeInMethodInterceptor;
import com.cometbid.commerce.ut.common.DomainObject;
import com.cometbid.commerce.ut.qualifiers.JavaUtilLogger;
import com.cometbid.commerce.util.rs.extra.JsonGenerator;
import com.cometbid.commerce.ut.services.AccountTypeFacadeLocal;
import com.cometbid.commerce.ut.services.CountryFacadeLocal;
import com.cometbid.commerce.ut.services.CurrencyFacadeLocal;
import com.cometbid.commerce.ut.services.LanguageFacadeLocal;
import com.cometbid.commerce.ut.services.RegionFacadeLocal;
import com.cometbid.commerce.ut.services.StateProvFacadeLocal;
import com.cometbid.ut.embeddables.StateCitiesEO;
import com.cometbid.ut.embeddables.StateLgaEO;
import com.cometbid.ut.entities.CountryEO;
import com.cometbid.ut.entities.CurrencyEO;
import com.cometbid.ut.entities.LanguageEO;
import com.cometbid.ut.entities.RegionEO;
import com.cometbid.ut.entities.StateProvEO;
import com.cometbid.ut.exceptions.CountryNotFoundException;
import com.cometbid.ut.exceptions.CurrencyNotFoundException;
import com.cometbid.ut.exceptions.LanguageNotFoundException;
import com.cometbid.ut.exceptions.RegionNotFoundException;
import com.cometbid.ut.exceptions.StateProvNotFoundException;
import com.cometbid.ut.exceptions.SubscriptionTypeNotFoundException;
import java.util.Collection;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import javax.interceptor.Interceptors;

/**
 * REST Web Service
 *
 * @author adebowale.samuel
 */
@RequestScoped
@Path("ut")
@Interceptors({HitCounterInterceptor.class, TimeInMethodInterceptor.class})
public class UtilityManagerService implements UtilityMgrResource {

    @Inject
    @JavaUtilLogger
    private Logger logger;

    @Context
    private UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @Context
    HttpHeaders httpHeaders;

    @EJB
    private AccountTypeFacadeLocal accountTypeFacade;
    @EJB
    private CountryFacadeLocal countryFacade;
    @EJB
    private CurrencyFacadeLocal currencyFacade;
    @EJB
    private LanguageFacadeLocal languageFacade;
    @EJB
    private RegionFacadeLocal regionFacade;
    @EJB
    private StateProvFacadeLocal stateProvFacade;

    /**
     * Creates a new instance of PaymentService
     */
    public UtilityManagerService() {
    }

    /**
     * Get Cometbid Subscription types with the number of record included
     * as JSON String wrapped in a javax.ws.rs.core.Response 
     * 
     * example: GET /ut/subscriptions/count
     * 
     * @return JSON String as <a>javax.ws.rs.core.Response</a>
     * @throws com.cometbid.ut.exceptions.SubscriptionTypeNotFoundException
     */
    @GET
    @Path("subscriptions/count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getAllSubscriptionTypes() throws SubscriptionTypeNotFoundException {

        try {
            Map<Integer, Collection<DomainObject>> accountMaps
                    = accountTypeFacade.getSubscriptionWithCount();

            int key = (Integer) accountMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> accountTypes = accountMaps.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(accountTypes, key, "Subscriptions");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/subscriptions/nocount
     *
     * @return JSON string
     * @throws com.cometbid.ut.exceptions.SubscriptionTypeNotFoundException
     */
    @GET
    @Path("subscriptions/nocount")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getSubscriptionWithoutCount() throws SubscriptionTypeNotFoundException {

        try {
            Collection<DomainObject> accountTypeList
                    = accountTypeFacade.getSubscriptionWithoutCount();

            String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(accountTypeList, "Subscriptions");
            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/subscription/123
     *
     * @param subscriptionId
     * @return
     * @throws com.cometbid.ut.exceptions.SubscriptionTypeNotFoundException
     */
    @GET
    @Path("/subscriptions/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getSubscriptionById(@PathParam("id") Integer subscriptionId)
            throws SubscriptionTypeNotFoundException {

        try {
            DomainObject subscriptionType = accountTypeFacade.getSubscriptionObject(subscriptionId);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(subscriptionType, "Subscription");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {

            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/countries/count
     *
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("countries/count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getAllCountries() throws CountryNotFoundException {

        try {
            Map<Integer, Collection<DomainObject>> countryMaps
                    = countryFacade.getCountriesWithCount();

            int key = (Integer) countryMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> countryList = countryMaps.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(countryList, key, "countries");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/countries/nocount
     *
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("countries/nocount")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getCountriesWithoutCount() throws CountryNotFoundException {

        try {
            Collection<DomainObject> countryList
                    = countryFacade.getCountriesWithoutCount();

            String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(countryList, "countries");
            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("/countries/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getCountryById(@PathParam("id") Integer countryId)
            throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.getCountryById(countryId);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "Country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/countries?pgNo=1&pgSize=10
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("countries")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getCountriesPaginated(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws CountryNotFoundException {

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        try {

            Map<Integer, Collection<DomainObject>> countryMapList = countryFacade.getCountriesPaginated(pageNumber, pageSize);

            int key = (Integer) countryMapList.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> countryList = countryMapList.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(countryList, key, "countries");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123/callcodes
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("/country/{id}/callcodes")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getCallingCodes(@PathParam("id") Integer countryId)
            throws CountryNotFoundException {

        try {
            Set<String> callingCodes = countryFacade.getCountryCallingCodes(countryId);

            if (callingCodes.isEmpty()) {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
            String jsonStr = JsonGenerator.getStringListAsJsonStr(callingCodes, "callingCodes");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123/dialcodes
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("/country/{id}/dialcodes")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getDiallingCodes(@PathParam("id") Integer countryId) throws CountryNotFoundException {

        try {
            Set<String> diallingCodes = countryFacade.getCountryDiallingCodes(countryId);

            if (diallingCodes.isEmpty()) {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }

            String jsonStr = JsonGenerator.getStringListAsJsonStr(diallingCodes, "diallingCodes");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123/phoneformats
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("/country/{id}/phoneformats")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getPhoneFormats(@PathParam("id") Integer countryId) throws CountryNotFoundException {

        try {
            Collection<Integer> phoneFormats = countryFacade.getCountryPhoneFormats(countryId);

            if (phoneFormats.isEmpty()) {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }

            String jsonStr = JsonGenerator.getIntegerListAsJsonStr(phoneFormats, "phoneformats");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/Country
     *
     * @param newCountry
     * @return
     */
    @POST
    @Path("/add/country")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addCountry(CountryEO newCountry) {

        try {
            CountryEO country = countryFacade.addCountry(newCountry);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: PUT /ut/edit/country
     *
     * @param updatedCountry
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @PUT
    @Path("/edit/country")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response updateCountry(CountryEO updatedCountry) throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.editCountry(updatedCountry);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/country/123
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @DELETE
    @Path("/delete/country/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeCountry(@PathParam("id") Integer countryId) throws CountryNotFoundException {

        try {
            countryFacade.removeCountry(countryId);

            return Response.ok("", MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/country/123/callCode/234
     *
     * @param countryId
     * @param callingCode
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @POST
    @Path("/add/country/{id}/callCode/{value}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addCallingCode(@PathParam("id") Integer countryId, @PathParam("value") String callingCode)
            throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.addCallingCodeToCountry(countryId, callingCode);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/country/123/callCode/234
     *
     * @param countryId
     * @param callingCode
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @DELETE
    @Path("/delete/country/{id}/callCode/{value}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeCallingCode(@PathParam("id") Integer countryId, @PathParam("value") String callingCode)
            throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.removeCallingCodeFromCountry(countryId, callingCode);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/country/123/dialCode/234
     *
     * @param countryId
     * @param diallingCode
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @POST
    @Path("/add/country/{id}/dialCode/{value}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addDiallingCode(@PathParam("id") Integer countryId, @PathParam("value") String diallingCode)
            throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.addDiallingCodeToCountry(countryId, diallingCode);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/country/123/dialCode/234
     *
     * @param countryId
     * @param diallingCode
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @DELETE
    @Path("/delete/country/{id}/dialCode/{value}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeDiallingCode(@PathParam("id") Integer countryId,
            @PathParam("value") String diallingCode) throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.removeDiallingCodeFromCountry(countryId, diallingCode);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/country/123/phoneformat/12
     *
     * @param countryId
     * @param phoneFormat
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @POST
    @Path("/add/country/{id}/phoneformat/{value}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addPhoneFormat(@PathParam("id") Integer countryId, @PathParam("value") int phoneFormat)
            throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.addPhoneFormatToCountry(countryId, phoneFormat);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/country/123/phoneformat/15
     *
     * @param countryId
     * @param phoneFormat
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @DELETE
    @Path("delete/country/{id}/phoneformat/{value}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removePhoneFormat(@PathParam("id") Integer countryId,
            @PathParam("value") int phoneFormat) throws CountryNotFoundException {

        try {
            CountryEO country = countryFacade.removePhoneFormatFromCountry(countryId, phoneFormat);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123/states?pgNo=1&pgSize=10
     *
     * @param pageNumber
     * @param pageSize
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("country/{id}/states")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getCountryStates(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize,
            @PathParam("id") Integer countryId) throws CountryNotFoundException {

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        try {
            Map<Integer, Collection<StateProvEO>> statesMap
                    = countryFacade.getCountryStateListPaginated(countryId, pageNumber, pageSize);

            int key = (Integer) statesMap.keySet().toArray()[0];
            if (key > 0) {
                Collection<StateProvEO> accountTypes = statesMap.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(accountTypes, key, "states");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * For example: GET /ut/currencies/count
     *
     * @return
     * @throws com.cometbid.ut.exceptions.CurrencyNotFoundException
     */
    @GET
    @Path("currencies/count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getAllCurrencies() throws CurrencyNotFoundException {

        try {
            Map<Integer, Collection<DomainObject>> currencyMaps
                    = currencyFacade.getCurrenciesWithCount();

            int key = (Integer) currencyMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> currencyList = currencyMaps.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(currencyList, key, "Currencies");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/currencies/nocount
     *
     * @return
     * @throws com.cometbid.ut.exceptions.CurrencyNotFoundException
     */
    @GET
    @Path("currencies/nocount")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getCurrenciesWithoutCount() throws CurrencyNotFoundException {

        try {
            Collection<DomainObject> currencyList
                    = currencyFacade.getCurrenciesWithoutCount();

            String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(currencyList, "Currencies");
            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/currencies/123
     *
     * @param currencyId
     * @return
     * @throws com.cometbid.ut.exceptions.CurrencyNotFoundException
     */
    @GET
    @Path("/currencies/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getCurrencyById(@PathParam("id") Integer currencyId)
            throws CurrencyNotFoundException {

        try {
            CurrencyEO currency = currencyFacade.getCurrencyById(currencyId);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(currency, "Currency");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/currencies?pgNo=1&pgSize=10
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws com.cometbid.ut.exceptions.CurrencyNotFoundException
     */
    @GET
    @Path("currencies")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getCurrenciesPaginated(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws CurrencyNotFoundException {

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        try {
            Map<Integer, Collection<DomainObject>> currencyMapList = currencyFacade.getCurrenciesPaginated(pageNumber, pageSize);

            if (currencyMapList.isEmpty()) {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }

            int key = (Integer) currencyMapList.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> countryList = currencyMapList.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(countryList, key, "Currencies");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/currency
     *
     * @param newCurrency
     * @return
     */
    @POST
    @Path("/add/currency")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addCurrency(CurrencyEO newCurrency) {

        try {
            CurrencyEO currency = currencyFacade.addCurrency(newCurrency);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(currency, "currency");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: PUT /ut/edit/currency
     *
     * @param updatedCurrency
     * @return
     * @throws com.cometbid.ut.exceptions.CurrencyNotFoundException
     */
    @PUT
    @Path("/edit/currency")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response updateCurrency(CurrencyEO updatedCurrency) throws CurrencyNotFoundException {

        try {
            CurrencyEO currency = currencyFacade.editCurrency(updatedCurrency);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(currency, "currency");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/currencies/123
     *
     * @param currencyId
     * @return
     * @throws com.cometbid.ut.exceptions.CurrencyNotFoundException
     */
    @DELETE
    @Path("/delete/currencies/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeCurrency(@PathParam("id") Integer currencyId) throws CurrencyNotFoundException {

        try {
            currencyFacade.removeCurrency(currencyId);

            return Response.ok("", MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/languages/count
     *
     * @return
     * @throws com.cometbid.ut.exceptions.LanguageNotFoundException
     */
    @GET
    @Path("languages/count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getAllLanguages() throws LanguageNotFoundException {

        try {
            Map<Integer, Collection<DomainObject>> languageMaps
                    = languageFacade.getLanguagesWithCount();

            int key = (Integer) languageMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> languageList = languageMaps.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(languageList, key, "Languages");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/languages/nocount
     *
     * @return
     */
    @GET
    @Path("languages/nocount")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getLanguagesWithoutCount() throws LanguageNotFoundException {

        try {
            Collection<DomainObject> languageList
                    = languageFacade.getLanguagesWithoutCount();

            if (languageList.isEmpty()) {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }

            String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(languageList, "Languages");
            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/languages/123
     *
     * @param languageId
     * @return
     * @throws com.cometbid.ut.exceptions.LanguageNotFoundException
     */
    @GET
    @Path("/languages/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getLanguageById(@PathParam("id") Integer languageId)
            throws LanguageNotFoundException {

        try {
            LanguageEO language = languageFacade.getLanguageById(languageId);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(language, "Language");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/languages?pgNo=1&pgSize=10
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws com.cometbid.ut.exceptions.LanguageNotFoundException
     */
    @GET
    @Path("languages")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getLanguagesPaginated(@DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws LanguageNotFoundException {

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        try {
            Map<Integer, List<DomainObject>> languageMapList = languageFacade.getLanguagesPaginated(pageNumber, pageSize);

            if (languageMapList.isEmpty()) {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }

            int key = (Integer) languageMapList.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> languageList = languageMapList.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(languageList, key, "Languages");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/language
     *
     * @param newLanguage
     * @return
     */
    @POST
    @Path("/add/language")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addCurrency(LanguageEO newLanguage) {

        try {
            LanguageEO language = languageFacade.addLanguage(newLanguage);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(language, "language");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: PUT /ut/edit/language
     *
     * @param updatedLanguage
     * @return
     * @throws com.cometbid.ut.exceptions.LanguageNotFoundException
     */
    @PUT
    @Path("/edit/language")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response updateLanguage(LanguageEO updatedLanguage) throws LanguageNotFoundException {

        try {
            LanguageEO language = languageFacade.editLanguage(updatedLanguage);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(language, "language");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/language/123
     *
     * @param languageId
     * @return
     * @throws com.cometbid.ut.exceptions.LanguageNotFoundException
     */
    @DELETE
    @Path("/delete/language/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeLanguage(@PathParam("id") Integer languageId) throws LanguageNotFoundException {

        try {
            languageFacade.removeLanguage(languageId);

            return Response.ok("", MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/regions/count
     *
     * @return
     * @throws com.cometbid.ut.exceptions.RegionNotFoundException
     */
    @GET
    @Path("regions/count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getAllRegions() throws RegionNotFoundException {

        try {
            Map<Integer, Collection<DomainObject>> regionMaps
                    = regionFacade.getRegionsWithCount();

            int key = (Integer) regionMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<DomainObject> regionList = regionMaps.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(regionList, key, "Regions");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/regions/nocount
     *
     * @return
     * @throws com.cometbid.ut.exceptions.RegionNotFoundException
     */
    @GET
    @Path("regions/nocount")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getRegionsWithoutCount() throws RegionNotFoundException {

        try {
            Collection<DomainObject> regionList
                    = regionFacade.getRegionsWithoutCount();

            String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(regionList, "Regions");
            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/regions/123
     *
     * @param regionId
     * @return
     * @throws com.cometbid.ut.exceptions.RegionNotFoundException
     */
    @GET
    @Path("/regions/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getRegionById(@PathParam("id") Integer regionId)
            throws RegionNotFoundException {

        try {
            RegionEO region = regionFacade.getRegionById(regionId);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(region, "Region");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/states/country/123
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("states/country/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getCountryStates(@PathParam("id") Integer countryId) throws CountryNotFoundException {

        try {
            List<StateProvEO> statesList = countryFacade.getCountryStateList(countryId);

            if (!statesList.isEmpty()) {
                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(statesList, "States");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123/states/count
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("country/{id}/states/count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getStatesWithCount(@PathParam("id") Integer countryId)
            throws StateProvNotFoundException, CountryNotFoundException {

        try {
            Map<Integer, Collection<StateProvEO>> statesMaps
                    = stateProvFacade.getStateProvWithCount(countryId);

            int key = (Integer) statesMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<StateProvEO> statesList = statesMaps.get(key);

                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(statesList, key, "States");
                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/country/123/states/nocount
     *
     * @param countryId
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @GET
    @Path("country/{id}/states/nocount")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getStatesWithoutCount(@PathParam("id") Integer countryId)
            throws StateProvNotFoundException, CountryNotFoundException {

        try {
            Collection<StateProvEO> statesList
                    = stateProvFacade.getStateProvWithoutCount(countryId);

            String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(statesList, "States/Provinces");
            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/states?pgNo=1&pgSize=50
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @GET
    @Path("states")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response getAllStatesPaginated(
            @DefaultValue("1") @QueryParam("pgNo") int pageNumber,
            @DefaultValue("10") @QueryParam("pgSize") int pageSize) throws StateProvNotFoundException {

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        try {
            Map<Integer, List<StateProvEO>> statesMaps
                    = stateProvFacade.getStateProvPaginated(pageNumber, pageSize);

            int key = (Integer) statesMaps.keySet().toArray()[0];
            if (key > 0) {
                Collection<StateProvEO> statesList = statesMaps.get(key);
                String jsonStr = JsonGenerator.getInstance().getCollectionAsJson(statesList, key, "States/Provinces");

                return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok("{}", MediaType.APPLICATION_JSON)
                        .status(Response.Status.NO_CONTENT).build();
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/states/123
     *
     * @param statesId
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @GET
    @Path("/states/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response getStatesById(@PathParam("id") Integer statesId)
            throws StateProvNotFoundException {

        try {
            StateProvEO country = stateProvFacade.getStateProvById(statesId);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(country, "Country");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: POST /ut/add/country/123/state
     *
     * @param countryId
     * @param newStateProv
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     */
    @POST
    @Path("/add/country/{id}/state")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addCountry(@PathParam("id") Integer countryId, StateProvEO newStateProv)
            throws CountryNotFoundException {

        try {
            StateProvEO stateProv = stateProvFacade.addStateProv(newStateProv, countryId);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(stateProv, "State/Province");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: PUT /ut/edit/country/123/state
     *
     * @param countryId
     * @param updatedStateProv
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @PUT
    @Path("/edit/country/{id}/state")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response updateStateProv(@PathParam("id") Integer countryId, StateProvEO updatedStateProv)
            throws CountryNotFoundException, StateProvNotFoundException {

        try {
            StateProvEO stateProv = stateProvFacade.editStateProv(updatedStateProv, countryId);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(stateProv, "State/Province");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();

        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/country/123/state/321
     *
     * @param countryId
     * @param statesId
     * @return
     * @throws com.cometbid.ut.exceptions.CountryNotFoundException
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @DELETE
    @Path("/delete/country/{countryId}/state/{statesId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeStatesProv(@PathParam("countryId") Integer countryId,
            @PathParam("statesId") Integer statesId)
            throws CountryNotFoundException, StateProvNotFoundException {

        try {
            stateProvFacade.removeStateProv(countryId, statesId);

            return Response.ok("", MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/add/states/123/city
     *
     * @param stateId
     * @param cityToAdd
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @POST
    @Path("add/states/{id}/city")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addCityToState(@PathParam("id") Integer stateId, StateCitiesEO cityToAdd)
            throws StateProvNotFoundException {

        try {
            StateProvEO stateProv = stateProvFacade.addCityToState(stateId, cityToAdd);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(stateProv, "State/Province");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: GET /ut/add/states/123/lga
     *
     * @param stateId
     * @param lgaToAdd
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @POST
    @Path("add/states/{id}/lga")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response addLgaToState(@PathParam("id") Integer stateId, StateLgaEO lgaToAdd)
            throws StateProvNotFoundException {

        try {
            StateProvEO stateProv = stateProvFacade.addLgaToState(stateId, lgaToAdd);

            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(stateProv, "State/Province");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/state/123/city
     *
     * @param stateId
     * @param cityToRemove
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @DELETE
    @Path("/delete/state/{id}/city")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeCityFromStatesProv(@PathParam("id") Integer stateId, StateCitiesEO cityToRemove)
            throws StateProvNotFoundException {

        try {
            StateProvEO stateProv = stateProvFacade.removeCityFromState(stateId, cityToRemove);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(stateProv, "State/Province");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For example: DELETE /ut/delete/state/123/lga
     *
     * @param stateId
     * @param lgaToRemove
     * @return
     * @throws com.cometbid.ut.exceptions.StateProvNotFoundException
     */
    @DELETE
    @Path("/delete/state/{id}/lga")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
    public Response removeLgaFromStatesProv(@PathParam("id") Integer stateId, StateLgaEO lgaToRemove)
            throws StateProvNotFoundException {

        try {
            StateProvEO stateProv = stateProvFacade.removeLgaFromState(stateId, lgaToRemove);
            String jsonStr = JsonGenerator.getInstance().getTypeAsJson(stateProv, "State/Province");

            return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
