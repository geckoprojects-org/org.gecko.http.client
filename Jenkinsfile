pipeline  {
    agent any

    tools {
        jdk 'OpenJDK11'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Main branch release') {
            when { 
                branch 'main' 
            }
            steps {

                echo "I am building on ${env.BRANCH_NAME}"
                sh "./gradlew clean build itest release -Drelease.dir=$JENKINS_HOME/repo.gecko/release/geckoprojects_org/ --info --stacktrace -Dmaven.repo.local=${WORKSPACE}/.m2"
            }
        }
        stage('Snapshot branch release') {
            when { 
                branch 'snapshot'
            }
            steps  {
                echo "I am building on ${env.JOB_NAME}"
                sh "./gradlew release --info --stacktrace -Dmaven.repo.local=${WORKSPACE}/.m2"
                sh "mkdir -p $JENKINS_HOME/repo.gecko/snapshot/geckoEMF"
                sh "rm -rf $JENKINS_HOME/repo.gecko/snapshot/geckoEMF/*"
                sh "cp -r cnf/release/* $JENKINS_HOME/repo.gecko/snapshot/geckoprojects_org"
            }
        }
    }

}