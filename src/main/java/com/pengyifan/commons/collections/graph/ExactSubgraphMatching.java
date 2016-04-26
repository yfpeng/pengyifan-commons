package com.pengyifan.commons.collections.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The Exact Subgraph Matching (ESM) algorithm Java implementation for subgraph
 * isomorphism problems
 * <p>
 * The subgraph isomorphism problem is NP-hard. We designed a simple exact
 * subgraph matching (ESM) algorithm for dependency graphs using a backtracking
 * approach. This algorithm is designed to process dependency graphs (directed
 * graphs, direction from governor token to dependent token) from dependency
 * parsers for biological relation and event extraction. It has been
 * demonstrated to be very efficient in these biological information extraction
 * applications.
 * <p>
 * The total worst-case algorithm complexity is O(<i>n</i>^2 *
 * <i>k</i>^<i>n</i>) where n is the number of vertices and k is the vertex
 * degree (number of adjacent edges). The best-case algorithm complexity is
 * O(n^3 * k^2).
 * <p>
 * For more details about our ESM algorithm, the analysis on time complexity,
 * and the different applications of the algorithm, see the section
 * "Related Publications" in the README file for the complete list of our
 * ESM-related publications.
 * <p>
 * This ESM implementation also provides a function to determine the graph
 * isomorphism between two graphs.
 * </p>
 *
 * @author "Yifan Peng"
 */
public class ExactSubgraphMatching<V, E> {

  /**
   * subgraph to be matched (normally smaller graph)
   */
  private final DirectedLoopGraph<V, E> subgraph;
  /**
   * graph to be matched (normally bigger graph)
   */
  private final DirectedLoopGraph<V, E> graph;
  /**
   * startnode of subgraph for matching (from which subgraph node to start the
   * matching process)
   */
  private V subgraphStartNode;
  /**
   * a set of startnodes of graph for matching
   */
  private List<V> graphStartNodes;

  /**
   * Constructor to initialize the subgraph and graph and specify the start
   * node of the subgraph and the set of start nodes of the graph
   *
   * @param subgraph subgraph (supposed to be smaller)
   * @param graph    graph (supposed to be bigger)
   */
  public ExactSubgraphMatching(DirectedLoopGraph<V, E> subgraph, DirectedLoopGraph<V, E> graph) {
    this.graph = graph;
    this.subgraph = subgraph;
    // set the startnode of subgraph
    subgraphStartNode = getRandomStartNode(Lists.newArrayList(subgraph
        .vertexSet()));
    graphStartNodes = Lists.newArrayList(graph.vertexSet());
  }

  /**
   * Randomly choose the start node of the subgraph (default setting)
   *
   * @param subgraphNodes
   * @return start node of the subgraph
   */
  private V getRandomStartNode(
      List<V> subgraphNodes) {
    // Generate a random number (index) with the size of the list being the
    // maximum
    return subgraphNodes.get(new Random().nextInt(subgraphNodes.size()));
  }

  /**
   * Instead of the default random start node, users can specify the start node
   * for the subgraph
   *
   * @param vertex user-specified start node
   */
  public void setSubgraphStartNode(V vertex) {
    subgraphStartNode = vertex;
  }

  /**
   * The default setting for choosing the set of start nodes for the graph is
   * use all nodes in the graph as start nodes. However, users can set the set
   * of start nodes for the graph to specify which specific set of nodes they
   * want to use to compare with the subgraph start node. This will narrow down
   * the search by avoiding to match the subgraph start node with every graph
   * node. Consequently, more efficient.
   *
   * @param vertices user-specified set of start nodes
   */
  public void setGraphStartNodes(List<V> vertices) {
    graphStartNodes = vertices;
  }

  /**
   * Retrieve specific matchings between the subgraph and the graph matching
   * result is store in a map. the key is the node in the subgraph and the
   * value is the injective matching node in the graph Since a subgraph can
   * match different places of a graph, we record all the matchings by putting
   * each matching into a List
   *
   * @return a list of matchings between two graphs
   */
  public List<Map<V, V>> getSubgraphMatchingMatches() {
    checkArgument(
        isSubgraphSmaller(),
        "The size of the subgraph: %s is bigger than the size of the graph %s.",
        subgraph.vertexSet().size(),
        graph.vertexSet().size());

    List<Map<V, V>> matches = null;

    for (int i = 0; i < graphStartNodes.size(); i++) {
      Map<V, V> subgraphToGraph = Maps.newHashMap();
      Map<V, V> graphToSubgraph = Maps.newHashMap();
      List<Map<V, V>> total = Lists.newArrayList();
      List<V> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));

      if (matchNodeForAllMatches(
          toMatch,
          subgraphToGraph,
          graphToSubgraph,
          total)) {
        if (matches == null) {
          matches = Lists.newArrayList(total);
          continue;
        }
        for (Map<V, V> m : total) {
          boolean flag = true;
          for (Map<V, V> n : matches) {
            if (equalMaps(m, n)) {
              flag = false;
            }
          }
          if (flag) {
            matches.add(m);
          }
        }
      }
    }
    return matches;
  }

  /**
   * Check if two maps are equal
   *
   * @param m1 : first map
   * @param m2 : second map
   * @return true of false
   */
  private boolean equalMaps(
      Map<V, V> m1,
      Map<V, V> m2) {
    if (m1.size() != m2.size()) {
      return false;
    }
    for (V key : m1.keySet()) {
      if (!m1.get(key).equals(m2.get(key))) {
        return false;
      }
    }
    return true;
  }

  /**
   * The main recursive function for subgraph isomorphism this function helps
   * retrieve all possible matchings between two graphs because a subgraph can
   * have multiple matchings with a graph
   *
   * @param toMatchs         : nodes to be matched in two graphs
   * @param subgraphToGraphs : map to record mapping from the subgraph to the graph
   * @param graphToSubgraphs : map to record mapping from the graph to the subgraph
   * @param total            : store all possible matchings between two graphs
   * @return true if the matching is successful
   */
  private boolean matchNodeForAllMatches(List<V> toMatchs,
      Map<V, V> subgraphToGraphs,
      Map<V, V> graphToSubgraphs,
      List<Map<V, V>> total) {
    // generate local copies
    List<V> toMatch = Lists.newArrayList(toMatchs);
    Map<V, V> subgraphToGraph = Maps.newHashMap(subgraphToGraphs);
    Map<V, V> graphToSubgraph = Maps.newHashMap(graphToSubgraphs);

    while (toMatch.size() != 0) {
      V noder = toMatch.remove(0);
      V nodes = toMatch.remove(0);

      // this is the place to check whether they can be matched
      if (subgraphToGraph.containsKey(noder)
          && !graphToSubgraph.containsKey(nodes)) {
        return false;
      }
      if (!subgraphToGraph.containsKey(noder)
          && graphToSubgraph.containsKey(nodes)) {
        return false;
      }
      if (subgraphToGraph.containsKey(noder)
          && graphToSubgraph.containsKey(nodes)) {
        if (subgraphToGraph.get(noder).equals(nodes)
            && graphToSubgraph.get(nodes).equals(noder)) {
          // do nothing
        } else {
          return false;
        }
      }

      // Here we can make checks whether noder and nodes should match
      if (!matchNodeContent(noder, nodes)) {
        return false;
      }

      // record the injective match
      subgraphToGraph.put(noder, nodes);
      graphToSubgraph.put(nodes, noder);

      // one direction match (as governor)
      for (E e : subgraph.outgoingEdgesOf(noder)) {
        V r = subgraph.getEdgeTarget(e);
        List<V> candidateNodes = Lists.newArrayList();
        for (E s : graph.outgoingEdgesOf(nodes)) {
          if (matchEdge(e, s)) {
            candidateNodes.add(graph.getEdgeTarget(s));
          }
        }
        boolean flag = false;
        boolean terminate = false;
        for (V s : candidateNodes) {
          if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            if (subgraphToGraph.get(r).equals(s)
                && graphToSubgraph.get(s).equals(r)) {
              terminate = true;
              break;
            } else {
              continue;
            }
          }
          List<V> toMatchTemp = Lists.newArrayList(toMatch);
          toMatchTemp.add(noder);
          toMatchTemp.add(nodes);
          toMatchTemp.add(r);
          toMatchTemp.add(s);
          if (matchNodeForAllMatches(
              toMatchTemp,
              subgraphToGraph,
              graphToSubgraph,
              total)) {
            flag = true;
          }
        }
        if (terminate) {
          continue;
        }
        return flag;
      }

      // the other direction match (as dependent)
      for (E e : subgraph.incomingEdgesOf(noder)) {
        V r = subgraph.getEdgeSource(e);
        List<V> candidateNodes = Lists.newArrayList();
        for (E s : graph.incomingEdgesOf(nodes)) {
          if (matchEdge(e, s)) {
            candidateNodes.add(graph.getEdgeSource(s));
          }
        }
        boolean flag = false;
        boolean terminate = false;
        for (V s : candidateNodes) {
          if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            if (subgraphToGraph.get(r).equals(s)
                && graphToSubgraph.get(s).equals(r)) {
              terminate = true;
              break;
            } else {
              continue;
            }
          }
          List<V> toMatchTemp = Lists.newArrayList(toMatch);
          toMatchTemp.add(noder);
          toMatchTemp.add(nodes);
          toMatchTemp.add(r);
          toMatchTemp.add(s);
          if (matchNodeForAllMatches(
              toMatchTemp,
              subgraphToGraph,
              graphToSubgraph,
              total)) {
            flag = true;
          }
        }
        if (terminate) {
          continue;
        }
        return flag;
      }

    }

    // success return
    total.add(subgraphToGraph);
    return true;
  }

  /**
   * Check if the input subgraph is subsumed by the input graph
   *
   * @return true or false
   */
  public boolean isSubgraphIsomorphism() {
    if (!isSubgraphSmaller()) {
      System.err.println("The size of the subgraph: "
          +
          subgraph.vertexSet().size()
          + " is bigger the size of the graph "
          +
          graph.vertexSet().size()
          + ". Please check.");
      return false;
    }
    boolean isSubgraphIsomorphism = false;
    for (int i = 0; i < graphStartNodes.size(); i++) {
      Map<V, V> subgraphToGraph = Maps.newHashMap();
      Map<V, V> graphToSubgraph = Maps.newHashMap();
      List<V> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));

      if (matchNodeForSingleMatch(
          toMatch,
          subgraphToGraph,
          graphToSubgraph,
          subgraph,
          graph)) {
        isSubgraphIsomorphism = true;
        break;
      }
    }
    return isSubgraphIsomorphism;
  }

  /**
   * Provide an additional function to determine if two graphs are isomorphic
   * to each other based on the fact that if two graphs are subgraph isomorphic
   * to each other, then they are isomorphic to each other
   *
   * @return true or false
   */
  public boolean isGraphIsomorphism() {
    if (!isGraphSizeSame()) {
      System.err.println("The size of the subgraph: "
          +
          subgraph.vertexSet().size()
          + " is not same as the size of the graph "
          +
          graph.vertexSet().size()
          + ". Please check.");
      return false;
    }
    boolean isGraphIsomorphism = false;
    boolean isSubgraphIsomorphicToGraph = false;
    boolean isgraphIsomorphicToSubgraph = false;
    // subgraph against graph
    for (int i = 0; i < graphStartNodes.size(); i++) {
      Map<V, V> subgraphToGraph = Maps.newHashMap();
      Map<V, V> graphToSubgraph = Maps.newHashMap();
      List<V> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));
      if (matchNodeForSingleMatch(
          toMatch,
          subgraphToGraph,
          graphToSubgraph,
          subgraph,
          graph)) {
        isSubgraphIsomorphicToGraph = true;
        break;
      }
    }

    // graph against subgraph
    // reset the startnode(s)
    subgraphStartNode = getRandomStartNode(Lists.newArrayList(graph.vertexSet()));
    graphStartNodes = Lists.newArrayList(graph.vertexSet());
    for (int i = 0; i < graphStartNodes.size(); i++) {
      Map<V, V> subgraphToGraph = Maps.newHashMap();
      Map<V, V> graphToSubgraph = Maps.newHashMap();
      List<V> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));
      if (matchNodeForSingleMatch(
          toMatch,
          subgraphToGraph,
          graphToSubgraph,
          graph,
          subgraph)) {
        isgraphIsomorphicToSubgraph = true;
        break;
      }
    }

    if (isSubgraphIsomorphicToGraph && isgraphIsomorphicToSubgraph) {
      isGraphIsomorphism = true;
    }

    // set the startnode(s) back
    subgraphStartNode = getRandomStartNode(Lists.newArrayList(subgraph.vertexSet()));
    graphStartNodes = Lists.newArrayList(graph.vertexSet());

    return isGraphIsomorphism;
  }

  /**
   * The main recursive function for subgraph isomorphism this function only
   * retrieve one possible matchings between two graphs. As long as it finds an
   * isomorphic subgraph, it returns. Thus, this function is used to determine
   * the subgraph isomorphism, instead of retrieving all possible matchings
   * between two graphs. Therefore, faster.
   *
   * @param toMatchs         nodes to be matched in two graphs
   * @param subgraphToGraphs map to record mapping from the subgraph to the graph
   * @param graphToSubgraphs map to record mapping from the graph to the subgraph
   * @param subgraph         the input subgraph
   * @param graph            the input graph
   * @return boolean to indicate if the subgraph isomorphism exists or not
   */
  private boolean matchNodeForSingleMatch(List<V> toMatchs,
      Map<V, V> subgraphToGraphs,
      Map<V, V> graphToSubgraphs,
      DirectedLoopGraph<V, E> subgraph,
      DirectedLoopGraph<V, E> graph) {
    // generate local copies
    List<V> toMatch = Lists.newArrayList(toMatchs);
    Map<V, V> subgraphToGraph = Maps.newHashMap(subgraphToGraphs);
    Map<V, V> graphToSubgraph = Maps.newHashMap(graphToSubgraphs);

    while (toMatch.size() != 0) {
      V noder = toMatch.remove(0);
      V nodes = toMatch.remove(0);
      // System.out.println("before " + noder.getToken() + " -> " +
      // nodes.getToken());
      // this is the place to check whether they can be matched
      if (subgraphToGraph.containsKey(noder)
          && !graphToSubgraph.containsKey(nodes)) {
        return false;
      }
      if (!subgraphToGraph.containsKey(noder)
          && graphToSubgraph.containsKey(nodes)) {
        return false;
      }
      if (subgraphToGraph.containsKey(noder)
          && graphToSubgraph.containsKey(nodes)) {
        if (subgraphToGraph.get(noder).equals(nodes)
            && graphToSubgraph.get(nodes).equals(noder)) {
          // do nothing
        } else {
          return false;
        }
      }

      // Here we can make checks whether noder and nodes should match
      if (!matchNodeContent(noder, nodes)) {
        return false;
      }

      // record the injective match
      subgraphToGraph.put(noder, nodes);
      graphToSubgraph.put(nodes, noder);
      // System.out.println("after " + noder.getToken() + " -> " +
      // nodes.getToken());

      // one direction match (as governor)
      for (E e : subgraph.outgoingEdgesOf(noder)) {
        V r = subgraph.getEdgeTarget(e);
        List<V> candidateNodes = Lists.newArrayList();
        for (E s : graph.outgoingEdgesOf(nodes)) {
          if (matchEdge(e, s)) {
            candidateNodes.add(graph.getEdgeTarget(s));
          }
        }
        boolean terminate = false;
        for (V s : candidateNodes) {
          if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            if (subgraphToGraph.get(r).equals(s)
                && graphToSubgraph.get(s).equals(r)) {
              terminate = true;
              break;
            } else {
              continue;
            }
          }
          List<V> toMatchTemp = Lists.newArrayList(toMatch);
          toMatchTemp.add(noder);
          toMatchTemp.add(nodes);
          toMatchTemp.add(r);
          toMatchTemp.add(s);
          if (matchNodeForSingleMatch(
              toMatchTemp,
              subgraphToGraph,
              graphToSubgraph,
              subgraph,
              graph)) {
            subgraphToGraphs = Maps.newHashMap(subgraphToGraph);
            graphToSubgraphs = Maps.newHashMap(graphToSubgraph);
            return true;
          }
        }
        if (terminate) {
          continue;
        }

        return false;
      }

      // the other direction match (as dependent)
      for (E e : subgraph.incomingEdgesOf(noder)) {
        V r = subgraph.getEdgeSource(e);
        List<V> candidateNodes = Lists.newArrayList();
        for (E s : graph.incomingEdgesOf(nodes)) {
          if (matchEdge(e, s)) {
            candidateNodes.add(graph.getEdgeSource(s));
          }
        }
        boolean terminate = false;
        for (V s : candidateNodes) {
          if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            continue;
          }
          if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
            if (subgraphToGraph.get(r).equals(s)
                && graphToSubgraph.get(s).equals(r)) {
              terminate = true;
              break;
            } else {
              continue;
            }
          }
          List<V> toMatchTemp = Lists.newArrayList(
              toMatch);
          toMatchTemp.add(noder);
          toMatchTemp.add(nodes);
          toMatchTemp.add(r);
          toMatchTemp.add(s);
          if (matchNodeForSingleMatch(
              toMatchTemp,
              subgraphToGraph,
              graphToSubgraph,
              subgraph,
              graph)) {
            subgraphToGraphs = Maps.newHashMap(subgraphToGraph);
            graphToSubgraphs = Maps.newHashMap(graphToSubgraph);
            return true;
          }
        }
        if (terminate)
          continue;

        return false;
      }

    }

    // success return
    subgraphToGraphs = Maps.newHashMap(subgraphToGraph);
    graphToSubgraphs = Maps.newHashMap(graphToSubgraph);
    // for(Entry<V, V> entry :
    // subgraphToGraphs.entrySet())
    // System.out.println(entry.getKey().getToken() + " -> " +
    // entry.getValue().getToken());
    return true;
  }

  /**
   * Determine if two nodes from two graphs should match with each other or not
   * Current implementation check the compareForm in each node, which includes
   * the generalized POS tag and the lemma information computed by the
   * BioLemmatizer
   *
   * @param noder : node in the subgraph
   * @param nodes : node in the graph
   * @return true of false
   */
  private boolean matchNodeContent(V noder, V nodes) {
    return noder.equals(nodes);
  }

  private boolean matchEdge(E edger, E edges) {
    return edger.equals(edges);
  }

  /**
   * Sanity check if the specified subgraph is smaller or equal to the
   * specified graph This function is called first when determining the
   * subgraph isomorphism
   *
   * @return true or false
   */
  private Boolean isSubgraphSmaller() {
    return subgraph.vertexSet().size() <= graph.vertexSet().size();
  }

  /**
   * Sanity check if the specified subgraph is equal to the specified graph
   * This function is called first when determining the graph isomorphism
   *
   * @return true or false
   */
  private Boolean isGraphSizeSame() {
    return subgraph.vertexSet().size() == graph.vertexSet().size();
  }

}
