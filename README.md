## Function as a Service System

**A Java-based Task Distribution System. Simulates de OpenWhisk architecture made by IBM**


## Introduction

This project implements a task distribution system in Java, allowing efficient allocation and execution of various actions among multiple invokers. The system employs different distribution policies, such as Greedy Group, Round Robin, and Uniform Group, providing flexibility in managing tasks based on diverse criteria.

## Features

- **Dynamic Task Distribution**: Tasks are intelligently distributed among invokers based on selected policies.
- **Policy Flexibility**: Choose from various distribution policies to suit different use cases, implemented using the `Strategy pattern`.
- **Observer Design Pattern**: Utilizes the `observer` pattern to monitor and manage invokers' states and actions.
- **Multi-Threading Support**: Enables concurrent execution of tasks through thread pools for improved efficiency.
- **Reflection and Proxy Integration**: Incorporates reflection and proxy mechanisms for dynamic task invocation.
- **Singleton Design Pattern**: The `Controller` class follows the `Singleton` pattern, ensuring a single instance controls the task distribution system.
- **Metrics and Analysis**: Includes functionality for analyzing execution times and invoker memory usage.
- **Decorator Pattern**: Implements the decorator pattern for adding dynamic behavior to actions, enhancing their functionality.
- **MapReduce Model**: Adheres to the MapReduce programming model, allowing parallel processing and efficient data handling.


## Getting Started
- **Clone the repository:**
git clone https://github.com/your-username/your-project.git

- **Navigate to the project directory:**
cd your-project

- **Compile and run the main class for testing different distribution policies.**

## Project Structure
The project is organized following common Java project conventions, with a modular and layered structure to enhance readability, maintainability, and scalability.

- **`main` Package**
The main package serves as the main entry point and contains classes responsible for orchestrating the task distribution system.

`Controller`: This class represents the central control unit responsible for managing task distribution. It follows the Singleton pattern to ensure a single point of control.

`Invoker`: The Invoker class represents the entities capable of executing tasks. Each invoker has its own memory capacity and is observable to allow the controller to monitor its state.

`Action`: The Action class, located in the main package, is a fundamental component representing a task that can be executed by an Invoker. It serves as the base class for various specific types of actions, such as Adder and Multiplier

`Metric`: Defines the structure for collecting and analyzing metrics during the execution of tasks. This interface includes methods related to tracking execution time, memory usage, and other relevant metrics

- **`operations` Package**
The operations package contains classes representing different types of tasks that can be assigned to invokers.

`Adder`: Represents an addition operation task.

`Multiplier`: Represents a multiplication operation task.

`CountWords`: The CountWords class is an implementation of the Operation interface, located in the operations package. This operation is designed to count the number of words in a given set of values.

`Factorial`: The Factorial class, also in the operations package, implements the Operation interface and performs the calculation of the factorial of a number. 

`WordCount`: The WordCount class is another implementation of the Operation interface within the operations package. It counts the occurrences of each word in a given set of values.

- **`policy` Package**
The policy package contains various task distribution policies, each implemented using the Strategy pattern.

`GreedyGroup`: A policy that assigns tasks to invokers based on available memory, prioritizing invokers with more available memory.

`RoundRobinImproved`: A policy that distributes tasks in a round-robin fashion among invokers.

`UniformGroup`: A policy that attempts to distribute tasks uniformly among invokers.

`BigGroup`: The BigGroup class, located in the policy package, implements the DistributionPolicy interface. This policy focuses on optimizing memory distribution by grouping multiple invokers together.

`RoundRobin`: The RoundRobin class, also in the policy package, implements the DistributionPolicy interface. This policy follows the round-robin strategy, distributing actions in a cyclic manner among the available invokers.

- **`reflection` Package**
The reflection package incorporates reflection and proxy mechanisms for dynamic task invocation.

`ActionProxy`: The ActionProxy class, also in the reflection package, acts as a wrapper around the DynamicProxy class, providing a more user-friendly interface for creating dynamic proxies.

`DynamicProxy`: The DynamicProxy class, located in the reflection package, is a part of the reflection mechanism and facilitates the creation of dynamic proxies for action objects. 

- **`main.main` Package**
The main.main package contains various Main classes representing different scenarios and configurations of the task distribution system. These classes demonstrate the usage of different policies, reflection, and other features.

- **`decorator` Packages**

`ActionResult`:The ActionResult class, situated in the decorator package, plays a pivotal role in wrapping the result of an action execution.

`InvokerCacheDecorator`: The InvokerCacheDecorator class, also part of the decorator package, serves as a decorator for the Invoker class.

`InvokerChronometer`: The InvokerChronometer class, located in the decorator package, acts as a decorator for the Invoker class to measure the time taken for action execution.

## Contributing

Contributions are welcome!

## Acknowledgments
The project makes use of design patterns and Java features to create a robust and flexible task distribution system.

Copyright (c) [2024] [ArnauPapiol][AdrianGarcia]