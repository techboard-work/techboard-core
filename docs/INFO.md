# TechBoard


## Dev

### Run JHipster

The process is described [here.](https://www.jhipster.tech/installation/#docker-installation-for-advanced-users-only)

Start JHipster

    docker-compose -f docs/docker-compose-jhipster.yaml up

Connect to the container to install KHipster:

    docker exec -it --user root jhipster bash
    npm install -g generator-jhipster-kotlin
    exit

Connect to use KHipster:

    docker exec -it jhipster bash
    khipster --skip-fake-data

Congratulations, JHipster execution is complete!

Apply JDL:

Follow [the process:](https://www.jhipster.tech/creating-an-entity/#jhipster-uml-and-jdl-studio)

    docker exec -it jhipster bash
    khipster jdl docs/jhipster-jdl.jdl


Stop JHipster

    docker-compose -f docs/docker-compose-jhipster.yaml down --volumes

### Run TechBoard

Launch the infra:

    cd src/main/docker
    docker-compose -f infra.yml up

Launch App with initial data (make sure you use Java 11):

    ./mvnw --version
    ./mvnw

Stop infra:

    docker-compose -f infra.yml down --volumes

### Angular

[Using Angular](https://www.jhipster.tech/using-angular/)
