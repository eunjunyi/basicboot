package com.mor.test.comm.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * @Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "settlement")
@PropertySource(value = {"classpath:/settlement.yml"})

@PropertySource(value = {
    "classpath:/settlement.yml",
    "classpath:/settlement-${spring.profiles.active}.yml"
})
 */
//@ConfigurationProperties(prefix = "settlement", locations = "classpath:/settlement.yml)

@Component
@ConfigurationProperties(prefix="myconf")
public class MyConfig {

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}


/****
 * 
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "settlement")
@PropertySource(value = {
    "classpath:/settlement.yml",
    "classpath:/settlement-${spring.profiles.active}.yml"
}, factory = YamlPropertySourceFactory.class)
public class SettlementProperties {
    @Getter
    @Setter
    private Erp erp;
 
    public static class Erp {
        @Getter
        @Setter
        String url;
    }
}
 
public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        return name != null ? new YamlPropertySourceLoader().load(name, resource.getResource(), null) : new YamlPropertySourceLoader().load(
            getNameForResource(resource.getResource()), resource.getResource(), null);
    }
 
    private static String getNameForResource(Resource resource) {
        String name = resource.getDescription();
 
        if (!StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
 
        return name;
    }
}
***/