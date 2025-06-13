package com.teebay.teebayapi.interceptor;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AuthInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request,
                                              WebGraphQlInterceptor.Chain chain) {
        // get userId from authorization header
        String raw = request.getHeaders().getFirst("Authorization");
        UUID userId = null;
        try {
            userId = (raw != null) ? UUID.fromString(raw.trim()) : null;
        } catch (IllegalArgumentException ignored) {
            // anonymous
        }

        // put in graphql context
        UUID finalUserId = userId;
        request.configureExecutionInput((executionInput, builder) ->
            builder.graphQLContext(ctx -> {
                if (finalUserId != null) {
                    ctx.put("userId", finalUserId);
                }
            }).build()
        );

        return chain.next(request);
    }
}
