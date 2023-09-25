pipeline {
    agent {
        docker {
            image 'maven:3.9.4-eclipse-temurin-17'
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -version'
                sh 'mvn clean install'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                sh "mvn clean verify sonar:sonar -Dsonar.projectKey=sdwebui_api_bot -Dsonar.projectName='sdwebui_api_bot'"
                }
            }
        }
    }
}
