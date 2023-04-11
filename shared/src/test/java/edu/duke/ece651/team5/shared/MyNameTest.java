package edu.duke.ece651.team5.shared;



import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;



public class MyNameTest {
    @Test
    public void test_getName() {
      assertEquals("team5", MyName.getName());
    }
  
  }
  