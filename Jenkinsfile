pipeline {
    agent any


    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'git@github.com:Spotinum/localDocWebApp.git'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }
        stage('Install postgres') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l dbserver-vm ~/workspace/ansible/playbooks/postgres.yaml
                '''
            }
        }

        stage('Deploy spring boot app') {
            steps {
                sh '''
                   # replace dbserver in host_vars
                    # sed -i 's/dbserver/20.0.152.18/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                   # replace workingdir in host_vars
                    # sed -i 's/vagrant/azureuser/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                '''
                sh '''
                    # edit host var for appserver

                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appserver-vm ~/workspace/ansible/playbooks/spring.yaml
                '''
            }
        }
       stage('Deploy frontend') {
            steps {
                sh '''
                    sed -i 's/dbserver/20.0.152.18/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appserver-vm -e branch=main -e backend_server_url=http://localhost:9090 ~/workspace/ansible/playbooks/vuejs.yaml
                '''
            }
       }
    }

}