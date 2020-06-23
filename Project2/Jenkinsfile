pipeline {
  triggers {
      pollSCM('H/5 * * * *')
  }
  agent any
  
  stages {
    stage('Build') {
	  steps {
	    withMaven(maven: 'maven') {
		  sh 'mvn -B -DskipTests clean package -f Auction/pom.xml'
		  sh 'mvn -B -DskipTests clean package -f BiddingService/pom.xml'
		  sh 'mvn -B -DskipTests clean package -f UserService/pom.xml'
		}
	  }
	}
	
	stage('Test') {
	  steps {
	    withMaven(maven: 'maven') {
		  sh 'mvn test -f Auction/pom.xml'
		  sh 'mvn test -f BiddingService/pom.xml'
		  sh 'mvn test -f UserService/pom.xml'
		}
	  }
	}
	
	stage('Containerize') {
	  steps {
	    sh 'docker build -t leeperry/g3p2-auction Auction/'
		sh 'docker build -t leeperry/g3p2-bidding BiddingService/'
		sh 'docker build -t leeperry/g3p2-user UserService/'
	  }
	}
	
	stage('Deliver') {
	  steps {
	    withDockerRegistry(credentialsId: 'dockerhub_id', url: '') {
		  sh 'docker push leeperry/g3p2-auction'
		  sh 'docker push leeperry/g3p2-bidding'
		  sh 'docker push leeperry/g3p2-user'
		}
	  }
	}
  }
  environment {
    POSTGRES_URL = 'dlbroadwadb.cpbqys5iu3x8.us-east-2.rds.amazonaws.com'
    POSTGRES_USERNAME = 'postgres'
    POSTGRES_DATABASE_NAME = 'postgres'
    POSTGRES_PASSWORD = 'enter123'
    POSTGRES_PORT = '5432'
    POSTGRES_DEFAULT_SCHEMA = 'ebay_schema'
  }
}