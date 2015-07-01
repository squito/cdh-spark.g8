A giter8 template for getting setup with Spark in CDH.

Usage
====

1. Install [`giter8`](https://github.com/n8han/giter8)

2. Run `g8 squito/cdh-spark` and follow the prompts

3. Compile your code with either sbt (my preference) or mvn.
    * Incremental compilation by opening an sbt session (`sbt`) then run `~compile`

Run the Example
======

With Sbt
---------

1. Open an sbt session in project "core" : `sbt` \ `project core`

2. Compile the code: `compile`

3. Run the app: `run-main <your-package>.SparkWordCount local[*] <some input file>`.  (If you don't
  specify an input file, it will just use the "pom.xml" sitting there.  It'll work, but not very
  interesting.)


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

2. Inside sbt, run `~compile`

3. Change code (with IntelliJ, vim, emacs, whatever).  Save your code, and watch sbt recompile


Continuous Unit Testing With Sbt
------------
