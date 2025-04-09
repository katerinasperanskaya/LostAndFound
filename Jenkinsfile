pipeline {
    agent any

    tools {
        maven 'Maven 3.8.1' // or whatever version you have configured in Jenkins
        jdk 'Java 17'       // match your app’s Java version
    }

    environment {
        JAR_NAME = 'LostAndFound-0.0.1-SNAPSHOT.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/katerinasperanskaya/LostAndFound.git', branch: 'main'
            }
        }

       

         stage('Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Test with JaCoCo') {
            steps {
                bat 'mvn test'
            }
        }

        stage('JaCoCo Report') {
            steps {
                bat 'mvn jacoco:report'  // Generates the coverage report
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${env.SONARQUBE_ENV}") {
                    bat """
                    mvn sonar:sonar ^
                      -Dsonar.projectKey=indv ^
                      -Dsonar.login=${SONAR_TOKEN} ^
                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }}

    post {
        always {
            echo 'Cleaning up...'
            sh 'pkill -f $JAR_NAME || true'
        }
    }
}
