The Common is a project focused on various aspects of resuable Java components. Its principal goal is to try new things! Therefore, if I find a good thrid-party package that can fulfill the same function, I would rather reuse it, unless I feel interested to know how they work.

The Common is written in Java and licensed under the BSD 3-clause license. Note that you can use it anywhere, except in your homework :P. 

I welcome participation from all that are interested, at all skill levels. Coding, documentation and testing are all critical parts of the softeware development process. If you are interested in participating in any of these aspects, please join me!

The Common includes but not limited to the following components

1. Collections: extends the Java collections
   1. [Most efficient way to increment a Map value in Java — Only search the key once](http://blog.pengyifan.com/most-efficient-way-to-increment-a-map-value-in-java-only-search-the-key-once/)
   1. [Yet another Java tree structure](http://blog.pengyifan.com/yet-another-java-tree-structure/)
   1. [Fibonacci Heap](http://blog.pengyifan.com/a-java-implementation-of-fibonacci-heep/)
1. IO: collection of I/O utilities
1. Lang: extra functionality for classes in java.lang
   1. [Indent/Hanging indent a paragraph](http://blog.pengyifan.com/using-regex-to-hanging-indent-a-paragraph-in-java/)
1. Ling: functionality for natural language processing 
1. [Brat standoff format](http://brat.nlplab.org/standoff.html)  
    Brat format is widely used in the BioNLP Shared Task 2009, 2011, and 2013. One of its advantages is that annotations are stored separately from the annotated document text. Common includes fully supports of the Brat format, including reading/writing/searching text and annotation files (entity, relation, event, modification, normalization, and note). For more details about the Brat format, please visit the [link](http://brat.nlplab.org/standoff.html)
1. Math: Common classes used throughout the math library.

### Getting started

The latest release is 0.1.0-SNAPSHOT.

It is available in Maven Central as com.pengyifan:pengyifan-commons:jar:0.1.0-SNAPSHOT

```XML
<repositories>
    <repository>
        <id>oss-sonatype</id>
        <name>oss-sonatype</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
...
<dependency>
  <groupId>com.pengyifan</groupId>
  <artifactId>pengyifan-commons</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Copyright (c) 2015 by Yifan Peng. All Rights Reserved. 
