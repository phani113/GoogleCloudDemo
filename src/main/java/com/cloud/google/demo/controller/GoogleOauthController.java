package com.cloud.google.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.cloud.google.demo.drive.GoogleCloudAuthServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GoogleOauthController {

    @Autowired
    private GoogleCloudAuthServiceImpl googleCloudAuthServiceImpl;
    private static String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus(@CookieValue("JSESSIONID") String JSESSIONID, HttpServletRequest request) throws Exception {
        return new RedirectView(googleCloudAuthServiceImpl.authorize());
    }

    @RequestMapping(value = "/oauth2callback", method = RequestMethod.GET, params = "code")
    public RedirectView oauth2Callback(@RequestParam(value = "code") String code, HttpServletRequest request) {

        googleCloudAuthServiceImpl.getDriveService(code);
        return new RedirectView("/getfilenames/10");


    }

   /* @GetMapping("/oauth_login")
    public String getLoginPage(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);

        return "oauth_login";
    }*/


}
