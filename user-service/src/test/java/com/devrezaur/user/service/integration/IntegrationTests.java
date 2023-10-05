//package com.devrezaur.user.service.integration;
//
//import com.devrezaur.user.service.util.TestUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.io.FileUtils;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StringUtils;
//
//import java.io.File;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class IntegrationTests {
//
//    private final TestUtil testUtil;
//    private ObjectMapper objectMapper;
//    private Map<String, Map<String, Object>> expectedResponseCollection;
//
//    static final String SELECTED_SCENARIO_PREFIX = "";
//    static final String SCENARIO_SUFFIX = "EndOfName";
//    static final String PARENT_JSON_FILE_PATH = "src/test/resources/integration";
//
//    public IntegrationTests(TestUtil testUtil) {
//        this.testUtil = testUtil;
//    }
//
//    @BeforeEach
//    public void setup() {
//        objectMapper = new ObjectMapper();
//        expectedResponseCollection = new ConcurrentHashMap<>();
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(ScenarioProvider.class)
//    @DisplayName("Integration Tests")
//    public void runIntegrationTests(String apiScenario) {
//        String[] apiScenarioSubStrings = apiScenario.split("_");
//        String apiName = apiScenarioSubStrings[0];
//        String scenario = apiScenarioSubStrings[1];
//
//        setupStub(apiName);
//
//        Assertions.assertTrue(StringUtils.hasLength(apiName));
//
//    }
//
//    private void setupStub(String apiName) throws JsonProcessingException {
//        File directory = new File(PARENT_JSON_FILE_PATH + "/" + apiName);
//        String[] allowedFileExtensions = new String[]{"json"};
//        Collection<File> fileCollection = FileUtils.listFiles(directory, allowedFileExtensions, true);
//
//        List<String> apiScenarioList = fileCollection
//                .parallelStream()
//                .map(file -> file
//                        .getParent()
//                        .substring(file.getParent().lastIndexOf(File.separator) + 1) + "_" + file.getName()
//                        .substring(0, file.getName().indexOf("_"))
//                )
//                .distinct()
//                .toList();
//
//        for (String apiScenario : apiScenarioList) {
//            if (!apiScenario.startsWith(SELECTED_SCENARIO_PREFIX)) {
//                continue;
//            }
//            String[] apiScenarioSubStrings = apiScenario.split("_");
//            String scenario = apiScenarioSubStrings[1];
//
//            String path = String.format("integration/%s/%s_mockBackendResponse.json", apiName, scenario);
//            String mockBackendPropertiesJson = testUtil.loadFromFile(path);
//
//            if (StringUtils.hasLength(mockBackendPropertiesJson)) {
//                Map<String, Object> mockBackendPropertiesMap =
//                        objectMapper.readValue(mockBackendPropertiesJson, Map.class);
//                expectedResponseCollection.put(apiScenario, mockBackendPropertiesMap);
//
//                Map<String> expectedRequestMap = (Map<String, String>) mockBackendPropertiesMap.getOrDefault("expected_request", Collections.EMPTY_MAP);
//
//            }
//
//        }
//
//    }
//
//
//    private static class ScenarioProvider implements ArgumentsProvider {
//        private static List<String> apiScenarioList;
//
//        @Override
//        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
//            populateInterfaceKeyScenarioList();
//            if (StringUtils.hasLength(SELECTED_SCENARIO_PREFIX)) {
//                apiScenarioList = apiScenarioList
//                        .stream()
//                        .filter(scenario -> scenario.startsWith(SELECTED_SCENARIO_PREFIX))
//                        .collect(Collectors.toList());
//            }
//            return Stream.of(apiScenarioList.toArray()).map(Arguments::of);
//        }
//
//        private void populateInterfaceKeyScenarioList() {
//            File directory = new File(PARENT_JSON_FILE_PATH);
//            String[] allowedFileExtensions = new String[]{"json"};
//            Collection<File> fileCollection = FileUtils.listFiles(directory, allowedFileExtensions, true);
//            apiScenarioList = fileCollection
//                    .parallelStream()
//                    .map(file -> file
//                            .getParent()
//                            .substring(file.getParent().lastIndexOf(File.separator) + 1) + "_" + file.getName()
//                            .substring(0, file.getName().indexOf("_"))
//                    )
//                    .distinct()
//                    .toList();
//        }
//    }
//}
