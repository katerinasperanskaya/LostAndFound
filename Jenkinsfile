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
                git url: 'https://github.com/katerinasperanskaya/LostAndFound.git', branch: 'develop'
            }
        }

        stage('Build & Unit Tests') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Start Spring Boot App') {
            steps {
                sh '''
                    nohup java -jar target/$JAR_NAME > app.log 2>&1 &
                    echo $! > app.pid
                    sleep 10
                '''
            }
        }

        stage('Run Karate Tests') {
            steps {
                sh 'mvn test -Dtest=*Karate*'
            }
        }

        stage('Stop App') {
            steps {
                sh '''
                    kill $(cat app.pid)
                    rm app.pid
                '''
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            sh 'pkill -f $JAR_NAME || true'
        }
    }
}
