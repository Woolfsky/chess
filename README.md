# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[Detailed Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAKJoAm2ESGZ2KRAGaEK1Qijr5CxYKQAiwMMACCIECgDOGzO0XAARsA0oY7fZkwBzKBACu2GAGI0wKgE8YAJRSWkGsFCKSBBoTgDuABZIYBKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0tsZQADpoAN5VlC4AtigANDC4WmHQ7J0oLcBICAC+mKLpMMmsHFw8YHwCSMIgk+JZUD5+MVAAFE1QrR1dRhq9UP0wg8MIAJSYbJzcvPxCImni0ykKSqrqWiylhQYAAqtUDtVjg9fio1JoNN9TPosmQWAAZFhwIowQ7HGCCGwtXHVTCw-4I6YzJ7zV7LVbrdRZNC2BAIR5zF6LN4rD5idRI8nwwEwEBbRQocGUSHNYBtTrdc59AZDEYwvQUrRIsxZACSADk0Z4cXi5SdFRcrjcRjADUV8iTaDSuUt3mtPgKZkKARosmKUBLlLYwBEZUczeq-sLETMdbbDSxjY7w-KYMBgxEihAANboeP2tMZska6NUlLOhau3nu-koLKNKFmzrpkNZ3NocYVuluxkmBIzSaUeum1MtzM59DjQewfspOLoMBZABMAAYV3UG7LRxm25PMOh2BZrHYHI5oLJgTB0RBfKFHJForFkAukdOsnlCqUKsYtME0BuR3NM5LSnNJKGSLtuXpPkqA2BAbx4MNjgVYC+geSCqwZD0+ySb0EQyYEwQhQDIzhH1tRRcgMSxE1GzaGAADFPHyABZZNiyjcjZ1mZ5Kx5LDa2HOigJ6ZVrlVMYOV47tq17QUSx9P1xRiIMQyQiMOLIylY0og0jVorcTjHXdQjtB0x00zUY2pTk+OgmtYKZGBNxTIydwnDspNpKCe2wstUmoIdnMA5t3PbUDApnGZ5wwZc1wA4TQtbDzxgPI8bHsJxrBQPNr0sOxmHvKIYkwGLmAHMDMhyORqKKFgynKH8ND-OpjI8iZKog2yZIExy6xgeD8uDPY2vbdDup82TsOSPCRUI1TQxIyzS2SOM0UxbEYAAKkY5i2LHOoAHUAAlExYZN8QAXkui6zWWriuukybeo2YLEsLZLwq8l1+Jg8QZoU-DTBQBAQRQBb1LaUirO+OMasxOrtt21iPoiI7Ts8c7AJga7brae7tJsp7ML+pyAlsCQMN+hyvlnN8YHJiRp1nMqMlXFd9w4dKTycLZ2CvHYYAAcTNREisfUrn0SFJ6eyIWWC-cpLDNVqwvQDrIu+Kn7N7LJkH8EW2g0Ea1bQcbiepuSAc4oH5ozSGUGhlaUjW6jNp2piUbHGB0bOvGTFxwCCa1Wdtd8wS3sMpLxy+767PDvrvlm30YEIw3NAdp2uJdyj1popHPbY5X6N9zGYAfGIpVcnGbux-JPB9tB9AQUBsyrq7a+E4PrKSMOpoj+pi80TsJpJmnPQqyL6yHjQIqmbjWZgdmNxn1KuasDLT2wWwoGwUH4GUkx0-CYqn3icqZcq98CmKBqh9Vz70BXs19TNOfKC10fLewpSAxidOTaPzQJ0Ier8oZxx6qTHCycgQgghktZOFFURuxxB7PaqNG4nT9tjQOXdEGhy-jrH+kdXLRxMiPC2RDaxJ0BiKNAKAwgAJCqnF+Gl8E5z1AmJMoCmwsLaLqOQ+YHRD27mWPuL0nKDzNAIih3kx69jplfZyQ8ZEa3SCzKWi5l4NBUXIcYnNDwbx5o4NwIN4JhBgAAKReMLM0Thm6t0lufV8Si8igkVvfFwpsNx72AKYqAcAIDwSgCA6Rei1HgV7oQhOr0ABWLwAGjXQAqFu-jAnBNCfwuQ5s5Hf2odbLSc04H2wQbQmMHCqIbVQcjfaGZMEYyxsJGu-tREEMoTEyRzCkmeXEVAgpVlYFgAAborOCJVoojzu7GpfCUCl3Oro5puju5tNyVQvq08wmyJ+ms-6uEylZGAOwdguRUnSl0Z0Zhvi0lBOgKMrQ4zOH6X9ikvxlB0nQCEXUIeMAsFlwWbjJZycVnbI6f1RmEDnp9OSPTcFzNkis3ZhYNKRjMqOGsH4+AoM8BpmwHvQgQQQgnwlmVFxU9qq1XqhUMwESZzlmif3dZwNQYxD2DkkFDLaY-H2UysGsJWWtIqfDBW500Eo0ONHTJEh8FE1WaCrI4Lenjz7NCpRsLOrws0WzNcBjMBAA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
