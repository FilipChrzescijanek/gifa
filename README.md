# gifa
Global image features analyzer

### How to run it?
```sh
$ git clone https://github.com/lazzymf/gifa.git
$ cd gifa/gifa-parent
$ mvn clean install
$ cd ../gifa-core
$ mvn clean install
$ cd ../gifa
$ mvn clean package -P natives
$ java -Djava.library.path=target/natives -jar target/gifa-0.0.1-SNAPSHOT.jar
```