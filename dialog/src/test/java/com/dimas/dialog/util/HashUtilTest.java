package com.dimas.dialog.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HashUtilTest {

    @Test
    void name() {
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();
        var userId3 = UUID.randomUUID();
        var list1 = List.of(userId1, userId2);
        var list2 = List.of(userId2, userId1);
        var list3 = List.of(userId1, userId3);
        var hash1 = HashUtil.hashInvariant(list1);
        var hash2 = HashUtil.hashInvariant(list2);
        var hash3 = HashUtil.hashInvariant(list3);
        assertEquals(hash1, hash2);
        assertEquals(hash2, hash1);
        assertNotEquals(hash1, hash3);
        assertNotEquals(hash2, hash3);
    }

    @Test
    void name2() {
        var userId1 = UUID.randomUUID().toString();
        var userId2 = UUID.randomUUID().toString();
        var userId3 = UUID.randomUUID().toString();
        var list1 = List.of(userId1, userId2);
        var list2 = List.of(userId2, userId1);
        var list3 = List.of(userId1, userId3);
        var hash1 = HashUtil.md5HashInvariant(list1);
        var hash2 = HashUtil.md5HashInvariant(list2);
        var hash3 = HashUtil.md5HashInvariant(list3);
        assertEquals(hash1, hash2);
        assertEquals(hash2, hash1);
        assertNotEquals(hash1, hash3);
        assertNotEquals(hash2, hash3);
    }

    @Test
    void name3() {
//        UUID user1= UUID.fromString("370c779a-3570-49e4-b066-34f1c06f494d");//Леонардо
        UUID user1= UUID.fromString("19bc1d82-b91f-4759-a62a-c8d199e29358");//Леонель
//        UUID user2= UUID.fromString("10775498-ae09-4e13-a2ff-f7c1e190043f");//ЛедиГага
        UUID user2= UUID.fromString("abbdd14d-9dfb-434d-8696-fbffa0d52d5d");//Леополь
        System.out.println(HashUtil.getDialogHash(user1, user2));
    }

}