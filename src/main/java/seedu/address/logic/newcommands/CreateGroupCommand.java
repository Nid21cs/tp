package seedu.address.logic.newcommands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.newcommands.exceptions.CommandException;
import seedu.address.model.path.RelativePath;
import seedu.address.model.path.exceptions.UnsupportedPathOperationException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Root;
import seedu.address.model.statemanager.ChildOperation;
import seedu.address.model.statemanager.State;
import seedu.address.model.statemanager.StateManager;

/**
 * Represents a command for creating a new group within ProfBook.
 * This command is typically used to create a new group at a specified path.
 */
public class CreateGroupCommand extends Command {

    public static final String COMMAND_WORD = "mkdir";

    public static final String MESSAGE_DUPLICATE_GROUP = "This group already exists in ProfBook";

    public static final String MESSAGE_SUCCESS = "New group added: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": group";

    private final RelativePath relativePath;

    private final Group group;

    /**
     * Constructs a {@code CreateGroupCommand} with the specified relative path and group details.
     *
     * @param relativePath  The relative path at which the new group will be created.
     * @param group The details of the group to be created.
     */
    public CreateGroupCommand(RelativePath relativePath, Group group) {
        requireAllNonNull(relativePath, group);
        this.relativePath = relativePath;
        this.group = group;
    }

    /**
     * Executes the CreateGroupCommand to create a new group within ProfBook at the specified path.
     *
     * @param state The current state of the application.
     * @return A CommandResult indicating the outcome of the execution.
     * @throws CommandException If an error occurs while executing the command.
     */
    @Override
    public CommandResult execute(State state) throws CommandException {
        requireAllNonNull(state);
        try {
            Root root = state.getRoot();
            ChildOperation<Group> target = StateManager.rootChildOperation(root);

            if (target.hasChild(this.group.getId())) {
                throw new CommandException(MESSAGE_DUPLICATE_GROUP);
            }

            target.addChild(this.group.getId(), this.group);
            state.updateList();
            return new CommandResult(String.format(MESSAGE_SUCCESS, this.group));
        } catch (UnsupportedPathOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if this CreateGroupCommand is equal to another object.
     *
     * @param other The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CreateGroupCommand)) {
            return false;
        }

        CreateGroupCommand otherCreateGroupCommand = (CreateGroupCommand) other;
        return this.relativePath.equals(otherCreateGroupCommand.relativePath)
                && this.group.equals(otherCreateGroupCommand.group);
    }

    /**
     * Returns a string representation of this CreateGroupCommand.
     *
     * @return A string representation of the CreateGroupCommand.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toCreateGroup", this.group)
                .toString();
    }
}
