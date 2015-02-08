Wolfram Cells
=============

Wolfram random patterns generator

How to run:
-----------

```bash
# Install Scala:
SCALA_VERSION="2.11.1";
wget http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz
tar xvzf scala-$SCALA_VERSION.tgz
sudo sh -c 'echo "export SCALA_HOME=\"$PWD/scala-'$SCALA_VERSION'\"" >> /etc/profile; echo "export PATH=\"\$PATH:\$SCALA_HOME/bin\"" >> /etc/profile;'
sh /etc/profile

# Install sbt (scala build tool):
wget http://dl.bintray.com/sbt/debian/sbt-0.13.5.deb
sudo dpkg -i sbt-0.13.5.deb

# Clone, build and run:
git clone https://www.github.com/gto76/wolfram-cells.git
cd wolfram-cells
sbt assembly
java -jar target/scala-2.11/wolfram-cells-assembly-0.9.0.jar 
```
