# scancode-databind

Transform output of scancode tool into csv files.

```mvn clean install```

```java -jar target/scancode-databind-1.0-SNAPSHOT-jar-with-dependencies.jar -jsonFile /home/roma/dev/java/istack-scan.json -outputFile /home/roma/dev/java/istack -license /home/roma/dev/java/istack-copyright.txt```

Output will actually be files with suffixes -known-license.csv, -unknown-license.csv and -without-license.csv

You have to run scancode with following options:

```
./scancode -c -l -i --full-root --license-text -f json --ignore generated-sources ~/path/to/oroject/ ~/path/to/scan.json
```