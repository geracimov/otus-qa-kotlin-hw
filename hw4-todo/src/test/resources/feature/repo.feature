Feature: Task management in task repository

  Scenario: Creating a task
    Given Task repository
    When Task is created
    Then Task count in task repository are equals 1

  Scenario: Getting the task list
    Given Task repository
    And Task repository has 3 active tasks
    When Repository getTasks called
    Then Task count in task repository are equals 3

  Scenario: Getting a list of unfinished tasks
    Given Task repository
    And Task repository has 2 active tasks
    And Task repository has 1 completed tasks
    Then Uncompleted task count in task repository are equals 2

  Scenario: Getting a list of completed tasks
    Given Task repository
    And Task repository has 2 active tasks
    And Task repository has 1 completed tasks
#  todo may be TasksRepositoryMemory incorrect behaviour. May be method must return 1
    Then Completed task count in task repository are equals 3

  Scenario: Deleting a task
    Given Task repository
    And Task repository has 2 active tasks
    And Task repository has 1 completed tasks
    When Task would delete with id 2
    Then Task count in task repository are equals 2

  Scenario: Deleting a task that does not exist
    Given Task repository
    And Task repository has 2 active tasks
    And Task repository has 1 completed tasks
    When Task would delete with id 999
    Then Task count in task repository are equals 3