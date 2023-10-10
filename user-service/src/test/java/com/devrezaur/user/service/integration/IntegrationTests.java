package com.devrezaur.user.service.integration;

import com.devrezaur.user.service.util.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    private static WireMockServer wireMockServer;
    private ObjectMapper objectMapper;
    private Map<String, Map<String, Object>> expectedResponseCollection;
    private Map<String, Set<String>> facadeApis;

    static final String SELECTED_SCENARIO_PREFIX = "deliveryDateDisplay";
    static final String SCENARIO_SUFFIX = "EndOfName";
    static final String PARENT_JSON_FILE_PATH = "src/test/resources/integration";

    @Autowired
    private TestUtil testUtil;

    @BeforeAll
    public static void beforeAll() {
        wireMockServer = new WireMockServer(TestSocketUtils.findAvailableTcpPort());
        wireMockServer.start();
    }

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        expectedResponseCollection = new ConcurrentHashMap<>();
        facadeApis = new ConcurrentHashMap<>();
    }

    @AfterEach
    public void clear() {
        wireMockServer.resetAll();
    }

    @AfterAll
    public static void teardownAll() {
        wireMockServer.stop();
    }

    @ParameterizedTest
    @ArgumentsSource(ScenarioProvider.class)
    @DisplayName("Integration Tests")
    public void runIntegrationTests(String apiScenario) throws JsonProcessingException {
        String[] apiScenarioSubStrings = apiScenario.split("_");
        String apiName = apiScenarioSubStrings[0];
        String scenario = apiScenarioSubStrings[1];

        setupStub(apiName);

        if (expectedResponseCollection.containsKey(apiScenario)) {
            String expectedResponse = String.format("integration/%s/%s_expectedGatewayResponse.json",
                    apiName, scenario);
            String expectedResponseProperties = "";
            Map<String, Object> expectedResponseMap = null;
            Integer expectedResponseHttpStatus = 418;

            expectedResponseProperties = testUtil.loadFromFile(expectedResponse);
            expectedResponseMap = objectMapper.readValue(expectedResponseProperties, Map.class);
            expectedResponseHttpStatus = (Integer) expectedResponseMap.getOrDefault("expectedResponseHttpStatus", 200);

            String requestJson = testUtil.loadFromFile(String.format("integration/%s/%s_request.json", apiName, scenario));
            Map<String, Object> requestMap = objectMapper.readValue(requestJson, Map.class);
            Map<String, Object> requestBody = (Map<String, Object>) requestMap.get("body");
            Map<String, Object> requestHeaders = (Map<String, Object>) requestMap.get("headers");

            if (requestHeaders == null) {
                requestBody = requestMap;
                requestHeaders = new HashMap<>();
            }

        } else {
            Assertions.fail(String.format("'%s' doesn't meet integration test assertion criteria!", apiName));
        }
    }

    private void setupStub(String apiName) throws JsonProcessingException {
        File directory = new File(PARENT_JSON_FILE_PATH + "/" + apiName);
        String[] allowedFileExtensions = new String[]{"json"};
        Collection<File> fileCollection = FileUtils.listFiles(directory, allowedFileExtensions, true);

        List<String> apiScenarioList = fileCollection
                .parallelStream()
                .map(file -> file
                        .getParent()
                        .substring(file.getParent().lastIndexOf(File.separator) + 1) + "_" + file.getName()
                        .substring(0, file.getName().indexOf("_"))
                )
                .distinct()
                .toList();

        for (String apiScenario : apiScenarioList) {
            if (!apiScenario.startsWith(SELECTED_SCENARIO_PREFIX)) {
                continue;
            }

            String[] apiScenarioSubStrings = apiScenario.split("_");
            String scenario = apiScenarioSubStrings[1];
            String mockResponseFilePath =
                    String.format("integration/%s/%s_mockBackendResponse.json", apiName, scenario);
            String mockBackendPropertiesJson = testUtil.loadFromFile(mockResponseFilePath);

            if (StringUtils.isNotEmpty(mockBackendPropertiesJson)) {
                Map<String, Object> mockBackendPropertiesMap =
                        objectMapper.readValue(mockBackendPropertiesJson, Map.class);

                expectedResponseCollection.put(apiScenario, mockBackendPropertiesMap);

                Map<String, Object> expectedRequestMap = (Map<String, Object>) mockBackendPropertiesMap
                        .getOrDefault("expectedRequest", Collections.EMPTY_MAP);

                if (CollectionUtils.isEmpty(expectedRequestMap)) {
                    Set<String> subApis = new HashSet<>();
                    for (Map.Entry<String, Object> entry : mockBackendPropertiesMap.entrySet()) {
                        mappingRequest(entry.getKey(), scenario, (Map<String, Object>) entry.getValue());
                        subApis.add(entry.getKey());
                    }
                    facadeApis.merge(apiName, subApis, (oldApis, newApis) -> {
                        oldApis.addAll(newApis);
                        return oldApis;
                    });
                } else {
                    mappingRequest(apiName, scenario, mockBackendPropertiesMap);
                }
            } else {
                expectedResponseCollection.put(apiScenario, Collections.EMPTY_MAP);
            }
        }
    }

    private void mappingRequest(String apiName, String scenario, Map<String, Object> mockBackendPropertiesMap)
            throws JsonProcessingException {
        Map<String, Object> expectedRequestMap = (Map<String, Object>) mockBackendPropertiesMap
                .getOrDefault("expectedRequest", Collections.EMPTY_MAP);
        Map<String, Object> mockResponseMap = (Map<String, Object>) mockBackendPropertiesMap
                .getOrDefault("mockResponse", Collections.EMPTY_MAP);

        String mockRequestBody;
        Object body = mockResponseMap.get("body");
        if (body instanceof String) {
            mockRequestBody = mockResponseMap.get("body").toString();
        } else {
            try {
                // issue might happen
                mockRequestBody = objectMapper.writeValueAsString(mockResponseMap.getOrDefault("body",
                        Collections.EMPTY_MAP));
            } catch (Exception ex) {
                mockRequestBody = "";
            }
        }

        Map<String, Object> headers = (Map<String, Object>) mockResponseMap
                .getOrDefault("headers", Collections.EMPTY_MAP);
        Map<String, Object> requiredHeaderParameters = (Map<String, Object>) expectedRequestMap
                .getOrDefault("headerParameters", Collections.EMPTY_MAP);
        Map<String, Object> requiredQueryParameters = (Map<String, Object>) expectedRequestMap
                .getOrDefault("queryParameters", Collections.EMPTY_MAP);
        Map<String, Object> requiredRequestBody = (Map<String, Object>) expectedRequestMap
                .getOrDefault("requestBody", Collections.EMPTY_MAP);
        Map<String, Object> requiredConnectionMap = (Map<String, Object>) expectedRequestMap
                .getOrDefault("connectionMap", Collections.EMPTY_MAP);

        String requiredHttpMethod = ((String) requiredConnectionMap.getOrDefault("method", "GET")).toUpperCase();
        Integer mockHttpStatus = (Integer) mockResponseMap.getOrDefault("httpStatus", 200);
        String subPath = (String) expectedRequestMap.getOrDefault("subPath", "");

        MappingBuilder requiredRequestMappingBuilder = getMappingBuilder(apiName, scenario, requiredHttpMethod,
                subPath, requiredQueryParameters);

        requiredHeaderParameterMapping(requiredRequestMappingBuilder, requiredHeaderParameters);
        requiredQueryParameterMapping(requiredRequestMappingBuilder, requiredQueryParameters);
        requiredRequestBodyMapping(requiredRequestMappingBuilder, requiredRequestBody);

        ResponseDefinitionBuilder responseDefinitionBuilder = getResponseDefinitionBuilder(mockRequestBody, headers,
                mockHttpStatus);
        wireMockServer.stubFor(requiredRequestMappingBuilder.willReturn(responseDefinitionBuilder));
    }

    private MappingBuilder getMappingBuilder(String apiName, String scenario, String httpMethod, String subPath,
                                             Map<String, Object> queryParameters) {
        String queryPattern = queryParameters
                .entrySet()
                .stream()
                .filter(entry -> ObjectUtils.isNotEmpty(entry.getValue()) && entry.getValue() instanceof Collection)
                .map(entry -> {
                    List<String> parameterValues = (List<String>) entry.getValue();
                    return parameterValues
                            .stream()
                            .map(value -> {
                                if (value.contains("|")) {
                                    value = value.replaceAll("\\|", "\\\\|");
                                }
                                return value;
                            })
                            .collect(Collectors.joining(String.format(")(?=.*%s=", entry.getKey()),
                                    String.format("(?=.*%s=", entry.getKey()), ").+"));
                }).collect(Collectors.joining());

        String pattern;
        if (subPath.isEmpty())
            pattern = String.format("/%s/%s.*", apiName, scenario + SCENARIO_SUFFIX);
        else {
            subPath = subPath.startsWith("/") ? StringUtils.substringAfter(subPath, "/") : subPath;
            pattern = String.format("/%s/%s/[^\\\\?]*%s.*", apiName, scenario + SCENARIO_SUFFIX, subPath);
        }
        if (StringUtils.isNotEmpty(queryPattern)) {
            pattern += queryPattern;
        }

        UrlPattern urlPathPattern = urlMatching(pattern);
        return switch (httpMethod) {
            case "GET" -> get(urlPathPattern);
            case "POST" -> post(urlPathPattern);
            case "PUT" -> put(urlPathPattern);
            case "DELETE" -> delete(urlPathPattern);
            default -> null;
        };
    }

    private void requiredHeaderParameterMapping(MappingBuilder mappingBuilder,
                                                Map<String, Object> requiredHeaderParameters) {
        if (!requiredHeaderParameters.isEmpty()) {
            requiredHeaderParameters.forEach((key, value) -> mappingBuilder.withHeader(key, equalTo(value.toString())));
        }
    }

    private void requiredQueryParameterMapping(MappingBuilder mappingBuilder,
                                               Map<String, Object> requiredQueryParameters) {
        if (!requiredQueryParameters.isEmpty()) {
            requiredQueryParameters.forEach((key, value) -> {
                if (!(value instanceof List)) {
                    mappingBuilder.withQueryParam(key, equalTo(value.toString()));
                }
            });
        }
    }

    private void requiredRequestBodyMapping(MappingBuilder mappingBuilder,
                                            Map<String, Object> requiredRequestBody) throws JsonProcessingException {
        if (!requiredRequestBody.isEmpty()) {
            mappingBuilder.withRequestBody(equalToJson(objectMapper.writeValueAsString(requiredRequestBody)));
        }
    }

    private ResponseDefinitionBuilder getResponseDefinitionBuilder(String mockedRequestBody,
                                                                   Map<String, Object> headers,
                                                                   Integer mockedHttpStatus) {
        ResponseDefinitionBuilder responseDefinitionBuilder =
                aResponse().withStatus(mockedHttpStatus).withBody(mockedRequestBody);

        for (Map.Entry<String, Object> header : headers.entrySet()) {
            if (header.getValue() instanceof String) {
                responseDefinitionBuilder = responseDefinitionBuilder.withHeader(header.getKey(),
                        header.getValue().toString());
            } else if (header.getValue() instanceof List) {
                List<String> values = (List<String>) header.getValue();
                responseDefinitionBuilder = responseDefinitionBuilder.withHeader(header.getKey(),
                        values.toArray(String[]::new));
            } else {
                throw new RuntimeException("IntegrationTests: Http headers can be either string or list of string! " +
                        "Header used now: " + header.getValue());
            }
        }
        return responseDefinitionBuilder;
    }

    /**
     * Custom implementation of ArgumentProvider class.
     * It will load all the integration test scenario names.
     * And pass it to the @ArgumentsSource as arguments.
     * <p>
     * Argument format: apiName_scenarioName
     */
    private static class ScenarioProvider implements ArgumentsProvider {
        private static List<String> apiScenarioList;

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            populateApiScenarioList();
            if (StringUtils.isNotEmpty(SELECTED_SCENARIO_PREFIX)) {
                apiScenarioList = apiScenarioList
                        .stream()
                        .filter(scenario -> scenario.startsWith(SELECTED_SCENARIO_PREFIX))
                        .collect(Collectors.toList());
            }
            return Stream.of(apiScenarioList.toArray()).map(Arguments::of);
        }

        private void populateApiScenarioList() {
            File directory = new File(PARENT_JSON_FILE_PATH);
            String[] allowedFileExtensions = new String[]{"json"};
            Collection<File> fileCollection = FileUtils.listFiles(directory, allowedFileExtensions, true);
            apiScenarioList = fileCollection
                    .parallelStream()
                    .map(file -> file
                            .getParent()
                            .substring(file.getParent().lastIndexOf(File.separator) + 1) + "_" + file.getName()
                            .substring(0, file.getName().indexOf("_"))
                    )
                    .distinct()
                    .toList();
        }
    }
}
