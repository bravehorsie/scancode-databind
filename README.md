# scancode-databind

Transform output of scancode tool into csv files.

```mvn clean install```

```java -jar target/scancode-databind-1.0-SNAPSHOT-jar-with-dependencies.jar ~/path/to/jsoninput.json ~/path/to/outputfile```

Output will actually be files with suffixes -known-license.csv, -unknown-license.csv and -without-license.csv

You have to run scancode with -i option if you want to exclude directories from the output files.

For example:

```
./scancode -i -l --format json ~/path/to/project ~/path/to/scancode_result.json
```