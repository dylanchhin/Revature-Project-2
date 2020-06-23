package auction.dataaccess;
import java.util.List;

public interface DAO <T, ID> {
    /***
     * Saves the current object to persistent storage.
     * @param obj Object to be Saved
     * @return Returns whether or not the operation succeeded
     */
    boolean save(T obj);

    /***
     * This method returns the all the objects of type T from persistent storage.
     * @return Returns whether or not the operation succeeded
     */
    List<T> retrieveAll();

    /***
     * This method retrieves an object of type T from persistent storage identified by the value passed which is of type ID.
     * @param id Id of the object to be retrieved
     * @return Returns whether or not the operation succeeded
     */
    T retrieveByID(ID id);

    /***
     * This method deletes all objects of type T in persistent storage.
     * @param obj Deletes the specified object
     * @return Returns whether or not the operation succeeded
     */
    boolean delete(T obj);

    /***
     * This method updates the object of type T in persistent storage.
     * @param newObj Creates a new object
     * @return Returns whether or not the operation succeeded
     */
    boolean update(T newObj);

}