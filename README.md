Wolfram Cells
=============

Wolfram random patterns generator

How to run:
-----------

Install Scala:
```bash
wget http://downloads.typesafe.com/scala/2.11.1/scala-2.11.1.tgz
tar xvzf scala-2.11.1.tgz
export SCALA_HOME="$PWD/scala-2.11.1"
export PATH="$PATH:$SCALA_HOME/bin"
```

Install sbt (scala build tool):
```bash
wget http://dl.bintray.com/sbt/debian/sbt-0.13.5.deb
sudo dpkg -i sbt-0.13.5.deb
```

Clone, build and run:
```bash
git clone https://www.github.com/gto76/wolfram-cells.git
cd wolfram-cells
sbt assembly
java -jar target/scala-x.x/wolfram-cells-assembly-x.x.x.jar
```
