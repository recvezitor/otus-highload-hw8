package com.dimas.core.util;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

public class HashUtil {

    public static Integer hashInvariant(List<UUID> list) {
        return list.stream()
                .mapToInt(Object::hashCode)
                .reduce(0, (left, right) -> left ^ right);
    }

    public static String getDialogHash(UUID user1, UUID user2) {
        return md5HashInvariant(List.of(user1.toString(), user2.toString()));
    }

    public static String md5HashInvariant(List<String> strings) {
        BigInteger hashSum = BigInteger.ZERO;
        for (String s : strings) {
            String hexHash = toHexString(md5Hex(s));
            hashSum = hashSum.add(new BigInteger(hexHash, 16));
        }
        return hashSum.toString(16);
    }

    @SneakyThrows
    public static byte[] md5Hex(String str) {
        byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(bytesOfMessage);
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
