package com.pengyifan.nlp.brat;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;

public final class BratUtils {

  private BratUtils() {
  }

  public static ImmutableList<BratEntity> filtEntities(
      List<BratEntity> entities,
      final String type) {
    return FluentIterable.from(entities).filter(new Predicate<BratEntity>() {

      @Override
      public boolean apply(BratEntity input) {
        return input.getType().equals(type);
      }
    }).toList();
  }

  public static ImmutableList<BratRelation> remove(
      List<BratRelation> relations, final Collection<String> types) {
    return FluentIterable.from(relations)
        .filter(new Predicate<BratRelation>() {

          @Override
          public boolean apply(BratRelation input) {
            return types.contains(input.getType());
          }
        }).toList();
  }

  public static Optional<BratEntity> getEnity(List<BratEntity> entities,
      int beginIndex, int endIndex) {
    for (BratEntity entity : entities) {
      if (entity.beginPosition() == beginIndex
          && entity.endPosition() == endIndex) {
        return Optional.of(entity);
      }
    }
    return Optional.absent();
  }

  public static Range<Integer> getRange(BratEntity entity) {
    return Range.closedOpen(entity.beginPosition(), entity.endPosition());
  }
}
