A giter8 template for getting setup with Apache Spark in CDH.

Setup
=====

There are two different ways to use this template.

Using sbt new (Spark 2)
-----------------------

In case of sbt’s launcher version 0.13.13 or above you can run `sbt new squito/cdh-spark.g8` and follow the interactive prompts.

Using g8 directly (Spark 2)
---------------------------

1. Install [`giter8`](https://github.com/n8han/giter8)

2. Run `g8 squito/cdh-spark` and follow the prompts


CDH5 / Spark 1.x
----------------

For creating a CDH5 / Spark 1.x application a separate branch of this repository can be used: `sbt new squito/cdh-spark.g8 --branch cdh5.x_spark1.x`. 



Setting up Access to Cloudera Private Maven Repos
====================


Some of the artifacts you need will be in a private cloudera repo, which requires authentication.  You can enable that by creating a `~/.m2/settings.xml` with this (or adding this to your existing one)

```
<settings>
  <servers>
    <server>
      <id>private-7.2.11.4</id>
      <username>[your username]</username>
      <password>[your password]</password>
    </server>
  </servers>
</settings>
```

Note that the `id` field has to match the `id` for the repository specified in `pom.xml`.  I haven't tried this yet, but I suspect you need to copy those for each releases repo.


Run the Example
===============

With Sbt
---------

1. Open an sbt session in project root : `sbt` then select the core project `project core`

2. Compile the code: `compile`

3. Run the app: `runMain <your-package>.SparkWordCount local[*] <some input file>`.  (If you don't
  specify an input file, it will just use the "pom.xml" sitting there.  It'll work, but not very
  interesting.)

With Maven
---------
After a maven build (at least a `mvn package`) execute `mvn exec:java -Dexec.classpathScope="compile" -pl core -Dexec.mainClass="<your-package>.SparkWordCount" -Dexec.args="local[*] <some input file>"`

Developing
================

Setting Up IntelliJ
-------------

1. Open IntelliJ

2. From the menu, choose "File / Import Project"

3. Choose the directory you have just created

4. Chose "Import Project From External Module / Maven"

5. Click through the remaining dialogs


Continuous Compilation With Sbt
------------

1. Open up an sbt session: `sbt`

2. Inside sbt, run `~compile`.  Leave the sbt session open.  After the first full compile, you'll see something like `1. Waiting for source changes... (press enter to interrupt)`.

3. Change code (with IntelliJ, vim, emacs, whatever).  Save your code, and watch sbt recompile.

Continuous Unit Testing With Sbt
------------

(first I need to write an example unit test)

Packaging Your Code
===================

You need to create a jar which contains all of your code & dependencies.  However, you also want to make sure
that your jar does *not* contain jars which are already available on the cluster.  This will help keep the jar
small, so it is quicker to package and send across the cluster (and also helps avoid confusing errors if
multiple versions of a library are included on the classpath).

Instructions to build these libraries vary slightly depending on the build tool.  Note that the project here
has been carefully configured to enable packaging to work this way -- eg., every sbt project won't necessarily
be able to build a jar like this.

After packaging your jar, you can launch a spark command on your cluster with `spark-submit`; just supply
your jar to the `--jars` argument.  Eg., 

```
spark-submit --master yarn --jars my_cool_project-core_2.10-0.1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.SparkWordCount
```

With Sbt
---------

Execute `sbt "project core" assembly`.

With Maven
----------

Execute `mvn package`.

This will create a jar like `core/target/my_cool_project-core_2.10-0.1.0-SNAPSHOT-jar-with-dependencies.jar`.
Add this to your `--jars` argument to `spark-submit` to run your code.

