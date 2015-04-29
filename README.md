=============================================================================================================================
                                                GENERAL INFORMATION

GW-MANAGER project is used for uploading and managing "Quiz" data: questions, backups etc. This project is supposed to be used together with GW-SERVER (https://github.com/java-fan/gw-server), which main role is recieving, storing and managing question information data, user records likewise managing database and securing requests.

The project is based on Java Swing GUI toolkit and is used as manager for GAE Server. Generally manager consists of two modules: Web Services part and GUI part. The manager is able to:

- upload and manage question information data (possible answers, correct answer, relevant question images)
- manage database: dowbload/upload backup

=============================================================================================================================
                                                TECHNOLOGY STACK

- Java 8
- Swing (GUI)
- Jersey client (Web Services requests)
- Spring Framework (IoC/DI)
- Maven (Build tool)
