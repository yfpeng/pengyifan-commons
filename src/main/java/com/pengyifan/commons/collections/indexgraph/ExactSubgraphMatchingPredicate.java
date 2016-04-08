package com.pengyifan.commons.collections.indexgraph;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.javatuples.Pair;
import org.jgrapht.DirectedGraph;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
public class ExactSubgraphMatchingPredicate<V, E extends IndexObject> {

  /**
   * subgraph to be matched (normally smaller graph)
   */
  private final DirectedGraph<Predicate<V>, Predicate<E>> subgraph;
  /**
   * graph to be matched (normally bigger graph)
   */
  private final DirectedGraph<V, E> graph;
  /**
   * startnode of subgraph for matching (from which subgraph node to start the
   * matching process)
   */
  private final Predicate<V> subgraphStartNode;
  /**
   * a set of startnodes of graph for matching
   */
  private final List<V> graphStartNodes;

  /**
   * Constructor to initialize the subgraph and graph and specify the start
   * node of the subgraph and the set of start nodes of the graph
   *
   * @param subgraph subgraph (supposed to be smaller)
   * @param graph    graph (supposed to be bigger)
   */
  public ExactSubgraphMatchingPredicate(
      DirectedGraph<Predicate<V>, Predicate<E>> subgraph,
      DirectedGraph<V, E> graph) {
    this.graph = graph;
    this.subgraph = subgraph;
    // set the startnode of subgraph
    subgraphStartNode = getRandomStartNode(subgraph.vertexSet());
    graphStartNodes = Lists.newArrayList(graph.vertexSet());
  }

  /**
   * randomly choose the start node of the subgraph (default setting)
   *
   * @return start node of the subgraph
   */
  private <A> A getRandomStartNode(Collection<A> nodes) {
    List<A> list = Lists.newArrayList(nodes);
    return list.get(new Random().nextInt(list.size()));
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
  public List<Map<Predicate<V>, V>> getSubgraphMatchingMatches() {
    List<Map<Predicate<V>, V>> matches = Lists.newArrayList();

    for (V graphStartNode : graphStartNodes) {
      LinkedList<Pair<Predicate<V>, V>> toMatch = Lists.newLinkedList();
      toMatch.add(Pair.with(subgraphStartNode, graphStartNode));

      List<Map<Predicate<V>, V>> total = Lists.newArrayList();
      Map<Predicate<V>, V> subgraphToGraph = Maps.newHashMap();
      Map<V, Predicate<V>> graphToSubgraph = Maps.newHashMap();
      if (matchHelper(toMatch, subgraphToGraph, graphToSubgraph, total)) {
        addIfNotExist(matches, total);
      }
    }
    return matches;
  }

  private <A, B> void addIfNotExist(List<Map<A, B>> src, List<Map<A, B>> dst) {
    dst.stream().forEach(m -> {
      if (!src.contains(m)) {
        src.add(m);
      }
    });
  }

  /**
   * The main recursive function for subgraph isomorphism this function helps
   * retrieve all possible matchings between two graphs because a subgraph can
   * have multiple matchings with a graph
   *
   * @param toMatchs         nodes to be matched in two graphs
   * @param subgraphToGraphs : map to record mapping from the subgraph to the
   *                         graph
   * @param graphToSubgraphs : map to record mapping from the graph to the
   *                         subgraph
   * @param total            : store all possible matchings between two graphs
   * @return boolean to indicate if the matching is successful or not
   */
  private boolean matchHelper(
      LinkedList<Pair<Predicate<V>, V>> toMatchs,
      Map<Predicate<V>, V> subgraphToGraphs,
      Map<V, Predicate<V>> graphToSubgraphs,
      List<Map<Predicate<V>, V>> total) {

    LinkedList<Pair<Predicate<V>, V>> toMatch = Lists.newLinkedList(toMatchs);
    Map<Predicate<V>, V> subgraphToGraph = Maps.newHashMap(subgraphToGraphs);
    Map<V, Predicate<V>> graphToSubgraph = Maps.newHashMap(graphToSubgraphs);

    while (!toMatch.isEmpty()) {
      Pair<Predicate<V>, V> pair = toMatch.pop();
      Predicate<V> pred = pair.getValue0();
      V node = pair.getValue1();

      // this is the place to check whether they can be matched
      if (isInMap(pred, node, subgraphToGraph, graphToSubgraph) == Status.NOT_MATCHED) {
        return false;
      }
      // Here we can make checks whether node should match
      if (!pred.test(node)) {
        return false;
      }

      // record the injective match
      subgraphToGraph.put(pred, node);
      graphToSubgraph.put(node, pred);

      // outgoing match
      AllStatus rst = matchOutgoing(pred, node, toMatch, subgraphToGraph, graphToSubgraph, total);
      if (rst == AllStatus.TRUE) {
        return true;
      } else if (rst == AllStatus.FALSE) {
        return false;
      }

      // incoming match
      rst = matchIncoming(pred, node, toMatch, subgraphToGraph, graphToSubgraph, total);
      if (rst == AllStatus.TRUE) {
        return true;
      } else if (rst == AllStatus.FALSE) {
        return false;
      }
    }

    // success return
    total.add(subgraphToGraph);
    return true;
  }

  private AllStatus matchIncoming(
      Predicate<V> thisPred,
      V thisNode,
      LinkedList<Pair<Predicate<V>, V>> toMatch,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph,
      List<Map<Predicate<V>, V>> total) {
    for (Predicate<E> edgePred : subgraph.incomingEdgesOf(thisPred)) {
      AllStatus rst = allMatchHelper(
          thisPred,
          thisNode,
          subgraph.getEdgeSource(edgePred),
          graph.incomingEdgesOf(thisNode)
              .stream()
              .filter(edgePred)
              .map(e -> graph.getEdgeSource(e))
              .collect(Collectors.toList()),
          toMatch,
          subgraphToGraph,
          graphToSubgraph,
          total);
      if (rst == AllStatus.TRUE || rst == AllStatus.FALSE) {
        return rst;
      }
    }
    return AllStatus.CONTINUE;
  }

  private AllStatus matchOutgoing(
      Predicate<V> thisPred,
      V thisNode,
      LinkedList<Pair<Predicate<V>, V>> toMatch,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph,
      List<Map<Predicate<V>, V>> total) {
    for (Predicate<E> edgePred : subgraph.outgoingEdgesOf(thisPred)) {
      AllStatus rst = allMatchHelper(
          thisPred,
          thisNode,
          subgraph.getEdgeTarget(edgePred),
          graph.outgoingEdgesOf(thisNode)
              .stream()
              .filter(edgePred)
              .map(e -> graph.getEdgeTarget(e))
              .collect(Collectors.toList()),
          toMatch,
          subgraphToGraph,
          graphToSubgraph,
          total);
      if (rst == AllStatus.TRUE || rst == AllStatus.FALSE) {
        return rst;
      }
    }
    return AllStatus.CONTINUE;
  }

  /**
   * 1 true, 0 false, 2 continue
   */
  private AllStatus allMatchHelper(
      Predicate<V> thisPred,
      V thisNode,
      Predicate<V> nextPred,
      List<V> nextNodes,
      LinkedList<Pair<Predicate<V>, V>> toMatch,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph,
      List<Map<Predicate<V>, V>> total) {
    boolean flag = false;
    boolean terminate = false;
    for (V nextNode : nextNodes) {
      Status status = isInMap(nextPred, nextNode, subgraphToGraph, graphToSubgraph);
      if (status == Status.NOT_MATCHED) {
        continue;
      } else if (status == Status.MATCHED) {
        terminate = true;
        break;
      }
      LinkedList<Pair<Predicate<V>, V>> toMatchTemp = Lists.newLinkedList(toMatch);
      toMatchTemp.push(Pair.with(thisPred, thisNode));
      toMatchTemp.push(Pair.with(nextPred, nextNode));
      if (matchHelper(toMatchTemp, subgraphToGraph, graphToSubgraph, total)) {
        flag = true;
      }
    }
    if (terminate) {
      return AllStatus.CONTINUE;
    }
    if (flag) {
      return AllStatus.TRUE;
    }

    return AllStatus.FALSE;
  }

  /**
   * Return true if the input predicate graph is subsumed by the input graph
   *
   * @return true or false
   */
  public boolean isSubgraphIsomorphism() {
    for (V graphStartNode : graphStartNodes) {
      LinkedList<Pair<Predicate<V>, V>> toMatch = Lists.newLinkedList();
      toMatch.add(Pair.with(subgraphStartNode, graphStartNode));

      Map<Predicate<V>, V> subgraphToGraph = Maps.newHashMap();
      Map<V, Predicate<V>> graphToSubgraph = Maps.newHashMap();

      if (matchHelper(toMatch, subgraphToGraph, graphToSubgraph)) {
        return true;
      }
    }
    return false;
  }

  /**
   * The main recursive function for subgraph isomorphism this function only
   * retrieve one possible matchings between two graphs. As long as it finds an
   * isomorphic subgraph, it returns. Thus, this function is used to determine
   * the subgraph isomorphism, instead of retrieving all possible matchings
   * between two graphs. Therefore, faster.
   *
   * @param toMatchs         : nodes to be matched in two graphs
   * @param subgraphToGraphs : map to record mapping from the subgraph to the
   *                         graph
   * @param graphToSubgraphs : map to record mapping from the graph to the
   *                         subgraph
   * @return boolean to indicate if the subgraph isomorphism exists or not
   */
  private boolean matchHelper(LinkedList<Pair<Predicate<V>, V>> toMatchs,
      Map<Predicate<V>, V> subgraphToGraphs,
      Map<V, Predicate<V>> graphToSubgraphs) {
    LinkedList<Pair<Predicate<V>, V>> toMatch = Lists.newLinkedList(toMatchs);
    Map<Predicate<V>, V> subgraphToGraph = Maps.newHashMap(subgraphToGraphs);
    Map<V, Predicate<V>> graphToSubgraph = Maps.newHashMap(graphToSubgraphs);

    while (!toMatch.isEmpty()) {
      Pair<Predicate<V>, V> pair = toMatch.pop();
      Predicate<V> pred = pair.getValue0();
      V node = pair.getValue1();

      // this is the place to check whether they can be matched
      if (isInMap(pred, node, subgraphToGraph, graphToSubgraph) == Status.NOT_MATCHED) {
        return false;
      }
      // Here we can make checks whether noder and nodes should match
      if (!pred.test(node)) {
        return false;
      }

      // record the injective match
      subgraphToGraph.put(pred, node);
      graphToSubgraph.put(node, pred);

      // outgoing match
      if (!matchOutgoing(pred, node, toMatch, subgraphToGraph, graphToSubgraph)) {
        return false;
      }

      // incoming match
      if (!matchIncoming(pred, node, toMatch, subgraphToGraph, graphToSubgraph)) {
        return false;
      }
    }

    return true;
  }

  private boolean matchOutgoing(
      Predicate<V> thisPred,
      V thisNode,
      LinkedList<Pair<Predicate<V>, V>> toMatch,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph) {
    for (Predicate<E> edgePred : subgraph.outgoingEdgesOf(thisPred)) {
      if (!singleMatchHelper(
          thisPred,
          thisNode,
          subgraph.getEdgeTarget(edgePred),
          graph.outgoingEdgesOf(thisNode)
              .stream()
              .filter(edgePred)
              .map(e -> graph.getEdgeTarget(e))
              .collect(Collectors.toList()),
          toMatch,
          subgraphToGraph,
          graphToSubgraph)) {
        return false;
      }
    }
    return true;
  }

  private boolean matchIncoming(
      Predicate<V> thisPred,
      V thisNode,
      LinkedList<Pair<Predicate<V>, V>> toMatch,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph) {
    for (Predicate<E> edgePred : subgraph.incomingEdgesOf(thisPred)) {
      if (!singleMatchHelper(
          thisPred,
          thisNode,
          subgraph.getEdgeSource(edgePred),
          graph.incomingEdgesOf(thisNode)
              .stream()
              .filter(edgePred)
              .map(e -> graph.getEdgeSource(e))
              .collect(Collectors.toList()),
          toMatch,
          subgraphToGraph,
          graphToSubgraph)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Test next node
   */
  private boolean singleMatchHelper(
      Predicate<V> thisPred,
      V thisNode,
      Predicate<V> nextPred,
      List<V> nextNodes,
      LinkedList<Pair<Predicate<V>, V>> toMatch,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph) {
    for (V nextNode : nextNodes) {
      Status status = isInMap(nextPred, nextNode, subgraphToGraph, graphToSubgraph);
      if (status == Status.NOT_MATCHED) {
        continue;
      } else if (status == Status.MATCHED) {
        return true;
      }
      LinkedList<Pair<Predicate<V>, V>> toMatchTemp = Lists.newLinkedList(toMatch);
      toMatchTemp.push(Pair.with(thisPred, thisNode));
      toMatchTemp.push(Pair.with(nextPred, nextNode));
      // next node
      if (matchHelper(toMatchTemp, subgraphToGraph, graphToSubgraph)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return 0: matched 1: not matched 2: not found
   */
  private Status isInMap(
      Predicate<V> pred,
      V node,
      Map<Predicate<V>, V> subgraphToGraph,
      Map<V, Predicate<V>> graphToSubgraph) {
    if (subgraphToGraph.containsKey(pred)
        && !graphToSubgraph.containsKey(node)) {
      return Status.NOT_MATCHED;
    }
    if (!subgraphToGraph.containsKey(pred)
        && graphToSubgraph.containsKey(node)) {
      return Status.NOT_MATCHED;
    }
    if (subgraphToGraph.containsKey(pred)
        && graphToSubgraph.containsKey(node)) {
      if (subgraphToGraph.get(pred).equals(node)
          && graphToSubgraph.get(node).equals(pred)) {
        return Status.MATCHED;
      } else {
        return Status.NOT_MATCHED;
      }
    }
    return Status.NOT_FOUND;
  }

  private enum Status {
    MATCHED, NOT_MATCHED, NOT_FOUND
  }

  private enum AllStatus {
    TRUE, FALSE, CONTINUE
  }
}
