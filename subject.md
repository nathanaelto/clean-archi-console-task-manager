# Console task manager

## Purpose

Our objective is to build a task manager accessible from the command line.


## A task

A task is a description of an activity to be done.
This task have these additional properties:
- creation date
- due date (optional)
- close date (when status changed to close)
- state (predefined values: `[todo, pending, progress, done, cancelled, closed]`), _`todo` will be the
  default when a new task is created_
- Subtasks (optional): an array of tasks

## Data

The persistence of the data of the tasks should be accessible and readable within simple text
files: csv, json or even text files. It could be one file per task or one file for all tasks.

The persistence of the data should be done in a folder called ".consoleAgenda" under the profile
of the user.
For example : `/users/rui/.consoleagenda/data.json`

## UI

The application should be a simple command line application able to interact with the data.

- list the tasks by creation date (first is more recent)
- add a new task
- remove a task
- update task status
- (optional) show tasks in different colors depending on their status

Each command need to unitary and self standing. A command do an action producing a result,
then show the result and then quit the app (there is no interactive interface hiding some
commands within the process of execution of the app)

example of executions :
- `agenda add -c "hello world"`
- `agenda add -d:2022-03-01 -c "finalize the agenda exercise"`
- `agenda list`
- `agenda update 123 -d:2022-04-01`
- `agenda remove 123`
- `agenda update 123 -s:done`

_note: in our examples, command options `-c` means content, `-d` due date, `-s` status_




## Additional Rules

These are features of the app that are less important for a basic working app but we would
like to have them later

- (optional) each execution of the app is logged in a file `log.txt` stored in the application
  folder `~/.consoleagenda/` with :
    - this format: `[status][yyyy-MM-dd:HHhmm,ss] command_arguments : optional_error`
    - where status: `[ok+] | [err]`
    - ex: `[err][2022-02-10:10h55,23] update 1 "hello world" : task #1 does not exist`
    - ex: `[ok+][2022-02-10:10h56,00] add "hello world"`
- (optional) overdue tasks (= tasks with due date in the past) should be presented in red,
  first before the other tasks

## Building on quality

Even if it's look like a simple app, we want to ensure that it works as expected and that it will
be maintainable over time:
- there must be tests proving that the application is working as expected
- the design must be modular enough, respecting principles of composition of software to ensure
  separation of concerns, minimal coupling & maximal cohesion, stability of core parts and modularity
  of concrete parts (data, interfaces, file system, log ...)


## Sample Data

this json could be used as reference for your initial tests:

```json
[
  {
    "Created": "2022-02-15T22:14:30.486798+01:00",
    "DueDate": "2022-02-16T14:14:30.535929+01:00",
    "CloseDate": null,
    "Description": "init a project to create an app for my tasks",
    "State": 0,
    "Tag": null,
    "SubTasks": [
      {
        "Created": "2022-02-15T22:14:30.535975+01:00",
        "DueDate": null,
        "CloseDate": null,
        "Description": "init repo for the project",
        "State": 3,
        "Tag": null,
        "SubTasks": null
      },
      {
        "Created": "2022-02-15T22:14:30.536022+01:00",
        "DueDate": null,
        "CloseDate": null,
        "Description": "create solution and project",
        "State": 3,
        "Tag": null,
        "SubTasks": null
      },
      {
        "Created": "2022-02-15T22:14:30.536022+01:00",
        "DueDate": null,
        "CloseDate": null,
        "Description": "create a small poc to test formats",
        "State": 2,
        "Tag": null,
        "SubTasks": null
      }
    ]
  },
  {
    "Created": "2022-02-13T22:14:30.536023+01:00",
    "DueDate": "2022-02-16T21:14:30.536024+01:00",
    "CloseDate": null,
    "Description": "make a design of my needs to manage my tasks",
    "State": 2,
    "Tag": null,
    "SubTasks": null
  },
  {
    "Created": "2022-02-16T22:14:30.536024+01:00",
    "DueDate": "2022-03-16T22:14:30.536025+01:00",
    "CloseDate": null,
    "Description": "get first feedback on my app with peers",
    "State": 0,
    "Tag": null,
    "SubTasks": null
  },
  {
    "Created": "2022-01-16T22:14:30.536143+01:00",
    "DueDate": "2022-02-01T22:14:30.536144+01:00",
    "CloseDate": "2022-02-11T22:14:30.536144+01:00",
    "Description": "brainstorm on what project to work on",
    "State": 5,
    "Tag": null,
    "SubTasks": null
  }
]
```

## Sample program to generate data

This is the sample program used to generate this sample data:

```csharp
using System.Text.Json;
using System.Text.Json.Serialization;

ExerciceHelpers.CreateSampleData();

enum TaskState { todo, pending, progress, done, cancelled, closed}
record Task (
    DateTimeOffset Created, 
    DateTimeOffset? DueDate, 
    DateTimeOffset? CloseDate,
    string Description, 
    TaskState State = TaskState.todo, 
    string? Tag = null,
    IEnumerable<Task>? SubTasks = null);


static class ExerciceHelpers
{
    internal static void CreateSampleData()
    {
        var tasks = new[]
        {
            new Task(DateTimeOffset.Now.AddDays(-1), DateTimeOffset.Now.AddHours(-8), null ,"init a project to create an app for my tasks",
                SubTasks: new []
                {
                    new Task(DateTimeOffset.Now.AddDays(-1), null,null ,"init repo for the project", TaskState.done),
                    new Task(DateTimeOffset.Now.AddDays(-1), null, null ,"create solution and project", TaskState.done),
                    new Task(DateTimeOffset.Now.AddDays(-1), null, null ,"create a small poc to test formats", TaskState.progress)
                }),
            new Task(DateTimeOffset.Now.AddDays(-3), DateTimeOffset.Now.AddHours(-1), null ,"make a design of my needs to manage my tasks", TaskState.progress),
            new Task(DateTimeOffset.Now, DateTimeOffset.Now.AddMonths(1), null,
                "get first feedback on my app with peers"),
            new Task(DateTimeOffset.Now.AddMonths(-1), DateTimeOffset.Now.AddDays(-15), DateTimeOffset.Now.AddDays(-5),"brainstorm on what project to work on", TaskState.closed),
        };

        var jsonTasks = JsonSerializer.Serialize(tasks, new JsonSerializerOptions(){ WriteIndented = true});
        File.WriteAllText("data.txt",jsonTasks);
        var content = File.ReadAllText("data.txt");
        var newTasks = JsonSerializer.Deserialize<IEnumerable<Task>>(content);
        var serializedContentCheck = JsonSerializer.Serialize(newTasks,new JsonSerializerOptions(){ WriteIndented = true});

        Console.WriteLine($"content : {serializedContentCheck}");
    }
}
```

