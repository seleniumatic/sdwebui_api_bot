pipeline {
    agent {
        docker {
            image 'maven:3.9.4-eclipse-temurin-17'
            label 'docker'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -version'
                sh 'mvn clean install'
            }
        }
    }
}
