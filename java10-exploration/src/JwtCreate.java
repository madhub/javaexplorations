import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.util.X509CertUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class JwtCreate {

    public static final String MARKER_START = "-----BEGIN RSA PRIVATE KEY-----";
    public static final String MARKER_END = "-----END RSA PRIVATE KEY-----";

    public static void main(String[] args) throws JOSEException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //createJwtWithRSA();
        // sreadFromPem();


    }

    private static void readFromPem(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(Paths.get(fileName));

        String pemContent = new String(bytes, US_ASCII);
        int markerStart = pemContent.indexOf(MARKER_START);
        if (markerStart < 0) {
            System.out.println("Invalid pem");
        } else {
            String buf = pemContent.substring(markerStart + MARKER_START.length());
            int markerEnd = buf.indexOf(MARKER_END);
            if (markerEnd < 0) {
                System.out.println("Invalid pem");
            } else {
                buf = buf.substring(0, markerEnd);
                buf = buf.replaceAll("\\s", "");
                byte[] decodedBytes = Base64.getDecoder().decode(buf);

                KeyFactory kf = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedBytes);
                RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
                System.out.println(privKey.getAlgorithm());
            }
        }
    }

    public static void createJwtWithRSA () throws JOSEException {
    // RSA signatures require a public and private RSA key pair, the public key
    // must be made known to the JWS recipient in order to verify the signatures
        RSAKey rsaJWK = null;
        try {
            rsaJWK = new RSAKeyGenerator(2048)
                    .generate();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();

    // Create RSA-signer with the private key
        JWSSigner signer = null;
        try {
            signer = new RSASSASigner(rsaJWK);
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                claimsSet);

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                new Payload("In RSA we trust!"));
        jwsObject.sign(signer);
        System.out.println(jwsObject.serialize());
    // Compute the RSA signature
        signedJWT.sign(signer);

    // To serialize to compact form, produces something like
    // eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2L
    // mZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3Qd
    // maXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7
    // -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
        String s = signedJWT.serialize();
        //System.out.println(s);
    }
    public static void createJWKFromJavaRSAKey(){
        // Generate the RSA key pair
        KeyPairGenerator gen = null;
        try {
            gen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();

        // Convert to JWK format
        JWK jwk = new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
                .privateKey((RSAPrivateKey)keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .build();

        // Output the private and public RSA JWK parameters
        System.out.println(jwk);

        // Output the public RSA JWK parameters only
        System.out.println(jwk.toPublicJWK());
    }
    public static void generateRsaJWKUsingJoseLib(){

        // Generate 2048-bit RSA key pair in JWK format, attach some metadata
        RSAKey jwk = null;
        try {
            jwk = new RSAKeyGenerator(2048)
                    .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key
                    .keyID(UUID.randomUUID().toString()) // give the key a unique ID
                    .generate();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        // Output the private and public RSA JWK parameters
        System.out.println(jwk);

        // Output the public RSA JWK parameters only
        System.out.println(jwk.toPublicJWK());
    }
}
