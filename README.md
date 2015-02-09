Wolfram Cells
=============

[Wolfram] (https://www.youtube.com/watch?v=jDguxopxyJk) random patterns generator, using one-dimensional binary cellular automata.

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

Man:
----
```
usage: wolfram-cells
 -b,--bitmap                   Outupt image instead of text.
 -c,--background-color <arg>   Background color of output bitmap as single
                               number. Default is black - 0. Warning: for
                               input image 0 is assumed.
 -e,--height <arg>             Matrix height.
 -f,--file <arg>               Output filename.
 -g,--foreground-color <arg>   Foreground color of output bitmap as single
                               number. Default is blue - 255.
 -h,--help                     Print this message.
 -i,--input-matrix <arg>       Specify filename containing matrix on which
                               the  transformations will be executed.
                               Should be in text format or in png. '-' for
                               stdin
 -o,--only-developed           Output matrix only if there is more than
                               one cell per row.
 -p,--pixel-size <arg>         Cell size in pixels.
 -r,--rule-number <arg>        Rule to be used.
 -w,--width <arg>              Matrix width
```
