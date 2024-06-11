### How to run a postgres container for development Linux
```bash
docker run --name ds-lab-pg --rm \
-e POSTGRES_PASSWORD=pass123 \
-e POSTGRES_USER=dbuser \
-e POSTGRES_DB=appdb \
-d --net=host \
-v ds-lab-vol:/var/lib/postgresql/data \
postgres:14
```

### How to run a postgres container for development Windows

```bash
docker run --name ds-lab-pg --rm -p 5432:5432 -e POSTGRES_PASSWORD=pass123  -e POSTGRES_USER=dbuser  -e POSTGRES_DB=appdb   -v ds-lab-vol:/var/lib/postgresql/data  postgres:14
```

### How to build application with Docker-compose
To start you must have docker and docker-compose installed on your system. Then you can simply run the command as is
```bash
docker-compose up -d
```
3 containers will be created and you can type in localhost (if you are running it on your computer) in the broswer to see the application

### How to build application with Jenkinsfile for ansible
You must first setup Jenkins on a vm and also download ansible
```bash
sudo apt-get install openjdk-17-jre
```

```bash
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install jenkins
```

```bash
sudo apt update
sudo apt install software-properties-common
sudo add-apt-repository --yes --update ppa:ansible/ansible
sudo apt install ansible
```

Once Jenkins is setup make sure to create an ssh key and connect it to github as jenkins user. Then create a freestyle project that clones the ansible-project repository ( https://github.com/Spotinum/ansible-Project ) And then create a pipeline project that clones the localDocApp repository and runs the JenkinsFile. Once everything is ready press build on the pipeline project and wait for it to start (you need 2 vms one for spring and postgres and one for vue, change the Jenkins on where the ip 20.117.107.2 is shown put the ip of the frontend vm. And for 20.0.144.33 put the ip of the spring and postgres vm.

### How to build application with Jenkinsfile for docker
In the same vm where jenkins has been set up, create a new pipeline project that clones the same repo but run the docker.Jenkinsfile instead.
you must first Download docker on the vm

```bash
sudo apt-get update


sudo apt-get install ca-certificates curl


sudo install -m 0755 -d /etc/apt/keyrings


sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null


sudo apt-get update

```

```bash
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

```

```bash
sudo groupadd docker
sudo usermod -aG docker jenkins 
sudo chmod 666 /var/run/docker.sock
```
Make sure injenkins to add your github token as your credential named as secret. Now you can build the application.

### How to build application with K8S
First you must setup a vm and install microk8s. 
Next, through the terminal, we need to enable the ingress and storage DNS. Then, we have to place the config in ~/.kube/config and afterwards copy it to our local computer in ~/.kube/config. We also need to change the IP address from localhost to the public IP of the VM and add insecure-skip-tls-verify: true.

* Run postgres persistent volume
```bash
kubectl apply -f k8s/postgres/postgres-pvc.yaml 
```

* Run postgres Deployment
```bash
kubectl apply -f k8s/postgres/postgres-deployment.yaml 
```

* Run postgres Service
```bash
kubectl apply -f k8s/postgres/postgres-svc.yaml 
```

* Run spring Deployment
```bash
kubectl apply -f k8s/spring/spring-deployment.yaml 
```

* Run spring Service
```bash
kubectl apply -f k8s/spring/spring-svc.yaml 
```

* Run vue Deployment
```bash
kubectl apply -f k8s/vue/vue-deployment.yaml 
```

* Run vue Service
```bash
kubectl apply -f k8s/vue/vue-svc.yaml 
```

* Run vue Ingress
```bash
kubectl apply -f k8s/vue/vue-ingress.yaml 
```

You can now go to a broswer and type spotinum-spring.ddns.net to look at the application