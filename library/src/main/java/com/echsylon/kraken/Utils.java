package com.echsylon.kraken;

import android.util.Base64;

import com.annimon.stream.Stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides internal helper functions.
 */
final class Utils {

    /**
     * Joins any provided non-empty string fragments into a comma separated
     * result.
     *
     * @param fragments The string fragments to join.
     * @return A comma separated string or null if no fragments provided.
     */
    static String join(String... fragments) {
        if (fragments == null || fragments.length <= 0)
            return null;

        String result = Stream.of(fragments)
                .reduce(new StringBuilder(),
                        (builder, fragment) ->
                                fragment != null && fragment.length() > 0 ?
                                        builder.append(",").append(fragment) :
                                        builder).toString();

        return result.length() > 1 ?
                result.startsWith(",") ?
                        result.substring(1) :
                        result :
                null;
    }

    /**
     * Returns a byte array as a string, encoding it with the platform default
     * encoding (UTF-8). Handles null gracefully.
     *
     * @param value The byte array to express as a string.
     * @return The value as a string or null if the input is null.
     */
    static String asString(byte[] value) {
        return value != null ?
                new String(value) :
                null;
    }

    /**
     * Returns the string value of a Long object. Handles null gracefully.
     *
     * @param value The object who's value to express as a string.
     * @return The value as a string or null if the input is null.
     */
    static String asString(Long value) {
        return value != null ?
                value.toString() :
                null;
    }

    /**
     * Returns the string value of a Integer object. Handles null gracefully.
     *
     * @param value The object who's value to express as a string.
     * @return The value as a string or null if the input is null.
     */
    static String asString(Integer value) {
        return value != null ?
                value.toString() :
                null;
    }

    /**
     * Returns the string value of a Boolean object. Handles null gracefully.
     *
     * @param value The object who's value to express as a string.
     * @return The "true" or "false" or null if the input is null.
     */
    static String asString(Boolean value) {
        return value != null ?
                value.toString() :
                null;
    }

    /**
     * Returns a string as a byte array. The string is expected to be encoded
     * with the platform default encoding (UTF-8).
     *
     * @param string The string to convert to a byte array.
     * @return The string byte array or null if the input is null.
     */
    static byte[] asBytes(String string) {
        if (string == null)
            return null;

        return string.getBytes();
    }

    /**
     * Concatenates the provided byte arrays in the given order to a single byte
     * array. Invalid byte arrays (like null pointers) are ignored.
     *
     * @param bytes The byte arrays to concatenate.
     * @return The resulting byte array (may be empty) or null if the input is
     * null.
     */
    static byte[] concat(byte[]... bytes) {
        return bytes != null ?
                Stream.of(bytes)
                        .reduce(new ByteArrayOutputStream(), (stream, array) -> {
                            if (array != null)
                                try {
                                    stream.write(array);
                                } catch (IOException e) {
                                    // ignore errors
                                }
                            return stream;
                        }).toByteArray() :
                null;
    }

    /**
     * Encrypt the provided byte array.
     *
     * @param message The data to encrypt.
     * @param secret  The encryption secret.
     * @return the encrypted byte array.
     */
    static byte[] hmacSha512(byte[] message, byte[] secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(secret, "HmacSHA512"));
            return mac.doFinal(message);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return null;
        }
    }

    /**
     * Encrypt the provided byte array.
     *
     * @param message The data to encrypt.
     * @return the encrypted byte array.
     */
    static byte[] sha256(byte[] message) {
        if (message == null)
            return null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(message);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Decodes the provided Base 64 encoded string.
     *
     * @param string The string to decode.
     * @return The decoded string as a byte array.
     */
    static byte[] base64Decode(String string) {
        return string != null ?
                Base64.decode(string, Base64.DEFAULT) :
                null;
    }

    /**
     * Encodes the provided byte array to a Base 64 string.
     *
     * @param bytes The bytes to encode.
     * @return The encoded byte array as a string.
     */
    static String base64Encode(byte[] bytes) {
        return bytes != null ?
                Base64.encodeToString(bytes, Base64.NO_WRAP) :
                null;
    }

}
