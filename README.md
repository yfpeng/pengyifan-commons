Yet another Java tree structure
=========

There are a couple of tree data structures in Java, such as [`DefaultMutableTreeNode`](http://docs.oracle.com/javase/6/docs/api/javax/swing/tree/DefaultMutableTreeNode.html) in JDK Swing, [`Tree`](http://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/trees/Tree.html) in Stanford parser package, and other toy codes. But none of these are sufficient yet small enough for general purpose.

### Contribution (:))

This project attempts to provide another general-purpose tree data structure in Java. The difference between this and others are
*  **Totally free**. You can use it anywhere (except in your homework :P)
*  **Small but general enough**. I put everything of the data structure in one class file, so it would be easy to copy/paste.
*  **Not just a toys**. I am aware of dozens of Java tree codes that can only handle binary trees or limited operations. This `TreeNode` is much more than that. It provides different ways of visiting nodes, such as preorder, postorder, breadthfirst, leaves, path to root, etc. Moreover, iterators are provided too for the sufficiency.
*  **More utils will be added**. I am willing to add more operations to make this project comprehensive, especially if you send a request through [**github**](https://github.com/yfpeng/java-tree).

### TreeNode

A `TreeNode` may have at most one parent and 0 or more children. It provides operations for examining and modifying a node's parent and children and also operations for examining the tree that the node is a part of. A node's tree is the set of all nodes that can be reached by starting at the node and following all the possible links to parents and children. A node with no parent is the root of its tree; a node with no children is a leaf. A tree may consist of many subtrees, each node acting as the root for its own subtree.

A `TreeNode` provides iterator for efficiently traversing a tree or subtree in various orders or for following the path between two nodes. A `TreeNode` may also hold a reference to a user object, the use of which is left to the user. Asking a `TreeNode` for its string representation with *toString()* returns the string representation of its user object.

This is not a thread safe class. If you intend to use a `TreeNode` (or a tree of TreeNodes) in more than one thread, you need to do your own synchronizing. A good convention to adopt is synchronizing on the root node of a tree. 

Most source code is copied from `DefaultMutableTreeNode` by Rob Davis, and modified for general purposes. 

### Print TreeNode

This project provides two formats to print out a tree structure. One is a human readable format (`TreeString`), like

    └ a
      ├ b
      ├ c
      │ ├ e
      │ └ f
      └ d

The other is Penn Treebank format (`PtbString`), like

    (a b (c (e f) d)
    
The following code snippet shows how to print a `TreeNode`.

```java
System.out.println(PtbString.toString(treeNode));
System.out.println(TreeString.toString(treeNode));
```

### Other utils

comming soon...

Some frequently used utils will be provded, such as *lowest common ancestor*, *tregrep*, etc.
