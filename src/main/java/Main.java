import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.impl.DMNContextImpl;
import org.kie.dmn.core.util.KieHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    private static final String GROUP_ID = "drool";
    private static final String ARTIFACT_ID = "feel-test";
    private static final String VERSION = "1";
    private static final String FILE_NAME = "alcohol.dmn";

    public static void main(String[] args) {
        DMNRuntime dmnRuntime = createDmnRuntime();
        DMNContextImpl context = createContext();
        evaluateDecision(dmnRuntime, context);
    }

    private static DMNRuntime createDmnRuntime() {
        KieServices ks = KieServices.Factory.get();
        Resource resource = ks.getResources().newClassPathResource(FILE_NAME);
        KieSessionConfiguration configuration = ks.newKieSessionConfiguration();
        KieContainer kieContainer = KieHelper.getKieContainer(ks.newReleaseId(GROUP_ID, ARTIFACT_ID, VERSION), resource);
        return kieContainer.newKieSession(configuration).getKieRuntime(DMNRuntime.class);
    }

    private static void evaluateDecision(DMNRuntime kieRuntime, DMNContextImpl context) {
        DMNResult result = kieRuntime.evaluateByName(kieRuntime.getModels().get(0), context, "Sell alcohol");
        if (result.hasErrors()) {
            for (DMNMessage dmnMessage : result.getMessages()) {
                System.err.println(dmnMessage.getMessage());
            }
        } else {
            System.out.println("Evaluation success!");
        }
    }

    static DMNContextImpl createContext() {
        Map<String, Object> input = new LinkedHashMap<String, Object>();
        input.put("age", 18);
        input.put("country", "PL");
        input.put("day of the week", "Monday");
        return new DMNContextImpl(input);
    }
}
