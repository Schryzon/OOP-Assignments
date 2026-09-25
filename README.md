# Object-Oriented Programming (OOP) Solutions

Academic coursework and system-grade implementations in Java (OpenJDK 26). This repository contains modular solutions for discrete course modules, emphasizing clean architecture, structural determinism, and zero visual noise.

---

## Repository Structure

```
OOP-Assignments/
├── 04/
│   ├── Country_Capital.java
│   ├── Bank_Account.java
│   ├── Bank_Account_Array_In_Action.java
│   ├── Account.java
│   ├── Customer.java
│   ├── Bank.java
│   ├── Bank_Demo.java
│   └── README.md
├── 05/
│   ├── Shape.java
│   ├── Square.java
│   ├── Circle.java
│   ├── Cylinder.java
│   ├── Shape_Demo.java
│   ├── Mathematics.java
│   ├── Advanced_Mathematics.java
│   ├── Math_Demo.java
│   └── README.md
└── README.md
```

---

## Modules Overview

### Module 04: Fixed Arrays, Dynamic Collections (ArrayList), and Aggregation
- **2D Arrays**: Multi-row matrix mappings for coordinate and relational lookups (`Country_Capital.java`).
- **Dynamic Collections**: Insertion, index-based mutation, and element tracking via `ArrayList` (`Bank_Account.java`, `Bank_Account_Array_In_Action.java`).
- **Domain Aggregation & Association**: Banking domain model encapsulating financial state across `Bank`, `Customer`, and `Account` entities with optimistic invariant validation (`Bank_Demo.java`).
- Detailed documentation: [04/README.md](04/README.md)

### Module 05: Abstraction, Encapsulation, Inheritance, and Polymorphism
- **Inheritance Hierarchy**: Specialized geometric models chaining constructors via `super()` (`Shape.java`, `Square.java`, `Circle.java`, `Cylinder.java`).
- **Run-Time Polymorphism**: Dynamic late binding and virtual method table (vtable) dispatch across heterogeneous shape arrays (`Shape_Demo.java`).
- **Compile-Time Polymorphism (Overloading)**: Multi-arity and multi-type arithmetic calculation kernels (`Mathematics.java`, `Advanced_Mathematics.java`, `Math_Demo.java`).
- Detailed documentation: [05/README.md](05/README.md)

---

## Build & Execution Instructions

### Prerequisites
- Java Development Kit (JDK 21+ / OpenJDK 26 recommended)

### Quick Run

Compile and execute Module 04:
```powershell
# Compile
javac 04/*.java

# Run Country-Capital Mapping
java -cp 04 Country_Capital

# Run ArrayList Dynamic Action Harness
java -cp 04 Bank_Account_Array_In_Action

# Run Core Banking Domain Harness
java -cp 04 Bank_Demo
```

Compile and execute Module 05:
```powershell
# Compile
javac 05/*.java

# Run Geometric Inheritance & Polymorphism Demo
java -cp 05 Shape_Demo

# Run Arithmetic Method Overloading Demo
java -cp 05 Math_Demo
```
