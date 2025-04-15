package tn.esprit.interfaces;

import java.util.ArrayList;

public interface IServiceLikes<T> {

    void add(T t);

    ArrayList<T> getAll();

    void update(T t);

    boolean delete(T t);

    T findById(int id);

    ArrayList<T> getByPostId(int postId);

    ArrayList<T> getByUserId(int userId);
}
