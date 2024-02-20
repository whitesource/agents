//package org.whitesource.agent.client;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
//import org.whitesource.agent.api.dispatch.UpdateType;
//import org.whitesource.agent.api.model.AgentProjectInfo;
//import org.whitesource.agent.api.model.Coordinates;
//import org.whitesource.agent.api.model.DependencyInfo;
//import org.whitesource.agent.api.model.DependencyType;
//
//import java.util.*;
//
//import static org.junit.Assert.*;
//
//public class WssStreamServiceClientImplTest {
//    UpdateInventoryRequest request;
//    Collection<AgentProjectInfo> projects;
//    String url;
//    String userKey;
//    String apiKey;
//
//    @Before
//    public void setUp() throws Exception {
//        url = StringUtils.isNotBlank(System.getenv("MEND_URL")) ? System.getenv("MEND_URL") : "https://saas.whitesourcesoftware.com/agent";
//        apiKey = StringUtils.isNotBlank(System.getenv("MEND_API_KEY")) ? System.getenv("MEND_API_KEY") : "api-key";
//        userKey = StringUtils.isNotBlank(System.getenv("MEND_USER_KEY")) ? System.getenv("MEND_USER_KEY") : "123";
//        AgentProjectInfo project = new AgentProjectInfo();
//        project.setCoordinates(new Coordinates("streaming-test", "streaming-test", "1.0.0"));
//        List<DependencyInfo> dependencies = new ArrayList<>();
//        Collection<DependencyInfo> children = new ArrayList<>();
//
//        for (int i = 0; i < 2; i++) {
//            DependencyInfo dependency = new DependencyInfo();
//            dependency.setArtifactId("log4j-core");
//            dependency.setGroupId("org.apache.logging.log4j");
//            dependency.setVersion("2.17.1");
//            dependency.setType("jar");
//            dependency.setFilename("log4j-core-2.17.1.jar");
//            dependency.setDependencyType(DependencyType.GRADLE);
//            dependency.setDependencyFile("C:\\\\app\\\\build.gradle.kts");
//            dependency.setAdditionalSha1("b6839ce51b64dfefc120600ecb1dc1589ae3ba19");
//            dependency.setSystemPath("C:\\\\.gradle\\\\caches\\\\modules-2\\\\files-2.1\\\\org.apache.logging.log4j\\\\log4j-core\\\\2.17.1\\\\779f60f3844dadc3ef597976fcb1e5127b1f343d\\\\log4j-core-2.17.1.jar");
//            dependency.setSha1("779f60f3844dadc3ef597976fcb1e5127b1f343d");
//            children.add(dependency);
//        }
//        for (int i = 0; i < 2500000; i++) {
//            DependencyInfo dependency = new DependencyInfo();
//            dependency.setArtifactId("guava");
//            dependency.setGroupId("com.google.guava");
//            dependency.setVersion("32.1.2-jre");
//            dependency.setAdditionalSha1("c1613a9c5b54c20bbaa93d5f29e36eb2ab39b1ca");
//            dependency.setType("jar");
//            dependency.setFilename("guava-32.1.2-jre.jar");
//            dependency.setDependencyType(DependencyType.GRADLE);
//            dependency.setDependencyFile("C:\\\\app\\\\build.gradle.kts");
//            dependency.setSystemPath("C:\\\\.gradle\\\\caches\\\\modules-2\\\\files-2.1\\\\com.google.guava\\\\guava\\\\32.1.2-jre\\\\5e64ec7e056456bef3a4bc4c6fdaef71e8ab6318\\\\guava-32.1.2-jre.jar");
//            dependency.setSha1("5e64ec7e056456bef3a4bc4c6fdaef71e8ab6318");
//            dependency.setChildren(children);
//            dependencies.add(dependency);
//        }
//        project.setDependencies(dependencies);
//        projects = new ArrayList<AgentProjectInfo>();
//        projects.add(project);
//
//
//        request = new UpdateInventoryRequest(projects);
//        request.setAgent("fs-agent");
//        request.setAgentVersion("2.9.9.90-SNAPSHOT");
//        request.setOrgToken(apiKey);
//        request.setUserKey(userKey);
//        request.setProduct("streaming-test");
//        request.setTimeStamp(System.currentTimeMillis());
//        request.setPluginVersion("24.1.2");
//        request.setAggregateModules(false);
//        request.setPreserveModuleStructure(false);
//        request.setUpdateType(UpdateType.OVERRIDE);
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    @Ignore
//    public void createHttpRequest() {
//
//        WssStreamServiceClientImpl streamClient = new WssStreamServiceClientImpl(url, false, 60, true);
//        try {
//            streamClient.updateInventory(request);
//
//        } catch (Exception e) {
//            fail("Exception thrown");
//        }
//
//    }
//}