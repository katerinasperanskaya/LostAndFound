//package com.tus.lostAndFound.util;
//
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret:DefaultSecretKeyIfNotFound}")
//    private String secret;
//
//    public static final long JWT_EXPIRATION_MS = 3_600_000; // Token expires after 1 hour
//
//    // Generate Token for Users
//    public String generateUserToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", userDetails.getAuthorities());
//        claims.put("userType", "USER"); //  Mark token as a USER token
//        return createToken(claims, userDetails.getUsername());
//    }
//
//    // Generate Token for Admins
//    public String generateAdminToken(UserDetails adminDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", adminDetails.getAuthorities());
//        claims.put("userType", "ADMIN"); //  Mark token as an ADMIN token
//        return createToken(claims, adminDetails.getUsername());
//    }
//
//    // Create Token with Claims
//    private String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    // Extract Email from Token
//    public String extractEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // Extract User Type (USER or ADMIN)
//    public String extractUserType(String token) {
//        return extractClaim(token, claims -> (String) claims.get("userType"));
//    }
//
//    // Extract Specific Claim
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    // Extract All Claims
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    // Validate Token
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String email = extractEmail(token);
//        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    // Check if Token is Expired
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // Extract Token Expiration Date
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//}
//
