package org.example.services;

import java.util.ArrayList;

public interface IServicePost<T> {

    void add(T t);

    ArrayList<T> getAll();

    void update(T t);

    void delete(int id);

    // Method to find an item by its ID or another unique identifier
    T findById(int id);

    // Method to get items based on a specific attribute
    ArrayList<T> getByAttribute(String attributeName, String value);
}
