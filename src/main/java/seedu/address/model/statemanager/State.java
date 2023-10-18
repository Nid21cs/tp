package seedu.address.model.statemanager;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;
import seedu.address.ui.Displayable;

/**
 * The API of the State component.
 */
public interface State {
    //=========== UserPrefs ==================================================================================
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    public ReadOnlyUserPrefs getUserPrefs();

    public GuiSettings getGuiSettings();

    public void setGuiSettings(GuiSettings guiSettings);

    public Path getAddressBookFilePath();

    public void setAddressBookFilePath(Path addressBookFilePath);

    //=========== ProfBook State ================================================================================
    /**
     * Return current directory.
     */
    public AbsolutePath getCurrPath();

    /**
     * Return Root.
     */
    public Root getRoot();

    /**
     * Return true if current path has task list.
     */
    public boolean hasTaskListInCurrentPath();

    /**
     * Return true if current path has children list.
     */
    public boolean hasChildrenListInCurrentPath();

    /**
     * Returns true if group in given path exists in the ProfBook.
     * {@code path} must be path with group information.
     * i.e. Group directory or Student Directory.
     */
    public boolean hasGroup(AbsolutePath path);

    /**
     * Returns true if student exists in the ProfBook.
     * {@code path} must be student path.
     */
    public boolean hasStudent(AbsolutePath path);

    /**
     * Returns true if given path exists in the ProfBook.
     */
    public boolean hasPath(AbsolutePath path);

    /**
     * Change directory to destination path
     * {@code path} must exist in ProfBook and is not student path.
     */
    public void changeDirectory(AbsolutePath path);

    //=========== Display Panel Settings =============================================================
    /**
     * Return the current list shown on display panel.
     */
    public ObservableList<Displayable> getDisplayList();

    /**
     * Update list with latest data according to
     * current display path and display content.
     */
    public void updateList();

    /**
     * Set the path to be shown on display panel.
     * {@code path} must exist in ProfBook.
     */
    public void setDisplayPath(AbsolutePath path);

    /**
     * Return true if current display path has task list.
     */
    public boolean hasTaskListInDisplayPath();

    /**
     * Return true if current display path has children list.
     */
    public boolean hasChildrenListInDisplayPath();

    /**
     * Displays a children list on the display panel.
     * This method should be called after checking that the current display path contains children list
     * by using the {@link hasChildrenListInDisplayPath} method.
     */
    public void showChildrenList();

    /**
     * Displays a task list on the display panel.
     * This method should be called after checking that the current display path contains task list
     * by using the {@link hasTaskListInDisplayPath} method.
     */
    public void showTaskList();

    //=========== State Management Operation =============================================================

    /**
     * Creates a ChildOperation class that performs operation on root.
     */
    public ChildOperation<Group> rootChildOperation();

    /**
     * Creates a ChildOperation that performs operation on the specified group.
     * {@code path} must be a directory with group information and exist in the ProfBook.
     */
    public ChildOperation<Student> groupChildOperation(AbsolutePath path);

    /**
     * Creates a TaskOperation that performs task operation on the specified task list.
     * {@code path} must be a directory with task list and exist in the ProfBook.
     */
    public TaskOperation taskOperation(AbsolutePath path);

    //=========== Helper Method =============================================================
    /**
     * Return true if given path has task list.
     */
    public boolean hasTaskListInPath(AbsolutePath path);

    /**
     * Return true if given path has children list.
     */
    public boolean hasChildrenListInPath(AbsolutePath path);
}
