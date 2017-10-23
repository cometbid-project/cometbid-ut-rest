/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ng.emts.eng.vas.mappers;

import com.cometbid.ut.exceptions.RegionNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**s
 *
 * @author Gbenga
 */
@Provider
public class RegionNotFoundMapper implements ExceptionMapper<RegionNotFoundException> {

    /**
     *
     * @param ex
     * @return
     */
    @Override
    public Response toResponse(RegionNotFoundException ex) {
        return Response.status(421).
                entity(ex.getMessage()).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}