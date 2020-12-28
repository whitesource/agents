package org.whitesource.agent.api.model;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class DependencyInfoTest {
    @Test
    public void dependencyInfoWithEmptyArgumentsTest() {
        DependencyInfo depInfo = new DependencyInfo();

        Assert.assertEquals(null, depInfo.getVersion());
        Assert.assertEquals(null, depInfo.getArtifactId());
        Assert.assertEquals(null, depInfo.getGroupId());
        Assert.assertEquals(false, depInfo.getOptional());
        Assert.assertEquals(new LinkedList<>(), depInfo.getChildren());
        Assert.assertEquals(new TreeMap<>(), depInfo.getChecksums());
        Assert.assertEquals(new HashMap<>(), depInfo.getDependencyModulesToPaths());
    }

    @Test
    public void dependencyInfoWithArgumentsTest() {
        DependencyInfo depInfo = new DependencyInfo("aaaa", "bbbb", "1.0.0");

        Assert.assertEquals("1.0.0", depInfo.getVersion());
        Assert.assertEquals("bbbb", depInfo.getArtifactId());
        Assert.assertEquals("aaaa", depInfo.getGroupId());
        Assert.assertEquals(false, depInfo.getOptional());
        Assert.assertEquals(new LinkedList<>(), depInfo.getChildren());
        Assert.assertEquals(new TreeMap<>(), depInfo.getChecksums());
        Assert.assertEquals(new HashMap<>(), depInfo.getDependencyModulesToPaths());
    }

    @Test
    public void checkArgumentsWithoutCallingTheGetMethodTest() throws NoSuchFieldException, IllegalAccessException {
        DependencyInfo depInfo = new DependencyInfo();

        Field childrenField = DependencyInfo.class.getDeclaredField("children");
        childrenField.setAccessible(true);
        Field checksumsField = DependencyInfo.class.getDeclaredField("checksums");
        checksumsField.setAccessible(true);
        Field dependencyModulesToPathsField = DependencyInfo.class.getDeclaredField("dependencyModulesToPaths");
        dependencyModulesToPathsField.setAccessible(true);

        Assert.assertEquals(null, childrenField.get(depInfo));
        Assert.assertEquals(null,  checksumsField.get(depInfo));
        Assert.assertEquals(null,  dependencyModulesToPathsField.get(depInfo));
    }

    @Test
    public void checkAddChecksumTest() throws NoSuchFieldException, IllegalAccessException {
        DependencyInfo depInfo = new DependencyInfo();

        Field checksumsField = DependencyInfo.class.getDeclaredField("checksums");
        checksumsField.setAccessible(true);

        ChecksumType type = ChecksumType.ADDITIONAL_SHA1;
        depInfo.addChecksum(type, "");
        Assert.assertEquals(null,  checksumsField.get(depInfo));

        String additionalSha1 = "blablablablabla";
        depInfo.addChecksum(type, additionalSha1);
        Map<ChecksumType, String> csf = (Map<ChecksumType, String>) checksumsField.get(depInfo);
        Assert.assertTrue(csf != null);
        Assert.assertEquals(1, csf.size());
        Assert.assertEquals(additionalSha1, csf.get(type));

        Assert.assertEquals(1, depInfo.getChecksums().size());

        String additionalSha1New = "123123123123123";
        Map<ChecksumType, String> checksumMap = new HashMap<>();
        checksumMap.put(type, additionalSha1New);
        checksumMap.put(ChecksumType.MD5, "md5md5md5md5md5");

        depInfo.setChecksums(checksumMap);
        Assert.assertEquals(2, depInfo.getChecksums().size());
        Assert.assertEquals(additionalSha1New, depInfo.getChecksums().get(type));
    }

    @Test
    public void getChecksumTest() throws NoSuchFieldException, IllegalAccessException {
        DependencyInfo depInfo = new DependencyInfo();

        Field checksumsField = DependencyInfo.class.getDeclaredField("checksums");
        checksumsField.setAccessible(true);

        Assert.assertEquals(null,  checksumsField.get(depInfo));
        Assert.assertTrue(depInfo.getChecksums() != null);
        Assert.assertEquals(0, depInfo.getChecksums().size());
    }

    @Test
    public void childrenTest() throws NoSuchFieldException, IllegalAccessException {
        DependencyInfo depInfo = new DependencyInfo();

        Field childrenField = DependencyInfo.class.getDeclaredField("children");
        childrenField.setAccessible(true);

        Assert.assertEquals(null,  childrenField.get(depInfo));
        Assert.assertTrue(depInfo.getChildren() != null);
        Assert.assertEquals(0, depInfo.getChildren().size());

        DependencyInfo childDep = new DependencyInfo();
        depInfo.getChildren().add(childDep);
        Assert.assertEquals(1, depInfo.getChildren().size());

        DependencyInfo childDep2 = new DependencyInfo();
        depInfo.getChildren().add(childDep2);
        Assert.assertEquals(2, depInfo.getChildren().size());
    }
}
