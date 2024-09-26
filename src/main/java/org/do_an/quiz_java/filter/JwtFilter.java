package org.do_an.quiz_java.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.exceptions.TokenExpiredException;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;

    @Value("${api.v1.prefix}")
    private String apiPrefix;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");

            if (isNonAuthRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String username = jwtGenerator.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = (User) userDetailsService.loadUserByUsername(username);

                    try {
                        if (jwtGenerator.isValidToken(token, user)) {  // Ensure the token is valid for the user
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            user,
                                            null,
                                            user.getAuthorities());

                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                            filterChain.doFilter(request, response);
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Invalid Token");
                        }
                    } catch (TokenExpiredException e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token has expired: " + e.getMessage());
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization header is missing or invalid");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }

    private boolean isNonAuthRequest(HttpServletRequest request) {
        final List<Pair<String, String>> nonAuthRequests = List.of(


                Pair.of("/api/v1/users/create", "POST"),
                // Swagger
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET"),

                // heath check
                Pair.of("/actuator", "GET"),
                Pair.of("/actuator/**", "GET"),

                // Login
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
//                Pair.of(String.format("%s/users/**", apiPrefix), "PUT"),
//                Pair.of(String.format("%s/users/**", apiPrefix), "GET"),
//                Pair.of(String.format("%s/roles", apiPrefix), "GET"),


                //Menu
                Pair.of(String.format("%s/menu/**", apiPrefix), "GET"),

//                Pair.of(String.format("%s/menu", apiPrefix), "GET"),
//                Pair.of(String.format("%s/menu/**", apiPrefix), "DELETE"),
//                Pair.of(String.format("%s/menu/**", apiPrefix), "PUT"),

                //Web-setting
                Pair.of(String.format("%s/web-settings", apiPrefix), "GET"),
                Pair.of(String.format("%s/web-settings/**", apiPrefix), "PUT"),


                // Category
//                Pair.of(String.format("%s/categories", apiPrefix),"GET"),
//                Pair.of(String.format("%s/categories/**", apiPrefix),"POST"),
                Pair.of(String.format("%s/categories/**", apiPrefix), "GET"),

                // notification
                Pair.of(String.format("%s/notifications/**", apiPrefix), "POST"),

                // orders
                Pair.of(String.format("%s/orders", apiPrefix), "POST"),
                Pair.of(String.format("%s/orders/getOrderDetails", apiPrefix), "GET"),

                // Table
                Pair.of(String.format("%s/tables/all", apiPrefix), "GET"),

                //home
                Pair.of("/home/**", "GET"),

                //Pay
                Pair.of(String.format("%s/payment/**", apiPrefix), "POST"),

                // img
                Pair.of(String.format("%s/menu/getUrlImage", apiPrefix), "POST"),

                // device token

                Pair.of(String.format("%s/device-tokens", apiPrefix), "POST")


        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> nonAuthRequest : nonAuthRequests) {
            String path = nonAuthRequest.getFirst();
            String method = nonAuthRequest.getSecond();


            if (requestPath.matches(path.replace("**", ".*"))
                    && requestMethod.equalsIgnoreCase(method)) {
                return true;
            }

        }

        return false;
    }

}
