/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.lorgen.chatmate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
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
@DeclareRoles({UserGroup.ADMIN, UserGroup.USER})
public class AuthService {

    @PersistenceContext
    EntityManager em;

    @GET
    @Path("login")
    public Response login(@Context SecurityContext sc,
            @Context HttpServletRequest request) {
        request.getSession(true);
        
        return Response.ok(getSecureRole(sc, request)).build();
    }

    
    @GET
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException ex) {
            Logger.getLogger(AuthService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok().cookie((NewCookie[])null).build();
    }
    
    @GET @Path("create")
    //@RolesAllowed(UserGroup.ADMIN)
    public SecureUser createUser(@QueryParam("uid") String uid, @QueryParam("pwd") String pwd) {
        SecureUser result = null;
        try {
            // Gets the UTF-8 byte array and create a SHA-256 hash
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(pwd.getBytes("UTF-8"));
            
            // Creates a new SecureUser object with a Base64 encoded version of the hashed password
            result = new SecureUser(uid, Base64.getEncoder().encodeToString(hash));
            
            // Inserts SecureUser into the database
            em.persist(result);
            
            // Inserts UserGroup into the database.
            em.persist(new UserGroup(UserGroup.USER,uid));
        } catch(UnsupportedEncodingException | NoSuchAlgorithmException e) {
            Logger.getLogger(AuthService.class.getName()).log(Level.WARNING, "message",e);
        }

        return result;
    }
    @GET @Path("status")
    public JsonObject getSecureRole(@Context SecurityContext sc,
                                    @Context HttpServletRequest request) {
        JsonArrayBuilder cookies = Json.createArrayBuilder();
        int length = request.getCookies() != null ? request.getCookies().length : 0;
        for(int i = 0; i < length; i++) {
            Cookie c = request.getCookies()[i];
            cookies.add(Json.createObjectBuilder()
               .add("name", c.getName())
               .add("value", c.getValue())
               .add("maxAge", c.getMaxAge())
               .add("secure", c.getSecure())
               .add("httpOnly", c.isHttpOnly())
               .add("version", c.getVersion())
            );
        }

        Principal user = request.getUserPrincipal();
        String authScheme = sc.getAuthenticationScheme() != null ? sc.getAuthenticationScheme() : "null";
        return Json.createObjectBuilder()
                .add("userid", user != null ? user.getName() : "not logged in")
                .add("authScheme", authScheme)
                .add("admin",  Boolean.toString(sc.isUserInRole("admin")))
                .add("user",  Boolean.toString(sc.isUserInRole("user")))
                .add("cookies",cookies)
                .build();
    }
}
