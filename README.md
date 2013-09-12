TuringMachine
=============
Simple Turing machine written in Java.


Source control management
=========================
For easy development and code versioning we use Github.

Commit policy
-------------
Every commit is required to compile without failures.


Build management
================

To build and test this project we use maven:
    mvn install

Make sure you have properly set up the following tools:
- JDK 1.7
- Maven


How to run
==========

We recommend to run the jar file from the command line. The name of the Turing
machine program to execute is passed as the only argument. Currently, the
following machines are implemented:
- multiplication
- factorial

Example:

	java -jar TuringMachine.jar multiplication


Authors
=======

- Marc-André Bühler
- Marco Buess
- Samuel Reutimann


License
=======

Check the LICENSE.md file for more information.
