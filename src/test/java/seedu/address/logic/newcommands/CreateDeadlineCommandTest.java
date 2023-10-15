package seedu.address.logic.newcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.newcommands.CreateDeadlineCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.newcommands.exceptions.CommandException;
import seedu.address.model.UserPrefs;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.RelativePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.path.exceptions.UnsupportedPathOperationException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;
import seedu.address.model.statemanager.State;
import seedu.address.model.statemanager.StateManager;
import seedu.address.model.statemanager.TaskOperation;
import seedu.address.model.taskmanager.Deadline;
import seedu.address.model.taskmanager.Task;
import seedu.address.model.taskmanager.TaskList;
import seedu.address.testutil.GroupBuilder;
import seedu.address.testutil.StudentBuilder;


class CreateDeadlineCommandTest {
    @Test
    public void execute_deadlineForStudentAccepted_addSuccessful() throws InvalidPathException,
            UnsupportedPathOperationException {
        AbsolutePath currPath = new AbsolutePath("~/grp-001/stu-001");
        List<Task> defaultTaskList = new ArrayList<>();
        defaultTaskList.add(new Deadline("Assignment 3", LocalDateTime.parse("2023-12-03T23:59")));
        TaskList taskList = new TaskList(defaultTaskList);
        Map<Id, Group> groups = new HashMap<>();
        Group grp = new GroupBuilder().build();
        groups.put(grp.getId(), grp);
        Root root = new Root(taskList, groups);

        LocalDateTime duedate = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline = new Deadline("Assignment 3", duedate);

        RelativePath path = new RelativePath("~/grp-001/stu-001");
        AbsolutePath absolutePath = currPath.resolve(path);

        TaskOperation student = StateManager.taskOperation(root, absolutePath);
        student.addTask(deadline);

        CommandResult returnStatement =
                new CommandResult(String.format(MESSAGE_SUCCESS, student.toString()));

        assertEquals(String.format(CreateDeadlineCommand.MESSAGE_SUCCESS, student),
                returnStatement.getFeedbackToUser());
    }

    public void execute_deadlineForAllStudentsInGroupAccepted_addSuccessful() throws InvalidPathException,
            UnsupportedPathOperationException, CommandException {
        AbsolutePath currPath = new AbsolutePath("~");
        Map<Id, Student> studentMap = new HashMap<>();
        Student alice = new StudentBuilder()
                .withName("Alice")
                .withEmail("alice@example.com")
                .withPhone("94351253")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withId("stu-001").build();
        Student bob = new StudentBuilder()
                .withName("Bob")
                .withEmail("johnd@example.com")
                .withPhone("98765432")
                .withAddress("311, Clementi Ave 2, #02-25")
                .withTags("owesMoney", "friends")
                .withId("stu-002").build();
        studentMap.put(alice.getId(), alice);
        studentMap.put(bob.getId(), bob);
        Group grp = new Group(new TaskList(null), studentMap, new Name("AmazingGroup"), new GroupId("grp-001"));
        Map<Id, Group> groups = new HashMap<>();
        groups.put(new GroupId("grp-001"), grp);
        Root root = new Root(new TaskList(null), groups);

        LocalDateTime duedate = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline = new Deadline("Assignment 3", duedate);

        RelativePath path = new RelativePath("~/grp-001/");

        CreateDeadlineCommand command = new CreateDeadlineCommand(path, deadline, "allStu");
        State state = new State(currPath, root, new UserPrefs());

        assertTrue(alice.checkDuplicates(deadline));
        assertTrue(bob.checkDuplicates(deadline));
    }

    @Test
    public void execute_deadlineForAllGroupsInRootAccepted_addSuccessful() throws InvalidPathException,
            UnsupportedPathOperationException, CommandException {
        AbsolutePath currPath = new AbsolutePath("~");
        Map<Id, Student> studentMap = new HashMap<>();
        List<Task> defaultTaskList = new ArrayList<>();
        defaultTaskList.add(new Deadline("Assignment 1", LocalDateTime.parse("2023-12-03T23:59")));
        TaskList taskList1 = new TaskList(defaultTaskList);
        TaskList taskList2 = new TaskList(defaultTaskList);
        Group grp1 = new Group(taskList1, studentMap, new Name("AmazingGroup"), new GroupId("grp-001"));
        Group grp2 = new Group(taskList2, studentMap, new Name("AmazingGroup"), new GroupId("grp-002"));
        Map<Id, Group> groups = new HashMap<>();
        groups.put(new GroupId("grp-001"), grp1);
        groups.put(new GroupId("grp-002"), grp2);
        Root root = new Root(new TaskList(null), groups);

        LocalDateTime duedate = LocalDateTime.parse("2023-12-03T23:58");
        Deadline deadline = new Deadline("Assignment 3", duedate);

        RelativePath path = new RelativePath("~");

        CreateDeadlineCommand command = new CreateDeadlineCommand(path, deadline, "allGrp");
        State state = new State(currPath, root, new UserPrefs());

        assertTrue(grp1.checkDuplicates(deadline));
        assertTrue(grp2.checkDuplicates(deadline));
    }

    @Test
    public void execute_duplicateDeadline_throwsCommandException() throws InvalidPathException {
        AbsolutePath currPath = new AbsolutePath("~/grp-001/");
        List<Task> defaultTaskList = new ArrayList<>();
        defaultTaskList.add(new Deadline("Assignment 1", LocalDateTime.parse("2023-12-03T23:59")));
        TaskList taskList = new TaskList(defaultTaskList);

        Map<Id, Group> groups = new HashMap<>();
        Group grp = new GroupBuilder().build();
        groups.put(new GroupId("grp-001"), grp);
        Root root = new Root(taskList, groups);

        State state = new State(currPath, root, new UserPrefs());

        RelativePath path = new RelativePath("~/grp-001/stu-001");
        Deadline deadline = new Deadline("Assignment 3", LocalDateTime.parse("2023-12-03T23:59"));

        CreateDeadlineCommand createDeadlineCommand = new CreateDeadlineCommand(path, deadline);

        assertThrows(CommandException.class,
                CreateDeadlineCommand.MESSAGE_DUPLICATE_DEADLINE_TASK, (
                    ) -> createDeadlineCommand.execute(state)
        );
    }

    @Test
    void equals_sameInstanceForStudentDirectory_success() throws InvalidPathException {
        RelativePath path = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline = new Deadline("Assignment 1", duedate);
        CreateDeadlineCommand command = new CreateDeadlineCommand(path, deadline);

        assertTrue(command.equals(command));
    }

    @Test
    void equals_sameInstanceForGroupDirectory_success() throws InvalidPathException {
        RelativePath path = new RelativePath("~/grp-001");
        LocalDateTime duedate = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline = new Deadline("Assignment 1", duedate);
        CreateDeadlineCommand command = new CreateDeadlineCommand(path, deadline);

        assertTrue(command.equals(command));
    }

    @Test
    void equals_sameCommandForStudentDirectory_success() throws InvalidPathException {
        RelativePath path1 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate1 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline1 = new Deadline("Assignment 1", duedate1);
        CreateDeadlineCommand command1 = new CreateDeadlineCommand(path1, deadline1);

        RelativePath path2 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate2 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline2 = new Deadline("Assignment 1", duedate2);
        CreateDeadlineCommand command2 = new CreateDeadlineCommand(path2, deadline2);

        assertTrue(command1.equals(command2));
    }

    @Test
    void equals_sameCommandForGroupDirectory_success() throws InvalidPathException {
        RelativePath path1 = new RelativePath("~/grp-001");
        LocalDateTime duedate1 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline1 = new Deadline("Assignment 1", duedate1);
        CreateDeadlineCommand command1 = new CreateDeadlineCommand(path1, deadline1);

        RelativePath path2 = new RelativePath("~/grp-001");
        LocalDateTime duedate2 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline2 = new Deadline("Assignment 1", duedate2);
        CreateDeadlineCommand command2 = new CreateDeadlineCommand(path2, deadline2);

        assertTrue(command1.equals(command2));
    }

    @Test
    void equals_differentDescriptionForStudentDirectory_fail() throws InvalidPathException {
        RelativePath path1 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate1 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline1 = new Deadline("Assignment 1", duedate1);
        CreateDeadlineCommand command1 = new CreateDeadlineCommand(path1, deadline1);

        RelativePath path2 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate2 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline2 = new Deadline("Assignment 2", duedate2);
        CreateDeadlineCommand command2 = new CreateDeadlineCommand(path2, deadline2);

        assertFalse(command1.equals(command2));
    }

    @Test
    void equals_differentPathForStudentDirectory_fail() throws InvalidPathException {
        RelativePath path1 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate1 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline1 = new Deadline("Assignment 1", duedate1);
        CreateDeadlineCommand command1 = new CreateDeadlineCommand(path1, deadline1);

        RelativePath path2 = new RelativePath("~/grp-001/stu-002");
        LocalDateTime duedate2 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline2 = new Deadline("Assignment 1", duedate2);
        CreateDeadlineCommand command2 = new CreateDeadlineCommand(path2, deadline2);

        assertFalse(command1.equals(command2));
    }

    @Test
    void equals_differentDueDateForStudentDirectory_fail() throws InvalidPathException {
        RelativePath path1 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate1 = LocalDateTime.parse("2023-12-03T23:58");
        Deadline deadline1 = new Deadline("Assignment 1", duedate1);
        CreateDeadlineCommand command1 = new CreateDeadlineCommand(path1, deadline1);

        RelativePath path2 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate2 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline2 = new Deadline("Assignment 1", duedate2);
        CreateDeadlineCommand command2 = new CreateDeadlineCommand(path2, deadline2);

        assertFalse(command1.equals(command2));
    }

    @Test
    void equals_differentFieldsForStudentDirectory_fail() throws InvalidPathException {
        RelativePath path1 = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate1 = LocalDateTime.parse("2023-12-03T23:58");
        Deadline deadline1 = new Deadline("Assignment 1", duedate1);
        CreateDeadlineCommand command1 = new CreateDeadlineCommand(path1, deadline1);

        RelativePath path2 = new RelativePath("~/grp-001/stu-002");
        LocalDateTime duedate2 = LocalDateTime.parse("2023-12-03T23:59");
        Deadline deadline2 = new Deadline("Assignment 2", duedate2);
        CreateDeadlineCommand command2 = new CreateDeadlineCommand(path2, deadline2);

        assertFalse(command1.equals(command2));
    }

    @Test
    void toString_sameStringForStudentDirectory_success() throws InvalidPathException {
        RelativePath path = new RelativePath("~/grp-001/stu-001");
        LocalDateTime duedate = LocalDateTime.parse("2023-12-03T23:58");
        Deadline deadline = new Deadline("Assignment 1", duedate);
        CreateDeadlineCommand command = new CreateDeadlineCommand(path, deadline);
        String expected = "seedu.address.logic.newcommands.CreateDeadlineCommand{"
                        + "toCreateDeadline=Deadline has been added:\n"
                        + "[D][ ] Assignment 1(by: 2023-12-03T23:58)}";

        assertEquals(expected, command.toString());
    }

}

