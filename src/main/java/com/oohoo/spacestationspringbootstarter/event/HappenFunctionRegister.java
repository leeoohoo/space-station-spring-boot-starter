package com.oohoo.spacestationspringbootstarter.event;

import com.oohoo.spacestationspringbootstarter.event.annotation.Happen;
import com.oohoo.spacestationspringbootstarter.event.annotation.Trigger;
import com.oohoo.spacestationspringbootstarter.utils.MyStringUtils;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * @Description: 注册 事件发生源 与接收器
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/5
 */
public class HappenFunctionRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        // 代理事件发起注解的方法类
        Set<String> basePackages = getBasePackages(metadata);
        this.handleHappen(basePackages, registry);
        // 将所有事件接收的方法放入容器中
        this.handleTrigger(basePackages, registry);

    }

    private void handleTrigger(Set<String> basePackages, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> trigger = new LinkedHashSet<>();
        this.initBeanByAnnotation(trigger, basePackages, Trigger.class);
        for (BeanDefinition candidateComponent : trigger) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(Trigger.class.getCanonicalName());
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                try {
                    this.registerTrigger(systemClassLoader.loadClass(candidateComponent.getBeanClassName()));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("sss");

            }
        }

    }


    private void registerTrigger(Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredMethods()).forEach(method -> {
            Trigger annotation = method.getAnnotation(Trigger.class);
            if (null != annotation) {
                TriggerMethod triggerMethod = new TriggerMethod();
                triggerMethod.setMethod(method);
                triggerMethod.setHappenName(annotation.happenName());
                triggerMethod.setName(annotation.value());
                triggerMethod.setOrder(annotation.order());
                triggerMethod.setObjectTypes(method.getParameterTypes());
                triggerMethod.setAsync(annotation.async());
                triggerMethod.setTargetName(MyStringUtils.getClassName(aClass.getName()));
                triggerMethod.setIsInterface(aClass.isInterface());
                HappenContext.registerTrigger(triggerMethod);
            }
        });
    }




    private void initBeanByAnnotation(LinkedHashSet<BeanDefinition> beanDefinitions, Set<String> basePackages, Class<?> annotationClass) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            //继承BaseMapper接口，并且是具体类
            AnnotationMetadata metadata1 = metadataReader.getAnnotationMetadata();
            Set<MethodMetadata> happenAnnotations = metadata1.getAnnotatedMethods(annotationClass.getName());
            return happenAnnotations.size() > 0;
        });
        for (String basePackage : basePackages) {
            beanDefinitions.addAll(scanner.findCandidateComponents(basePackage));
        }
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        basePackages.add(
                ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        return basePackages;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }


    private void handleHappen(Set<String> basePackages, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> happenComponents = new LinkedHashSet<>();
        this.initBeanByAnnotation(happenComponents, basePackages, Happen.class);
        for (BeanDefinition candidateComponent : happenComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                // verify annotated class is an interface
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

                GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
                genericBeanDefinition.setBeanClass(HappenConfiguration.class);
                registry.registerBeanDefinition(beanDefinition.getBeanClassName() + "HappenProxy",
                        genericBeanDefinition);
            }
        }
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
