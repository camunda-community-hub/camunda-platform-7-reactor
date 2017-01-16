package org.camunda.bpm.extension.reactor.util;


import java.util.Objects;

/**
 * Pair representing two elements, left and right.
 *
 * @param <L> type of first element in pair
 * @param <R> type of second element in pair
 */
public class Pair<L, R> {

  public static <L, R> Pair<L, R> of(L left, R right) {
    return new Pair<L, R>(left, right);
  }

  private final L left;
  private final R right;

  private Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public L getLeft() {
    return left;
  }

  public R getRight() {
    return right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public String toString() {
    return "Pair{" +
      "left=" + left +
      ", right=" + right +
      '}';
  }
}
