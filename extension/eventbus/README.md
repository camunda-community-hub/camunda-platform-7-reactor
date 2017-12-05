# project-reactor eventbus

The camunda-bpm-reactor extension relies heavily on
the eventbus provided by projectreactor in version 2.0.8.

Unfortunately, with the new mjor release of projectreactor (3.x)
the eventbus is no longer maintained nor was it migrated to the new version,
see issue https://github.com/reactor/reactor-bus/issues/3.

We decided to copy the sources to this module and upgrade the functionality
to support java 8.

This module does not depend on camunda 
and can be used as replacement for the
2.0.8.RELEASE version of eventbus and
reactor-core.

Changes made:

- the fn classes (Supplier, Function, Consumer, ...) where replaced by the java.function classes
- the tupleN classes where replaced by vavr
- the gs-collections where replaced by eclipse-collections
- the sources of core and eventbus where merged in one package-structure

If the reactor eventbus should be released in a java8 compatible manner with latest lib support in the future,
we will consider dropping this module in favor of the "official" library, but as of today, this seems unlikely.

Please not that mixing this code with official releases of projectreactor (neither the 2.0.x nor the 3.x branch)
has not been tested and should be avoided or at least handled with care. 
