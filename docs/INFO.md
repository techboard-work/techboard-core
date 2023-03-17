# TechBoard


## Dev

### Run JHipster

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


Stop JHipster

    docker-compose -f docs/docker-compose-jhipster.yaml down --volumes

### Run TechBoard

Launch the infra:

    cd src/main/docker
    docker-compose -f infra.yml up

Launch App (make sure you use Java 11):

    ./mvnw --version
    ./mvnw
