# Building Smart Web Apps with AI

## Description
This repository contains the materials (slides and code) created for the International Summmer School course *Building 
Smart 
Web Apps with AI*.

### Course Contents
During the course, the following modules are covered:

0. [Schedule](./slides/0-Schedule.pdf)
1. [Introduction to AI](./slides/1-Introduction-to-AI.pdf)
2. [Language Models](./slides/2-Language-Models.pdf)
3. [Developing Web Applications with Plain Java](./slides/3-Developing-Web-Applications-with-Plain-Java.pdf)
4. [Integrating LLM into Applications](./slides/4-Integrating-LLM-in-applications.pdf)
5. [Prompt Engineering](./slides/5-Prompt-Engineering.pdf)
6. [Retrieval-Augmented Generation](./slides/6-Retrieval-Augmented-Generation-RAG.pdf)
7. [LLM Agents](./slides/7-LLM-Agents.pdf)
8. [Advanced RAG](./slides/8-Advanced-RAG.pdf)
9. [LLMOps](./slides/9-LLM-Ops.pdf)
10. [Closing](./slides/10-Closing.pdf)

## Smart Web App Project
In this repository, you will find the source code for a web project based on Java technologies.
The project was created using the Spring Boot, Vaadin, and LangChain4J frameworks.

The application offers the following functionalities:
1. Hello World
2. Book management (create, read, update, delete)
3. Text generator
4. Image generator
3. Image descriptor
4. Text translator
5. Text summarizer
6. Sentiment analyzer
7. Data extractor
8. Simple chat
9. Chat with memory
10. Basic assistant
11. Semantic PDF search service
12. Semantic Internet search 
13. Semantic database search
12. Assistant with PDF search
13. Assistant with Internet search
14. Assistant with database search
15. Agent for book management
16. Agent for asking Github

## Usage Instructions

### Modifying the Application
You can use this project in two different ways:
- From your development machine by cloning the project with *git clone* and working locally with your IDE.
- Using a GitHub *codespace*. A codespace is a cloud-based development environment that you can use 
  directly without needing any additional setup.

### Running the Application
The project is a standard Maven project. To run it from the command line, use `mvnw` (Windows) or `./mvnw` (Mac and Linux), then open http://localhost:8080 in your browser.

You can also import the project into your preferred IDE as you would with any Maven project. Read more about how to 
import Vaadin projects into different IDEs (Eclipse, IntelliJ IDEA, NetBeans, and VS Code). Then you can run the 
Application class.

### Project Structure
- The `pom.xml` file in the root directory contains the Maven dependencies and build configuration.
- The `src/main/resources` directory contains the application properties and static resources.
- The `src/main/frontend` contains custom CSS styles.
- The `src/main/java` directory contains the Java source code of the application.
  - The `es.uca.summerschool.smartwebapp` package is the root package of the application.
  - The `views` package contains the Vaadin views of the application.
  - The `services` package contains the service classes that implement the business logic of the application.
  - The `data` package contains the entity classes and the Spring Data repositories used by the application.
  - The `security` package contains the security configuration classes.
  - The `controllers` package contains the REST controllers for the application.

## Useful Links
- [Spring](https://spring.io)
- [Vaadin](https://vaadin.com)
- [LangChain4J](https://docs.langchain4j.dev/)

## Information about Guardrails
- [Top 20 LLM Guardrails With Examples](https://www.datacamp.com/blog/llm-guardrails?dc_referrer)
- [The LLM Red Teaming Framework](https://github.com/confident-ai/deepteam)


## Author
This project was developed by [Iván Ruiz Rube](https://www.linkedin.com/in/iv%C3%A1n-ruiz-rube-0970331a) and [Andrés Muñoz](https://es.linkedin.com/in/andr%C3%A9s-mu%C3%B1oz-ortega-52235415).

## Acknowlegments
This course is part of the R&D&i Project PID2023-149674OB-I00 (GENIALLE), funded by MICIU/AEI/10.13039/501100011033 and ERDF, EU. 
![](./MICIU+Cofinanciado+AEI.jpg)

