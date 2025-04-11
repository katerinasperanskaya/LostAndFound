pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.9'
    }

    environment {
        SONARQUBE_URL = 'http://SonarQube:9000'
        SONARQUBE_TOKEN = 'sqp_9b45604a5b39f962a82571da0e9c28019b14cae1'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git(
                    branch: 'dev',
                    url: 'https://github.com/Rane43/BrainBash.git'
                )
            }
        }

        stage('Clean Project') {
            steps {
                sh "mvn clean"
            }
        }

        stage('Build') {
            steps {
                sh "mvn package"
            }
        }
        
        stage('Verify') {
            steps {
                sh "mvn verify jacoco:report"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        // Trigger SonarQube analysis and retrieve task ID
                        def sonarAnalysis = sh(script: """
                        mvn sonar:sonar \
                            -Dsonar.projectKey=individual-project \
                            -Dsonar.projectName='individual-project' \
                            -Dsonar.host.url=$SONARQUBE_URL \
                            -Dsonar.token=$SONARQUBE_TOKEN
                        """, returnStdout: true).trim()
                    }
                }
            }
        }
    }
}
