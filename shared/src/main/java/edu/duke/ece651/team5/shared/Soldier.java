package edu.duke.ece651.team5.shared;

import java.io.Serializable;

public class Soldier extends Unit implements Serializable{
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
  
      return true;
  }

  @Override
  public int hashCode() {
      return 0;
  }
}
