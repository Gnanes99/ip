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

## Session record

After a run, report the input and actual output in the conversation. Do not store transient session results in this plan unless the user asks for them to be saved.
