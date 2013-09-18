Yet another Java tree structure
=========

There are a couple of tree data structures in Java, such as [`DefaultMutableTreeNode`](http://docs.oracle.com/javase/6/docs/api/javax/swing/tree/DefaultMutableTreeNode.html) in JDK Swing, [`Tree`](http://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/trees/Tree.html) in Stanford parser package, and other toy codes. But none of these are sufficient enough for general purpose.

This project attempts to provide a general-purpose node in a tree data structure. 

## TreeNode

A `TreeNode` may have at most one parent and 0 or more children. It provides operations for examining and modifying a node's parent and children and also operations for examining the tree that the node is a part of. A node's tree is the set of all nodes that can be reached by starting at the node and following all the possible links to parents and children. A node with no parent is the root of its tree; a node with no children is a leaf. A tree may consist of many subtrees, each node acting as the root for its own subtree.

A `TreeNode` provides iterator for efficiently traversing a tree or subtree in various orders or for following the path between two nodes. A `TreeNode` may also hold a reference to a user object, the use of which is left to the user. Asking a <code>TreeNode</code> for its string representation with <code>toString()</code> returns the string representation of its user object.

This is not a thread safe class.</b>If you intend to use a TreeNode (or a tree of TreeNodes) in more than one thread, you need to do your own synchronizing. A good convention to adopt is synchronizing on the root node of a tree. 

Most source code is copied from <code>DefaultMutableTreeNode</code> by Rob Davis, and modified for general purpose. 

## Print TreeNode

This project provides two formats to print out a tree structure. One is a human readable format (`TreeString`), like

    └ a
      ├ b
      ├ c
      │ ├ e
      │ └ f
      └ d

The other is Penn Treebank format (`PtbString`), like

    (a b (c (e f) d)
    
The following code snippet shows how to print a `TreeNode`

```java
System.out.println(PtbString.toString(treeNode));
System.out.println(TreeString.toString(treeNode));
```

## Other utils

comming soon...

Some frequently used utils will be provded, such as *lowest common ancestor*, *tregrep*, etc.
