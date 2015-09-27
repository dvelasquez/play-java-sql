package com.nazca.sdk.collections.generics;

import java.util.AbstractMap.SimpleEntry;

public class NameValueCollection<K, V> extends java.util.ArrayList<SimpleEntry<K, V>> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public V get(K key) {
        int size = this.size();

        for (int index = 0; index < size; index++) {

            SimpleEntry<K, V> item = this.get(index);
            K compareKey = item.getKey();

            if (key.equals(compareKey)) {
                return item.getValue();
            }
        }
        return null;
    }

    public void add(K key, V value) {

        this.add(new SimpleEntry<>(key, value));

    }
}