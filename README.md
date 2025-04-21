# Reconciliation Service

The **Reconciliation Service** is a Spring Boot application designed to perform transaction reconciliation and correlation. It provides RESTful APIs to make the reconciliation between two files. The API offers 2 endpoints one for comparing the files and other calculating the similarity between transactions.

## Features

- **Transaction Reconciliation**: Compare two files containing transaction data and identify matches, mismatches, and discrepancies.
- **Transaction Correlation**: Analyze the similarity between a single transaction and a list of transactions by evaluating various attributes such as amount, date, and narrative. The correlation score quantifies the degree of similarity, with values closer to 0 indicating a high level of resemblance between transactions.

## Technologies Used

- **Java 21**
- **Spring Boot**
- **Lombok**
- **Gradle**

## API Endpoints

### 1. **Compare Transactions**
- **Endpoint**: `/reconciliations/compares`
- **Method**: `POST`
- **Consumes**: `multipart/form-data`
- **Produces**: `application/json`
- **Description**: Compares two files containing transaction data and returns the reconciliation results.
- **Request Parameters**:
  - `afile`: The first file containing transaction data.
  - `bfile`: The second file containing transaction data.
- **Response**: A `TransactionReconciliation` object containing the results of the reconciliation.

### 2. **Calculate Correlations**
- **Endpoint**: `/reconciliations/correlations`
- **Method**: `POST`
- **Consumes**: `multipart/form-data`
- **Produces**: `application/json`
- **Description**: Calculates the correlation between a transaction and a list of transactions.
- **Request Parameters**:
  - `transaction`: The transaction to compare.
  - `file`: The file containing the list of transactions to compare against.
- **Response**: A list of `Transaction` objects sorted by their correlation values. 
