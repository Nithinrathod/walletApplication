package com.tcs.apiGateway.filter;

import com.tcs.apiGateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            
            // 1. ALTERNATIVE: Get the header list directly to avoid .containsKey() issues
            List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

            // 2. Check if it is null or empty
            if (authHeaders == null || authHeaders.isEmpty()) {
                throw new RuntimeException("Missing Authorization Header");
            }

            // 3. Extract the token (safely get the first value)
            String authHeader = authHeaders.get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            try {
                // 4. Validate Token
                jwtUtil.validateToken(authHeader);
                String userId = jwtUtil.extractUserId(authHeader);
                
                // 5. Mutate the request to pass the userId downstream
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .build();

                // 6. Important: Pass the MUTATED exchange to the chain
                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception e) {
                System.out.println("Invalid Access: " + e.getMessage());
                throw new RuntimeException("Unauthorized access to application");
            }
        };
    }

    public static class Config {}
}