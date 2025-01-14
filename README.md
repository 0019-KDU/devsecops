# CI/CD Pipeline Documentation

![Alt text](docs/Screenshot%202025-01-14%20131630.png)

## Overview

This documentation provides a detailed description of the CI/CD pipeline workflow.

### CI Pipeline

1. **Source Code Repository**

   - Tools: GitLab, Git
   - The process begins with the source code being stored in a repository.

2. **Build & Unit Test**

   - Tools: Maven
   - Compiles the code and runs unit tests to ensure the code's functionality.

3. **Code Coverage**

   - Tools: Jacoco
   - Measures how much of the code is executed during the unit tests.

4. **SCA (Software Composition Analysis)**

   - Tools: OWASP Dependency-Check
   - Analyzes dependencies for security vulnerabilities.

5. **SAST (Static Application Security Testing)**

   - Tools: SonarQube
   - Scans the code for potential vulnerabilities and ensures adherence to coding standards.

6. **Quality Gates**

   - Tools: SonarQube
   - Verifies that the code meets predefined quality criteria.

7. **Build Image**

   - Tools: Dockerfile
   - Creates a container image for the application.

8. **Scan Image**

   - Tools: Trivy
   - Scans the Docker image for vulnerabilities.

9. **Smoke Test**

   - Performs preliminary tests to check the basic functionality of the application.

10. **Trigger CD Job**
    - The CI pipeline triggers the CD pipeline for deployment.

### CD Pipeline

1. **Update YAML**

   - Updates the deployment manifest files stored in the repository.

2. **Manifest Repository**

   - Stores the deployment configuration files.

3. **Deployment**

   - Tools: ArgoCD
   - Deploys the application to a Kubernetes cluster.

4. **Kubernetes Cluster**
   - Manages the deployment and scaling of the application.

---

## Tools Summary

- **Build and Test**: Maven
- **Code Coverage**: Jacoco
- **SCA**: OWASP Dependency-Check
- **SAST and Quality Gates**: SonarQube
- **Containerization**: Dockerfile
- **Image Scanning**: Trivy
- **Deployment**: ArgoCD
- **Cluster Management**: Kubernetes

To set up a Production-Grade DevSecOps CI/CD Pipeline using Jenkins, SonarQube, Minikube, and ArgoCD, follow the detailed steps below:

## Initial Setup

### Launch an EC2 Instance

1. **Login to AWS Management Console:**
   - Navigate to the EC2 Dashboard.
   - Launch a new instance.
   - Choose Ubuntu as the AMI.
   - Select an instance type (e.g., t2.micro).
   - Configure security groups to allow SSH, HTTP, and port 8080.

### Install Jenkins

```bash
#!/bin/bash
# Update packages
sudo apt-get update

# Install Java
sudo apt-get install -y openjdk-11-jdk

# Add Jenkins repo key to your system
wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo apt-key add -

# Add Jenkins to your system's sources
sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'

# Update package list
sudo apt-get update

# Install Jenkins
sudo apt-get install -y jenkins

# Start Jenkins service
sudo systemctl start jenkins

```

## Jenkins Setup for SonarQube and AWS Integration

## Prerequisites

### EC2 Servers

1. **Build Server:**

   - **Instance Type:** t2.micro
   - **Storage:** 15 GB (EBS volume)
   - **Use Case:** This server will be used to build the project.

2. **SonarQube Server:**
   - **Instance Type:** t2.medium
   - **Memory:** 4 GB
   - **Use Case:** This server will run SonarQube for static code analysis.

## Step 1: Ensure Necessary Plugins Are Installed on Jenkins Master

Make sure the following Jenkins plugins are installed on the Jenkins Master server:

- **Parameterized Trigger Plugin:** Allows triggering other jobs with parameters.
- **GitLab Plugin:** Integrates Jenkins with GitLab to trigger builds and retrieve information from repositories.
- **Docker Pipeline:** Allows using Docker commands within Jenkins pipelines.
- **Pipeline: AWS Steps:** AWS steps for integrating Jenkins pipelines with AWS services.
- **SonarQube Scanner:** Integrates Jenkins with SonarQube to run code quality scans.
- **Quality Gates Plugin:** This plugin ensures the code passes the quality gate in SonarQube before continuing the pipeline.

### How to Install Plugins:

1. Go to Jenkins Dashboard.
2. Click on "Manage Jenkins" > "Manage Plugins."
3. Under the "Available" tab, search for the plugins by name (e.g., "GitLab Plugin", "SonarQube Scanner").
4. Select the plugins and click "Install without restart."

## Step 2: Install Docker, Java8, Java11 & Trivy on Build Server

To set up the environment, run the setup script:

```

$ sudo ./setup.sh

```

## Step 3: Install Sonrqube on the t2.medium server

```

$ sudo apt update
$ sudo apt install -y docker.io
$ sudo usermod -a -G docker ubuntu
$ sudo docker run -d --name sonar -p 9000:9000 sonarqube:lts-community

```

## Step 4: Add necessary credentials

- [ ] Generate Sonarqube token of type "global analysis token" and add it as Jenkins credential of type "secret text"
- [ ] Add dockerhub credentials as username/password type
- [ ] Add Gitlab credentials
- [ ] Add Build server credentials for Jenkins master to connect

## Step 5: Enable Sonarqube webhook for Quality Gates & Install dependency-check plugin

- [ ] Generate webhook & add the Jenkins URL as follows - http://URL:8080/sonarqube-webhook/

EX-http://ec2-54-82-33-243.compute-1.amazonaws.com:8080/sonarqube-webhook/

## Minikube Setup (One EC2 Server)

Create a t2.medium EC2 instance with 4 GB storage for Minikube

## Setup Minikube

```

$ Install Docker
$ sudo apt update && sudo apt -y install docker.io

Install kubectl
$ curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.23.7/bin/linux/amd64/kubectl && chmod +x ./kubectl && sudo mv ./kubectl /usr/local/bin/kubectl

Install Minikube
$ curl -Lo minikube https://storage.googleapis.com/minikube/releases/v1.23.2/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/

Start Minikube
$ sudo apt install conntrack
$ minikube start --vm-driver=none

```

## Setup ArgoCD

```

$ kubectl create namespace argocd
$ kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
$ kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "NodePort"}}'

For version 1.9 or later:
$ kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d && echo

```

```

```
