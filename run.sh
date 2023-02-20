#!/bin/bash
touch tasks.json

./mvnw clean install
# ajouter une tâche
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="add -c 'hello world'"
# ajouter une tâche avec une date
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="add -d:2022-03-01 -c 'finalize the agenda exercise'"
# lister les tâches
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="list"
# update la derniere tache
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="update 1 -d:2022-04-01"
# cancel la premiere tache
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="remove 0"
# update la derniere tache avec un status
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="update 1 -s:done"
# lister les tâches
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="list"
# ajout d'un tag
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="update 1 -t 'urgent'"
# ajout d'une sous tache
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="add -c 'tout faire a la derniere minute' -p 1"
# modification de la sous tache
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="update 2 -s:done -t 'sa va le faire'"
# lister les tâches
./mvnw exec:java -q -Dexec.mainClass=org.esgi.Main -Dexec.args="list"
