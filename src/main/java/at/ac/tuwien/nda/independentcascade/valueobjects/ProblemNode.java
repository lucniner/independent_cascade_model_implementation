package at.ac.tuwien.nda.independentcascade.valueobjects;

import java.util.Objects;

public class ProblemNode implements Comparable {

  private final int id;
  private int flag;
  private ProblemNode prevBest;
  private double marginalGain1;
  private double marginalGain2;

  public ProblemNode(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }


  public int getFlag() {
    return flag;
  }

  public ProblemNode setFlag(final int flag) {
    this.flag = flag;
    return this;
  }

  public ProblemNode getPrevBest() {
    return prevBest;
  }

  public ProblemNode setPrevBest(final ProblemNode prevBest) {
    this.prevBest = prevBest;
    return this;
  }

  public double getMarginalGain1() {
    return marginalGain1;
  }

  public ProblemNode setMarginalGain1(final double marginalGain1) {
    this.marginalGain1 = marginalGain1;
    return this;
  }

  public double getMarginalGain2() {
    return marginalGain2;
  }

  public ProblemNode setMarginalGain2(final double marginalGain2) {
    this.marginalGain2 = marginalGain2;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final ProblemNode problemNode = (ProblemNode) o;
    return id == problemNode.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(final Object o) {
    if (o instanceof ProblemNode) {
      return (int) (((ProblemNode) o).getMarginalGain1() - this.getMarginalGain1());
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
