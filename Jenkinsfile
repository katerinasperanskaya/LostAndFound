pipeline {
    agent any
    
    tools {
        maven 'maven'
    }



    stages {
        stage('Checkout Code') {
            steps {
                git(
                    branch: 'main',
                    url: 'https://github.com/katerinasperanskaya/LostAndFound'
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
  -Dsonar.projectKey=lostandfound \
  -Dsonar.projectName='lostandfound' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_e707f8d0c9615b1b869e8884dc5a385d99fc2c76
                        """, returnStdout: true).trim()
                    }
                }
            }
        }
    }
}
