package com.pengyifan.nlp.brat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Range;

public final class BratUtils {

  private BratUtils() {
  }

  public static List<BratEntity> filtEntities(
      List<BratEntity> entities,
      final String type) {
    return entities.stream().filter(entity -> entity.getType().equals(type))
        .collect(Collectors.toList());
  }

  public static List<BratRelation> remove(
      List<BratRelation> relations, final Collection<String> types) {
    return relations.stream()
        .filter(relation -> types.contains(relation.getType()))
        .collect(Collectors.toList());
  }

  public static Optional<BratEntity> getEnity(List<BratEntity> entities,
      int beginIndex, int endIndex) {
    for (BratEntity entity : entities) {
      if (entity.beginPosition() == beginIndex
          && entity.endPosition() == endIndex) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }

  public static Range<Integer> getRange(BratEntity entity) {
    return Range.closedOpen(entity.beginPosition(), entity.endPosition());
  }
}
