package dataaccessobjects;

import java.util.List;

public interface DAO <T, ID>{
    /***
     * Saves the current object to persistent storage.
     * @param obj
     * @return
     */
    boolean save(T obj);

    /***
     * This method returns the all the objects of type T from persistent storage.
     * @return
     */
    List<T> retrieveAll();

    /***
     * This method retrieves an object of type T from persistent storage identified by the value passed which is of type ID.
     * @param id
     * @return
     */
    T retrieveByID(ID id);

    /***
     * This method deletes all objects of type T in persistent storage.
     * @param obj
     */
    boolean delete(T obj);

    /***
     * This method updates the object of type T in persistent storage.
     * @param newObj
     */
    boolean update(T newObj);

}
