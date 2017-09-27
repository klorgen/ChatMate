/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.lorgen.chatmate;

import java.util.Collections;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import no.ntnu.lorgen.chatmate.domain.Chat;
import no.ntnu.lorgen.chatmate.domain.Message;
import no.ntnu.lorgen.chatmate.domain.SecureUser;

/**
 *
 * @author klorg
 */
@Stateless
@Path("messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatService {

    @PersistenceContext
    EntityManager em;

    @GET
    public List<Message> getMessages(@QueryParam("name") String name) {
        List<Message> result = null;
        if (name != null) {
            result = em.createQuery("SELECT m FROM Message m WHERE m.chat.id = :id",
                    Message.class)
                    .setParameter("id", name)
                    .getResultList();
        }
        return result != null ? result : Collections.EMPTY_LIST;
    }

    @GET
    @Path("chatrooms")
    public List<Chat> getChats() {
        return em.createNamedQuery(Chat.QUERY_FINDALL, Chat.class)
                .getResultList();
    }

    @POST
    @Path("add")
    //@RolesAllowed({"user"})
    public Response addMessage(@QueryParam("user") String user,
            @QueryParam("name") String name,
            Message message) {
        if (name != null) {
            Chat c = em.find(Chat.class, name);
            if (c == null) {
                c = new Chat(name);
                em.persist(c);
            }

            message.setChat(c);
            em.persist(message);

            return Response.ok(message).build();
        } else {
            return Response.noContent().build();
        }
    }

}
