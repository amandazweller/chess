# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

Sequence Diagram Phase 2: [https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDhLzir7x+QJeCg6B7gevjMMe6SZJg2TmGyxZphU0gAKK7kh9RIc0LQPqoT7dNOs7oGYP6eH+G6Qrau7QjAADio6shBp7QeezCytQ17UWhmH2KOeFBgRaBESuJHroE2A+FA2DcPAuqZDRo4pJBZ45Cxl5sQhtQNFxPHBHxjboEO3GjAAcqO35CWu-4BJYKDKhAyQwAAUhAPJyaMgQ6AgoANkxylwVe6mUneLSGSgvF1npz69JJwDWVAcAQAg0CzCFACS0hmb+IkBF4MVdl6sDANgkmEPEiQKYxMEXkUanlIhKFoRhrTGIJmBAA](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5xDAaTgALdvYoALIoAIyY9lAQAK7YMADEaMBUljAASij2SKoWckgQaAkA7r5IYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD00QZQADpoAN4ARKOUSQC2KDPNMzAzADQbuOpF0Byr61sbKIvASAhHGwC+mMJNMHWs7FyUrbPzUEsraxvbM12qn2UEOfxOMzOFyu4LubE43FgzweolaUEy2XKUAAFBksjlKBkAI7RNRgACU90aoie9Vk8iUKnUrUCYAAqmNsV8fpT6YplGpVLSjDpmgAxJCcGAcyh8mA6VLc4DLTB8xmCp7I6kqVpoaIIBBUkQqYVqgXMkDouQoGU4r583nadXqYXGZoKDgcaVjPlGqg055mpmqZqWlDWhTRMC+bHAKO+B2qp3moXPN0er2R6O+lEm6rPeFvFrpDEEqAZVT6rCFxGa+oPd4wT5jH7rVrHAFx6O9CAAa3QbY2tz9lDr8GQ5laACYnE5Js2FsqVjB2-8Nl3fD3+2hB8c7ugOKZIjE4vFoAFDAAZCBZQrxEplCoTmr1xqNzo9AbDAzqfJoedKss2xAiCHB3A2SL1DWjYLt8S7XBCIEHAhcKvLWWrGigrQIDekrYtet7EqSOSUrmhiBsmwYsig7KcoBKCOgyKauqKEpSracoKjA9F+gGr6Ya0VDAMgWjlDASGgrxeZ0pRgqhla5RZjGG6JkGGppqKGYwEpObauRUFoY2BGShWVaYNBSIYU0HwbPRCGdvGW4DuCMDgW+ln1JUk4wDOc7TLZLbweCDndn2znHK5SZMcGY5kbq+qGmRwoWc0UwbMs6jAIEqwbAAolAUTFtESTxtASAAF4oIckUWbF7kfDMGWqFlvx5QV0CtMVG5lZV1VudQo75p5z5gK0AAsTihPOjWCi1OUzPlhWdSV0Y9VVMz7pwR5RLECSRCg6AwARMTMPepTlJgXkvg0A3Fm00i5ZeuW9LlgxDD+qh-pMG5OWgI6QS8CIwTMP1hTuqFAwDcUwDh9gnfhN5dFGRFkqRemmrJzIwKySmxo5YOMfyMUaeKkqZvGnGpKD25SfpMiYyGHAoNwinxnjoXboTzqpvUbrSMzNGGDp2i08lhnFsdyNqGZtVDTd1lNpFEFjldo0+bO86RWpLpy9DeoGqLBbi6l6Wzdl7aLR13Erb4a19eZ4t1bdDVNXNFvtUVNt2+s-WPHLqvjZN02u+bbVLdb3VQBV62bYeEQ7ae6JepemIwAA4kuQpnY+l0jcKEGtG0afPW99hLt9+M08rRuQw11POUOG0O5D+d6dhmIZxl+Ed5nKMkYbMnRXJ2M0bj9doFzzEk2x5PZto8pU5X6AD-TQ9Y8gOQKAandqNik-E7zooZBYqA0DAwkIOnS66ZhYu10dPcZaZCDVo7uv1YrMxlxlOVtLM38oAAJLSHmmuGYD5yi2lbMFDYOgECgF7FAoKHYNgAIAHJLlhDAQYvtBrPADurPyaUv6Z1-v-JcwDQEQggTaQKyx7KwPgSARBdDfgoJIcsDByw1gbWwVrBmTt-Q6hgPrRK6Ma5FhNjNTKocFoe2WpHaO9tZZWWBiHVqcjw5dVKlHXqPt-oqxGoHKa0xpHNVkZbT2ii9Gx22ieBI2BohQGwNweAClDA72KOdJ8VRmCqLut0PogwDESMRDZDhKAuFsKHBDIsrcBIwDDNaHe2I4DuJ3n3CkUUibqUPu6T02MlwLyvpwpcK9tYhhHmAMeFNtD71ySKUmUphbyGKePcpDNWhJPKCkneUT6k6zyVpNAKAiglJQDfIRdMUpuPDD0pcz9X4t3fs7T+ADKGrn0dXfBRjCGawiRsxu-C16pisqiERCUV4pWIeo+aliFE6KUfolR-EFazFue7LRXtdHrSVu5QxvjjHBzNho+5EdHk2MwEMABFcOboHnOgpctj472PiJYZmOExkACkICSnGQkOBCDc6+PiQrTobIvzQvLjbX684XHAHRVAOAEAcJQG2Os6QuCAYzLSrMeljLmWsqOAAdRYIAl6QwABCl4FBwAANIQg5VQ1ysT0KvPOQAK1xWgFJOLJQZJQCSVGHSTnUWqWzFSdTsncxYk02eCZ55cXaUlCipqqkpI5QMnmjSZ7jMpoU5YwCV7QxAL4FAzCd5kHkGJAAZtAc+QlLi6EuGUSwJqclY1iKJFAniihlF8CI0Z4l4GMutVPdVwjRGG1Ce8G5IK7nyPPggK0HBUjyG3M8t+-jwkfLDlbYSLa23AA7X826ALvITQAMz7N7Zo-tzbwytpgO2gcxyM3evTAUr4y6IABsME6pef0Km2qzdaP18883Rhhgy0crxEksugFc42MA9U6oWdLF+zc4krLeaOv2OzAV7P8mum1P7zlVpdQZWudaZGgsbdo1aPzlFdteWo+tnyrYIdtkhrZ-z-a7ImiY956G+1WIhTHKFMKaVgwRUuKJdwzBbRRbteIkQGXjnDLAYA2AXGEDyAULxOdVakvfA9J6L03rGBCVByRHG8AwHREanIX61Xy3OSAbgeAFA8eQCAfjaA97pu5l0zTUBbSqEM8ekmMgBZiQvt6WUItIOr3XSZzjO8LNettTZlmhh7M70mXxFzxnEmmaUr9TzZaD6NJ84LJtl8WnAGrTJsJcnywfqWd+7tTYuXjowNODW0xbEHkwEAA)

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
