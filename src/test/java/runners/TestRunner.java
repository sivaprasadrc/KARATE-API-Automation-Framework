package runners;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRunner {
    @Test
    void triggerFeatures() {
//        Results results = Runner.path("classpath:feature/02APIsequence.feature").parallel(1);
//        assertEquals(0,results.getFailCount(),results.getErrorMessages());
    }
}



//@Test
//void testSample() {
//
//    // Stage 1: Authentication
//    Results authResults = Runner
//            .path("classpath:auth")
//            .parallel(1);
//
//    // Stop if authentication fails
//    assertEquals(0, authResults.getFailCount(),
//            "Authentication failed. Stopping execution.");
//
//    // Stage 2: Business features
//    Results businessResults = Runner
//            .path("classpath:feature/homeLoan.feature")
//            .parallel(1);
//
//    assertEquals(0, businessResults.getFailCount(),
//            businessResults.getErrorMessages());
//}
//@Karate.Test
//Karate test() {
//    return Karate.run("classpath:feature/02APIsequence.feature");
//}
