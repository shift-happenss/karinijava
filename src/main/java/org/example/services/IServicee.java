package org.example.services;


import java.util.List;

public interface IServicee<T> {
    boolean ajouter(T t);            // Ajoute un objet de type T
    void modifier(T t);              // Modifie un objet de type T
    boolean supprimer(int id);       // Supprime un objet de type T par son ID
    List<T> afficher();              // Affiche tous les objets de type T
    T trouverParId(int id);          // Trouve un objet de type T par son ID
}
