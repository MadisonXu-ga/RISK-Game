package edu.duke.ece651.team5.shared;

import java.io.Serial;
import java.io.Serializable;

public class Soldier extends Unit implements Serializable{
    @Serial
    private static final long serialVersionUID = -8795509670021307809L;

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
