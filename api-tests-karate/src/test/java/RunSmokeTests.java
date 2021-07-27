import static org.junit.jupiter.api.Assertions.assertEquals;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

class RunSmokeTests {

  @Test
  void runSmokeTests() {
    Results results = Runner.path("classpath:components").tags("~@ignore", "@Smoke").parallel(1);
    assertEquals(0, results.getFailCount(), results.getErrorMessages());
  }
}
