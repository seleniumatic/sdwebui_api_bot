pipeline {
    agent {
        docker {
            image 'maven:3.9.4-eclipse-temurin-17'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarServer') {
                    sh "mvn clean verify sonar:sonar -Pcoverage"
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -version'
                sh 'mvn clean install'
            }
        }
    }
    post {
        always('Publish Report') {
            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts 'target/*.jar'
        }
    }
}