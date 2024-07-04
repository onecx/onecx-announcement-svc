package org.tkit.onecx.announcement.test;

import java.util.List;

import org.tkit.quarkus.security.test.AbstractSecurityTest;
import org.tkit.quarkus.security.test.SecurityTestConfig;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SecurityTest extends AbstractSecurityTest {
    @Override
    public SecurityTestConfig getConfig() {
        SecurityTestConfig config = new SecurityTestConfig();
        config.addConfig("read", "/internal/announcements/id", 404, List.of("ocx-an:read"), "get");
        config.addConfig("write", "/internal/announcements", 400, List.of("ocx-an:write"), "post");
        return config;
    }
}
