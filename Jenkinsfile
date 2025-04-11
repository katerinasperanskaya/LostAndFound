        stage('Check Jacoco Report Exists') {
            steps {
                bat "dir target\\site\\jacoco"
            }
        }

        stage('Print Working Directory Before SonarQube') {
            steps {
                bat "cd"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat '''
                        mvn sonar:sonar ^
                            -Dsonar.projectKey=lostandfound ^
                            -Dsonar.projectName="lostandfound" ^
                            -Dsonar.host.url=http://localhost:9000 ^
                            -Dsonar.token=sqp_e707f8d0c9615b1b869e8884dc5a385d99fc2c76 ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }
