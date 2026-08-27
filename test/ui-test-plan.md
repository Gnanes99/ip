# Console UI Test Plan

## Test environment

- Java version: Java 25
- Main class: `Dennis`
- Source files: `src/main/java/*.java`
- Isolation: Compile into a temporary directory and start a fresh program for each case.
- Comparison: Compare the complete standard output exactly, normalizing only CRLF/LF line endings.
- Failure behavior: Stop at the first compilation or output mismatch; do not run later cases.

## Test cases

### TC-01: Add and list all task types

**Aim:** Verify that ToDo, Deadline, and Event commands create correctly formatted tasks and that `list` displays all of them.

**Input:**

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
Understood. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
Understood. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Understood. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-02: Handle invalid commands and task numbers

**Aim:** Verify that invalid commands, missing command details, and invalid task numbers produce error messages without stopping the chatbot.

**Input:**

```text
todo
deadline return book
event meeting /from 2pm
mark
mark abc
mark 1
unmark abc
unmark 1
delete abc
delete 1
blah
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
ERROR!! I'm sorry, todo must contain a task.
_____________________________________________________
ERROR!! Use /by to specify the deadline.
_____________________________________________________
ERROR!! Use /from and /to to specify the duration of the event.
_____________________________________________________
ERROR!! Please enter a task number.
_____________________________________________________
ERROR!! The task number must be an integer.
_____________________________________________________
ERROR!! That task number exceeds the tasks.
_____________________________________________________
ERROR!! The task number must be an integer.
_____________________________________________________
ERROR!! That task number exceeds the tasks.
_____________________________________________________
ERROR!! The task number must be an integer.
_____________________________________________________
ERROR!! That task number exceeds the tasks.
_____________________________________________________
ERROR!! I'm sorry, I don't understand what you are trying to say :(
_____________________________________________________
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-03: Reject a deadline with no description

**Aim:** Verify that a deadline with a date but no task description produces its specific error message.

**Input:**

```text
deadline /by Sunday
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
ERROR!! I'm sorry, deadline must contain a task.
_____________________________________________________
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-04: Delete a task

**Aim:** Verify that `delete` removes the selected task, reports the removed task, updates the task count, and shifts the remaining task numbers forward.

**Input:**

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
delete 2
list
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
Understood. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Understood. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Understood. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Understood. I've removed this task:
  [D][ ] return book (by: Sunday)
Now there are 2 tasks in the list.
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-05: Saving to disk does not disturb the console output

**Aim:** Verify that the Level 7 auto-save (writing `./data/duke.txt` after every
change to the task list) runs silently: adding all three task types, marking,
unmarking, and deleting must still produce exactly the normal console output,
with no extra lines, warnings, or stack traces on standard output. The contents
of the save file are checked separately by inspecting `./data/duke.txt` after the
run; with this input it should contain:

```text
T | 0 | read book
E | 1 | project meeting | Mon 2pm | 4pm
```

**Input:**

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
unmark 1
delete 2
mark 2
list
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
Understood. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Understood. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Understood. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Excellent! I've marked this task as done:
  [T][X] read book
Alright, I've marked this task as not done yet:
  [T][ ] read book
Understood. I've removed this task:
  [D][ ] return book (by: Sunday)
Now there are 2 tasks in the list.
Excellent! I've marked this task as done:
  [E][X] project meeting (from: Mon 2pm to: 4pm)
Here are the tasks in your list:
1.[T][ ] read book
2.[E][X] project meeting (from: Mon 2pm to: 4pm)
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-06: Load saved tasks on start-up

**Aim:** Verify that when `./data/duke.txt` already exists, the chatbot reads it
on start-up so the previous tasks (including their done/not-done state) are
available immediately, before the user enters any command.

**Precondition — create `./data/duke.txt` with exactly this content before running:**

```text
T | 0 | read book
D | 1 | return book | Sunday
E | 0 | project meeting | Mon 2pm | 4pm
```

**Input:**

```text
list
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-07: Start cleanly when no save file exists

**Aim:** Verify that on a fresh machine, where `./data/duke.txt` does not exist
yet, the chatbot starts normally with an empty list and no error on standard
output.

**Precondition:** `./data/duke.txt` must not exist.

**Input:**

```text
list
todo first task
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
Here are the tasks in your list:
Understood. I've added this task:
  [T][ ] first task
Now you have 1 tasks in the list.
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-08: Reject a task field containing the save-file separator

**Aim:** The save file separates fields with `" | "`, so a `|` inside a
description or date would corrupt the file. Verify such input is rejected with
a clear error and does not stop the chatbot; well-formed input after it still
works.

**Input:**

```text
todo grocery | list
todo buy milk
list
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
ERROR!! A task description cannot contain the '|' character.
_____________________________________________________
Understood. I've added this task:
  [T][ ] buy milk
Now you have 1 tasks in the list.
Here are the tasks in your list:
1.[T][ ] buy milk
Bye. Looking forward to seeing you again!
_____________________________________________________
```

### TC-09: Recover from a corrupted save file

**Aim:** Verify that a save file containing several unreadable lines does not
stop start-up: the readable tasks are still loaded and listed, and the bad
lines are skipped. (The per-line "Skipping unreadable line ..." notes are
written to the error stream, so they do not appear in the standard output
compared here; check them separately with `2>` redirection if desired.)

**Precondition — create `./data/duke.txt` with exactly this content before running:**

```text
T | 1 | valid todo
GARBAGE
D | 0 | no date
X | 0 | wrong type | z
E | 0 | ok event | Mon | Tue
```

**Input:**

```text
list
bye
```

**Expected output:**

```text
 ____                   _     
|  _ \  ___ _ __  _ __ (_)___ 
| | | |/ _ \ '_ \| '_ \| / __|
| |_| |  __/ | | | | | | \__ \
|____/ \___|_| |_|_| |_|_|___/

Hi, my name is Dennis. It is lovely to meet you!
How may I help you today?
_____________________________________________________
Here are the tasks in your list:
1.[T][X] valid todo
2.[E][ ] ok event (from: Mon to: Tue)
Bye. Looking forward to seeing you again!
_____________________________________________________
```

## Session record

After a run, report the input and actual output in the conversation. Do not store transient session results in this plan unless the user asks for them to be saved.
