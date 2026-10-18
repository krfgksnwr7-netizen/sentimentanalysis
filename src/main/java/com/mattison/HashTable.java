package com.mattison;

import java.util.LinkedList;

public class HashTable<Key, Value> {

    // Represents one key-value pair
    private static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Hash table variables
    private LinkedList<Entry<Key, Value>>[] buckets;
    private int capacity;
    private int size;

    // Maximum load factor is 50%
    private static final double LOAD_FACTOR_THRESHOLD = 0.5;

    // Constructor initializes the hash table
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {

        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be greater than zero"
            );
        }

        capacity = initialCapacity;
        size = 0;

        buckets = (LinkedList<Entry<Key, Value>>[])
                new LinkedList[capacity];

        // Create an empty linked list in every bucket
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    // Insert a new entry or update an existing one
    public void put(Key key, Value value) {

        int index = hash(key);

        // Check whether the key already exists
        for (Entry<Key, Value> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // Expand before inserting if the table would exceed 50%
        if ((double) (size + 1) / capacity
                > LOAD_FACTOR_THRESHOLD) {

            rehash();
            index = hash(key);
        }

        // Add the new entry
        buckets[index].add(new Entry<>(key, value));
        size++;
    }

    // Alias for put()
    public void insert(Key key, Value value) {
        put(key, value);
    }

    // Find a value using its key
    public Value get(Key key) {

        int index = hash(key);

        for (Entry<Key, Value> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    // Alias for get()
    public Value lookup(Key key) {
        return get(key);
    }

    // Double the bucket count and redistribute entries
    @SuppressWarnings("unchecked")
    private void rehash() {

        // Save the existing buckets
        LinkedList<Entry<Key, Value>>[] oldBuckets = buckets;

        // Double the capacity
        capacity = capacity * 2;

        // Create a new bucket array
        buckets = (LinkedList<Entry<Key, Value>>[])
                new LinkedList[capacity];

        // Initialize every new bucket
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }

        // Rehash all existing entries
        for (LinkedList<Entry<Key, Value>> bucket : oldBuckets) {

            for (Entry<Key, Value> entry : bucket) {

                // Calculate the index using the new capacity
                int index = hash(entry.key);

                // Insert into the appropriate new bucket
                buckets[index].add(entry);
            }
        }
    }

    // Calculate the hash table index
    private int hash(Key key) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    // Return the number of entries
    public int size() {
        return size;
    }

    // Return the current number of buckets
    public int capacity() {
        return capacity;
    }
}