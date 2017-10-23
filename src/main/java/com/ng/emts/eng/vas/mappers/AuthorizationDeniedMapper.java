/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ng.emts.eng.vas.mappers;

import com.cometbid.ut.exceptions.AuthorizationDeniedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Gbenga
 */
@Provider
public class AuthorizationDeniedMapper implements ExceptionMapper<AuthorizationDeniedException> {

    /**
     *
     * @param ex
     * @return
     */
    @Override
    public Response toResponse(AuthorizationDeniedException ex) {
        return Response.status(450).
                entity(ex.getMessage()).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}
