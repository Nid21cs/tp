package seedu.address.model.statemanager;

import seedu.address.model.id.Id;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Student;
import seedu.address.model.profbook.exceptions.DuplicateChildException;
import seedu.address.model.profbook.exceptions.NoSuchChildException;

/**
 * Encapsulate logic required to perform group operations
 */
public class GroupOperation extends StateManager implements IChildOperation<Student> {

    private static final String LOGGING_PREFIX = "In Group Operations, ";
    private final Group baseDir;

    /**
     * Constructs a new group operation method
     * @param baseDir - The group object to perform operations on
     */
    GroupOperation(Group baseDir) {
        super(baseDir);
        this.baseDir = baseDir;
        this.stateLogger(GroupOperation.LOGGING_PREFIX);
    }

    /**
     * Adds the child to list of children
     *
     * @param id    - Unique identifier of the child
     * @param child - The child in question
     * @throws DuplicateChildException If attempting to add child with the same ID
     */
    @Override
    public void addChild(Id id, Student child) throws DuplicateChildException {
        this.baseDir.addChild(id, child);
        this.stateLogger(GroupOperation.LOGGING_PREFIX + "adding" + child.toString());
    }

    /**
     * Deletes the child specified by the id
     *
     * @param id - Unique identifier of the child
     * @return The deleted Child
     * @throws NoSuchChildException If there is no such Child found
     */
    @Override
    public Student deleteChild(Id id) throws NoSuchChildException {
        this.stateLogger(GroupOperation.LOGGING_PREFIX + "deleting" + id.toString());
        return this.baseDir.deleteChild(id);
    }

    /**
     * Returns the child specified by the id
     *
     * @param id - Unique identifier of the child
     * @return The specified Child
     * @throws NoSuchChildException If there is no such Child found
     */
    @Override
    public Student getChild(Id id) throws NoSuchChildException {
        this.stateLogger(GroupOperation.LOGGING_PREFIX + "getting" + id.toString());
        return this.getChild(id);
    }

    /**
     * Returns a list of all current children
     *
     * @return list of all current children
     */
    @Override
    public Student[] getAllChildren() {
        this.stateLogger(GroupOperation.LOGGING_PREFIX + "getting all child");
        return this.baseDir.getAllChildren().toArray(new Student[0]);
    }
    
    /**
     * Returns Number of current children
     *
     * @return The Number of current children
     */
    @Override
    public int numOfChildren() {
        return this.numOfChildren();
    }
}
