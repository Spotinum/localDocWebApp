pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
            /* clone repository */
                git branch: 'main', url: 'git@github.com:Spotinum/localDocWebApp.git'
            }
        }
        stage('Test') {
            steps {
            /* run the tests */
                sh './mvnw test'
            }
        }
        stage('run ansible pipeline') {
            steps {
            /* clone ansible repository */
                build job: 'ansible'
            }
        }
        stage('Install postgres') {
            steps {
            /* run the ansible script for postgres */
                sh '''

                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l dbserver-vm ~/workspace/ansible/playbooks/postgres.yaml
                '''
            }
        }

/* run the ansible script for spring */
        stage('Deploy spring boot app') {
            steps {
                sh '''
                   # replace dbserver in host_vars
                     sed -i 's/dbserver/localhost/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                     # replace ip in group_vars for the frontend and backend vms public ip
                     sed -i 's/192.168.56.101/20.0.144.33/g' ~/workspace/ansible/group_vars/appservers.yaml
                     sed -i 's/192.168.56.103/20.117.107.2/g' ~/workspace/ansible/group_vars/appservers.yaml
                '''
                sh '''
                    # edit host var for appserver

                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appserver-vm ~/workspace/ansible/playbooks/spring.yaml
                '''
            }
        }
        /* run the ansible script for vuejs */
       stage('Deploy frontend') {
            steps {
                sh '''
                   # sed -i 's/dbserver/20.0.144.33/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l frontend-vm ~/workspace/ansible/playbooks/vuejs.yaml
                '''
            }
       }
    }
}