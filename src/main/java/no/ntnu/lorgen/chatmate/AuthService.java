/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.lorgen.chatmate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import no.ntnu.lorgen.chatmate.domain.SecureUser;
import no.ntnu.lorgen.chatmate.domain.UserGroup;

/**
 *
 * @author klorg
 */
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
@DeclareRoles({UserGroup.ADMIN,UserGroup.USER})
public class AuthService {
    @PersistenceContext
    EntityManager em;
    
    @Path("login")
    @GET
    public Response login(@Context SecurityContext sc,
                          @Context HttpServletRequest request) {
        request.getSession(true);
        return Response.ok(request.getUserPrincipal().getName() + " ### " + request.getRequestURL()).build();
    }
    
    @Path("create")
    @RolesAllowed(UserGroup.ADMIN)
    @GET
    public SecureUser createUser(@QueryParam("uid")String uid,
                                 @QueryParam("pwd")String pwd) {
        SecureUser result = null;
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(pwd.getBytes("UTF-8"));
            result = new SecureUser(uid, Base64.getEncoder().encodeToString(hash));
                    em.persist(result);
                    em.persist(new UserGroup(UserGroup.USER,uid));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(AuthService.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return result;
    }
}
