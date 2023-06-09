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

### Export data

Users can be created in Keycloak UI [and exported](https://www.keycloak.org/server/importExport):

    /opt/keycloak/bin/kc.sh
    $ /opt/keycloak/bin/kc.sh export --dir /tmp/key_export --realm jhipster --users realm_file

    docker cp 57a83b7e6357:/tmp/key_export/jhipster-realm.json .

Each user will be added to the DB after first login.

Data can be created in UI and exported as (to be copied to changelogs):

     ./mvnw liquibase:generateChangeLog -Dliquibase.diffTypes=data

### Users

- admin:admin
- user:user
- kepler:kepler

### Angular

[Using Angular](https://www.jhipster.tech/using-angular/)


### Tech

Kotlin coroutines:
https://medium.com/@inzuael/how-to-start-with-coroutines-in-springboot-applications-e0da1f013dbd
