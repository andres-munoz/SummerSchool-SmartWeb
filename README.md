# Building Smart Web Apps with AI

## Description
This repository contains the sample materials created for the Internationa Summmer School course *Building Smart Web Apps with AI*.

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
8. [LLMOps](./slides/8-LLM-Ops.pdf)
9. [Closing](./slides/9-Closing.pdf)

## Repository Contents
In this repository you will find the source code for a web project based on Java technologies.
The project was created using the Spring Boot, Vaadin, and LangChain4J frameworks.

The application offers the following functionalities:
1. Text generator
2. Image generator
3. Image describer (multimodal)
4. Text translator
5. Text summarizer
6. Sentiment analyzer
7. Data extractor
8. Simple chat
9. Chat with memory
10. Assistant
11. Chat web service
12. Assistant with EasyRAG
13. Assistant with Naive RAG
14. Copilot with Tools
15. Semantic search in relational database
16. Semantic search in vector database

## Usage Instructions

### Modifying the Application
You can use this project in two different ways:
- From your development machine by cloning the project with *git clone* and working locally with your IDE.
- Using a GitHub *codespace*. A codespace is a cloud-based development environment that you can use 
  directly without needing any additional setup.

### Running the Application
The project is a standard Maven project. To run it from the command line, use `mvnw` (Windows) or `./mvnw` (Mac and Linux), then open http://localhost:8080 in your browser.

You can also import the project into your preferred IDE as you would with any Maven project. Read more about how to import Vaadin projects into different IDEs (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

### Project Structure
- `MainLayout.java` in `src/main/java` contains the navigation configuration (i.e., the sidebar/top bar and main menu). This configuration uses App Layout.
- The `views` package in `src/main/java` contains the server-side Java views of your application.
- The `views` folder in `src/main/frontend` contains the client-side JavaScript views of your application.
- The `themes` folder in `src/main/frontend` contains custom CSS styles.

## Useful Links
- [Spring](https://spring.io)
- [Vaadin](https://vaadin.com)
- [LangChain4J](https://docs.langchain4j.dev/)

## Author
This project was developed by [Iván Ruiz Rube](https://www.linkedin.com/in/iv%C3%A1n-ruiz-rube-0970331a) and [Andrés Muñoz](https://es.linkedin.com/in/andr%C3%A9s-mu%C3%B1oz-ortega-52235415).

## Acknowlegments
This course is part of the R&D&i Project PID2023-149674OB-I00 (GENIALLE), funded by MICIU/AEI/10.13039/501100011033 and ERDF, EU. 
![](./MICIU+Cofinanciado+AEI.jpg)

