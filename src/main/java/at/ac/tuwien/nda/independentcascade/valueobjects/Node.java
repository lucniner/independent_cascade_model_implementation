package at.ac.tuwien.nda.independentcascade.valueobjects;

import java.util.Objects;

public class Node implements Comparable {

  private final int id;
  private int flag;
  private Node prevBest;
  private double marginalGain1;
  private double marginalGain2;

  public Node(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }


  public int getFlag() {
    return flag;
  }

  public Node setFlag(final int flag) {
    this.flag = flag;
    return this;
  }

  public Node getPrevBest() {
    return prevBest;
  }

  public Node setPrevBest(final Node prevBest) {
    this.prevBest = prevBest;
    return this;
  }

  public double getMarginalGain1() {
    return marginalGain1;
  }

  public Node setMarginalGain1(final double marginalGain1) {
    this.marginalGain1 = marginalGain1;
    return this;
  }

  public double getMarginalGain2() {
    return marginalGain2;
  }

  public Node setMarginalGain2(final double marginalGain2) {
    this.marginalGain2 = marginalGain2;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Node node = (Node) o;
    return id == node.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(final Object o) {
    if (o instanceof Node) {
      return (int) (this.getMarginalGain1() - ((Node) o).getMarginalGain1());
    }
    return 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Node{");
    sb.append("id=").append(id);
    sb.append(", flag=").append(flag);
    sb.append(", marginalGain1=").append(marginalGain1);
    sb.append(", marginalGain2=").append(marginalGain2);
    sb.append('}');
    return sb.toString();
  }
}
