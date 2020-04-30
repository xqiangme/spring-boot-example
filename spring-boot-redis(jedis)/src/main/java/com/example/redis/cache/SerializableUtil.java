package com.example.redis.cache;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class SerializableUtil {

    /**
     * 序列化
     */
    public static byte[] serialize(Object obj) {
        if (null == obj) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);
            objectOut.writeObject(obj);
            byte[] b = byteArrayOut.toByteArray();
            objectOut.close();
            byteArrayOut.close();
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反序列化
     *
     * @param value
     * @return
     */
    public static Object deserialize(byte[] value) {
        if (null == value) {
            return null;
        }
        try {
            ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(value);
            ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);
            Object obj = objectIn.readObject();
            objectIn.close();
            byteArrayIn.close();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        Map<String, List<Object>> map = new HashMap<>();
        map.put("1", Arrays.asList("1001,1002,1003,1004".split(",")));
        map.put("2", Arrays.asList("2001,2002,2003,2004".split(",")));
        map.put("3", Arrays.asList("3001,3002,3003,3004".split(",")));

        System.out.println(map);


        byte[] serialize = serialize(map);
        System.out.println(Arrays.toString(serialize));

        Object deserialize = deserialize(serialize);
        if (deserialize instanceof Map) {
            System.out.println(deserialize);

        }

        System.out.println(DigestUtils.md5Hex("123456"));

    }
}
